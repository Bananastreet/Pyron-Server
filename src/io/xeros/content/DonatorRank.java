package io.xeros.content;

import io.xeros.Configuration;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.content.leaderboard.impl.Misc;
import io.xeros.content.leaderboard.impl.Raid;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Right;
import io.xeros.model.entity.player.mode.ModeType;

import java.util.Arrays;
import java.util.List;

public class DonatorRank {

    enum RankUpgrade {
        SAPPHIRE("Sapphire", Right.SAPPHIRE_DONATOR, 50),
        EMERALD("Emerald", Right.EMERALD_DONATOR, 100),
        RUBY("Ruby", Right.RUBY_DONATOR, 250),
        DIAMOND("Diamond", Right.DIAMOND_DONATOR, 500),
        ONYX("Onyx", Right.ONYX_DONATOR, 1000),
        ZENYTE("Zenyte", Right.ZENYTE_DONATOR, 1500),
        DIVINE("Divine", Right.DIVINE_DONATOR, 2500);

        public final String rankName;
        public final Right right;
        public final int amount;

        RankUpgrade(String rankName, Right right, int amount) {
            this.rankName = rankName;
            this.right = right;
            this.amount = amount;
        }
    }

    public static void addTokensSpent(Player player, int amount) {
        player.getQuestTab().updateInformationTab();
        player.tokensSpent += amount;
        player.getDonationRewards().increaseDonationAmount(amount);
        //player.updateRank();
        updateRank(player, determineNextRank(player));
        if (Configuration.leaderboardEnabled) {
            LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MISCELLANEOUS, player.tokensSpent, Misc.DONATOR_TOKENS);
        }
    }

    private static void updateRank(Player player, Right right) {
        if (player.getRights().contains(right)) { return; }

        var primaryRights = new Right[] { Right.YOUTUBER, Right.ROGUE_HARDCORE_IRONMAN,
                Right.ROGUE_IRONMAN, Right.ROGUE, Right.GROUP_IRONMAN, Right.IRONMAN, Right.HC_IRONMAN,
                Right.ULTIMATE_IRONMAN, Right.OSRS, Right.HELPER, Right.MODERATOR, Right.OWNER,
                Right.ADMINISTRATOR, Right.GAME_DEVELOPER };

        //If the player has a primary right, such as special game modes, we don't want to override it
        var hasPrimaryRight = Arrays.stream(primaryRights).anyMatch(player.getRights()::contains);
        if (hasPrimaryRight) {
            player.getRights().add(right);
        } else {
            player.getRights().setPrimary(right);
        }

        player.getPA().requestUpdates();
        player.sendMessage("New rank unlocked: " + right.toString() + "!");
        if (!right.toString().toLowerCase().contains("sapphire")) {
            PlayerHandler.executeGlobalMessage("@cr36@@red@[RANK] @blu@" + player.getDisplayName() + " has reached the rank " + right.toString());
        }
        if (player.clan != null) {
            player.clan.updateDisplayName(player);
        }
    }

    private static Right determineNextRank(Player player) {
        List<RankUpgrade> ranks = Arrays.asList(RankUpgrade.values());
        for (int i = ranks.size() - 1; i >= 0; i--) {
            var rank = ranks.get(i);
            if (player.tokensSpent < rank.amount) {
                continue;
            }
            if (player.getRights().isOrInherits(rank.right) || player.getRights().getPrimary().equals(rank.right)) {
                continue;
            }
            return rank.right;
        }
        return player.getRights().getPrimary();
    }

    public static Right getHighestRank(Player player) {
        List<RankUpgrade> ranks = Arrays.asList(RankUpgrade.values());
        for (int i = ranks.size() - 1; i >= 0; i--) {
            var rank = ranks.get(i);
            if (player.tokensSpent >= rank.amount) {
                return rank.right;
            }
        }
        return player.getRights().getPrimary();
    }

    /* Below is handling for donator benefits based on rank. I decided to do them in this class, rather than
    * individual classes so they are contained in one area, and easy to change in the future if we ever need to.
    * This is primarily just the number lowering for specific things based on rank.
    *
    * Note: slayer point increase and trading post listing increase is done in the respective classes.*/

// HERE IS NO DROPRATE ADDED



    /**
     * Determines bonus pkp points for each rank
     */
    public static int determineBonusPkp(Right right) {
        if (right == Right.DIVINE_DONATOR) {
            return 12;
        } else if (right == Right.ZENYTE_DONATOR) {
            return 10;
        } else if (right == Right.ONYX_DONATOR) {
            return 8;
        } else if (right == Right.DIAMOND_DONATOR) {
            return 6;
        } else if (right == Right.RUBY_DONATOR) {
            return 4;
        } else if (right == Right.EMERALD_DONATOR) {
            return 2;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return 1;
        }
        return 0;
    }

    public static int getDroprate(Right right) { //can you test it
        if (right == Right.DIVINE_DONATOR) {
            return 15;
        } else if (right == Right.ZENYTE_DONATOR) {
            return 12;
        } else if (right == Right.ONYX_DONATOR) {
            return 8;
        } else if (right == Right.DIAMOND_DONATOR) {
            return 6;
        } else if (right == Right.RUBY_DONATOR) {
            return 5;
        } else if (right == Right.EMERALD_DONATOR) {
            return 4;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return 3;
        }
        return 0;
    }

    /**
     * Determines the yell timers for each rank
     */
    public static long getYellTimer(Right right) {
        if (right == Right.MODERATOR) {
            return 0;
        }
        if (right == Right.DIVINE_DONATOR) {
            return 0;
        } else if (right == Right.ZENYTE_DONATOR) {
            return 5;
        } else if (right == Right.ONYX_DONATOR) {
            return 10;
        } else if (right == Right.DIAMOND_DONATOR) {
            return 20;
        } else if (right == Right.RUBY_DONATOR) {
            return 30;
        } else if (right == Right.EMERALD_DONATOR) {
            return 40;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return 50;
        }
        return 60;
    }

    /**
     * Determines the amount of cannonballs for each rank
     */
    public static int determineCannonAmmo(Right right, Player player) {
        int rogueBonus = 0;
        if (player.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN || player.getMode().getType() == ModeType.ROGUE || player.getMode().getType() == ModeType.ROGUE_IRONMAN) {
            rogueBonus = 30;
        }
        if (right == Right.DIVINE_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 130 + rogueBonus : 65 + rogueBonus;
        } else if (right == Right.ZENYTE_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 120 + rogueBonus : 60 + rogueBonus;
        } else if (right == Right.ONYX_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 110 + rogueBonus : 55 + rogueBonus;
        } else if (right == Right.DIAMOND_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 100 + rogueBonus : 50 + rogueBonus;
        } else if (right == Right.RUBY_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 90 + rogueBonus : 45 + rogueBonus;
        } else if (right == Right.EMERALD_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 80 + rogueBonus : 40 + rogueBonus;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return player.hasPerk(Player.CANNON_CAPACITY) ? 70 + rogueBonus : 35 + rogueBonus;
        }
        return player.hasPerk(Player.CANNON_CAPACITY) ? 60 + rogueBonus : 30 + rogueBonus;
    }

    /**
     * Determines the cost of blocking a slayer task for each rank
     */
    public static int determineBlockCost(Right right) {
        if (right == Right.DIVINE_DONATOR) {
            return 40;
        } else if (right == Right.ZENYTE_DONATOR) {
            return 50;
        } else if (right == Right.ONYX_DONATOR) {
            return 60;
        } else if (right == Right.DIAMOND_DONATOR) {
            return 65;
        } else if (right == Right.RUBY_DONATOR) {
            return 70;
        } else if (right == Right.EMERALD_DONATOR) {
            return 75;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return 80;
        }
        return 100;
    }

    /**
     * Determines the cost of cancelling a slayer task for each rank
     */
    public static int determineCancelCost(Right right) {
        if (right == Right.DIVINE_DONATOR) {
            return 10;
        } else if (right == Right.ZENYTE_DONATOR) {
            return 12;
        } else if (right == Right.ONYX_DONATOR) {
            return 15;
        } else if (right == Right.DIAMOND_DONATOR) {
            return 18;
        } else if (right == Right.RUBY_DONATOR) {
            return 20;
        } else if (right == Right.EMERALD_DONATOR) {
            return 23;
        } else if (right == Right.SAPPHIRE_DONATOR) {
            return 25;
        }
        return 30;
    }

}
