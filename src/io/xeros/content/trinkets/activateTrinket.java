package io.xeros.content.trinkets;

import io.xeros.Server;
import io.xeros.content.combat.magic.MagicRequirements;
import io.xeros.content.worldevent.events.tournaments.TourneyManager;
import io.xeros.model.entity.player.ClientGameTimer;
import io.xeros.model.entity.player.Player;
import io.xeros.model.multiplayersession.MultiplayerSessionType;
import io.xeros.model.multiplayersession.duel.DuelSession;
import io.xeros.model.multiplayersession.duel.DuelSessionRules;
import io.xeros.util.Misc;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class activateTrinket {

    static final int trinketOfLuck = 75;
    static final int trinketOfVengeance = 76;
    static final int trinketOfProtection = 77;
    static final int trinketOfNinja = 78;
    static final int trinketOfSuperiority = 79;
    static final int trinketOfTricks = 80;
    static final int trinketOfExperience = 81;
    static final int trinketOfTheUndead = 82;
    static final int trinketOfAdvancedWeaponry = 83;
    static final int trinketOfEscape = 84;
    static final int trinketOfEssence = 85;
    static final int trinketOfRestoration = 86;

    public static boolean isTrinket(int itemID) {
        switch (itemID) {
            case trinketOfLuck:
            case trinketOfVengeance:
            case trinketOfProtection:
            case trinketOfNinja:
            case trinketOfSuperiority:
            case trinketOfTricks:
            case trinketOfExperience:
            case trinketOfTheUndead:
            case trinketOfAdvancedWeaponry:
            case trinketOfEscape:
            case trinketOfEssence:
            case trinketOfRestoration:
                return true;
        }
        return false;
    }

    public static void activateTrinket(int itemID, Player player) {
        switch (itemID) {
            case trinketOfLuck:
                trinketOfLuck(player);
                break;
            case trinketOfVengeance:
                trinketOfVengeance(player);
                break;
            case trinketOfProtection:
                trinketOfProtection(player);
                break;
            case trinketOfNinja:
                trinketOfNinja(player);
                break;
            case trinketOfSuperiority:
                trinketOfSuperiority(player);
                break;
            case trinketOfEscape:
                trinketOfEscape(player);
                break;
            case trinketOfTricks:
                trinketOfTricks(player);
                break;
            case trinketOfExperience:
                trinketOfExperience(player);
                break;
            case trinketOfTheUndead:
                trinketOfUndead(player);
                break;
            case trinketOfAdvancedWeaponry:
                trinketOfAdvanced(player);
                break;
            case trinketOfRestoration:
                trinketOfRestoration(player);
                break;
        }
    }

    private static final long TORESTORATION_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(5);

    public static void trinketOfRestoration(Player player) {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TORESTORATION_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfRestorationCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                if (player.specAmount >= 10.0) {
                    player.sendMessage("You do not need to restore any special attack energy.");
                    return;
                }
                // Activate the trinket
                player.gfx100(474);
                if (player.specAmount >= 5.0) {
                    player.specAmount = 10.0;
                } else {
                    player.specAmount = (player.specAmount += 5.0);
                }
                player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
                player.specRestore = 120;
                player.trinketOfRestorationCooldown = System.currentTimeMillis();
                player.sendMessage("Your trinket of restoration restores your special attack.");
            }
    }

    private static final long TRINKET_OF_ADVANCED_DURATION_MS = TimeUnit.MINUTES.toMillis(5);
    private static final long TOADVANCED_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(15);

    public static void trinketOfAdvanced(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfAdvancedActive) {
            long millisecondsRemaining = player.trinketOfAdvancedTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of advanced weaponry is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOADVANCED_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfAdvancedCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                player.trinketOfAdvancedActive = true;
                player.trinketOfAdvancedTime = TRINKET_OF_ADVANCED_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfAdvancedCooldown = System.currentTimeMillis();
                player.sendMessage("Your trinket of advanced weaponry is now active.");
            }
        }
    }

    private static final long TRINKET_OF_UNDEAD_DURATION_MS = TimeUnit.MINUTES.toMillis(5);
    private static final long TOUNDEAD_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(15);

    public static void trinketOfUndead(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfUndeadActive) {
            long millisecondsRemaining = player.trinketOfUndeadTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of the undead is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOUNDEAD_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfUndeadCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                player.trinketOfUndeadActive = true;
                player.trinketOfUndeadTime = TRINKET_OF_UNDEAD_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfUndeadCooldown = System.currentTimeMillis();
                player.sendMessage("your trinket of the undead is now active.");
            }
        }
    }

    private static final long TRINKET_OF_EXPERIENCE_DURATION_MS = TimeUnit.MINUTES.toMillis(10);
    private static final long TOEXP_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(20);

    public static void trinketOfExperience(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfExperienceActive) {
            long millisecondsRemaining = player.trinketOfExperienceTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of experience is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOEXP_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfExperienceCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                player.trinketOfExperienceActive = true;
                player.trinketOfExperienceTime = TRINKET_OF_EXPERIENCE_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfExperienceCooldown = System.currentTimeMillis();
                player.sendMessage("Your trinket of experience is now active.");
            }
        }
    }

    private static final long TRINKET_OF_TRICKS_DURATION_MS = TimeUnit.MINUTES.toMillis(10);
    private static final long TOT_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(20);

    public static void trinketOfTricks(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfTricksActive) {
            long millisecondsRemaining = player.trinketOfTricksTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of tricks is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOT_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfTricksCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                String type = "range";
                int selector = Misc.random(2);
                int chance = Misc.random(99);
                String boost = "negative";
                int boostAmount;
                if (selector == 0) type = "melee"; if (selector == 1) type = "range"; if (selector == 2) type = "magic";
                player.trinketOfTricksType = type;
                if (chance <= 74) {
                    boost = "positive";
                    boostAmount = 15;
                    player.sendMessage("Treat! Your trinket is now granting "+ boostAmount+"% more damage to "+ type);
                } else {
                    boost = "negative";
                    boostAmount = 10;
                    player.sendMessage("Trick! Your trinket is now reducing "+ type+" damage by "+ boostAmount + "%");
                }
                player.trinketOfTricksBoost = boost;
                player.trinketOfTricksActive = true;
                player.trinketOfTricksTime = TRINKET_OF_TRICKS_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfTricksCooldown = System.currentTimeMillis();
            }
        }
    }

    public static void trinketOfEscape(Player player) {
        if (player.wildLevel <= 40 && player.wildLevel >= 1) {
            player.getPA().movePlayer(3095, 3507, 0);
        } else {
            player.sendMessage("You can only use this below level 40 wilderness.");
        }
    }

    public static void trinketOfSuperiority(Player player) {
        if (player.trinketOfSuperiorityActive) {
            player.trinketOfSuperiorityActive = false;
            player.sendMessage("@red@Your trinket of superiority is now disabled.");
        } else {
            player.trinketOfSuperiorityActive = true;
            player.sendMessage("@red@Your trinket of superiority is now active.");
        }
    }

    private static final long TRINKET_OF_NINJA_DURATION_MS = TimeUnit.MINUTES.toMillis(5);
    private static final long TON_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(15);

    public static void trinketOfNinja(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfNinjaActive) {
            long millisecondsRemaining = player.trinketOfNinjaTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of ninja is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TON_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfNinjaCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                player.sendMessage("@red@You have activated your trinket of the ninja.");
                player.trinketOfNinjaActive = true;
                player.trinketOfNinjaTime = TRINKET_OF_NINJA_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfNinjaCooldown = System.currentTimeMillis();
            }
        }
    }

    private static final long TRINKET_OF_LUCK_DURATION_MS = TimeUnit.MINUTES.toMillis(5);
    private static final long TOL_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(15);

    public static void trinketOfLuck(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfLuckActive) {
            long millisecondsRemaining = player.trinketOfLuckTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of luck is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOL_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfLuckCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(474);
                player.sendMessage("@red@You have activated your trinket of luck.");
                player.trinketOfLuckActive = true;
                player.trinketOfLuckTime = TRINKET_OF_LUCK_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfLuckCooldown = System.currentTimeMillis();
            }
        }
    }

    public static void trinketOfVengeance(Player player) {
        player.usingMagic = true;
        if (player.playerLevel[1] < 40) {
            player.sendMessage("You need a defence level of 40 to cast this spell.");
            return;
        }
        if (System.currentTimeMillis() - player.lastCast < 30000) {
            player.sendMessage("You can only cast vengeance every 30 seconds.");
            return;
        }
        if (player.vengOn) {
            player.sendMessage("You already have vengeance casted.");
            return;
        }
        DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
        if (!Objects.isNull(session)) {
            if (session.getRules().contains(DuelSessionRules.Rule.NO_MAGE)) {
                player.sendMessage("You can't cast this spell because magic has been disabled.");
                return;
            }
        }

        player.getPA().sendGameTimer(ClientGameTimer.VENGEANCE, TimeUnit.SECONDS, 30);
        player.startAnimation(8317);
        player.gfx100(726);
        player.getPA().addSkillXPMultiplied(112, 6, true);
        player.getPA().refreshSkill(6);
        player.vengOn = true;
        player.usingMagic = false;
        player.lastCast = System.currentTimeMillis();
    }

    private static final long TRINKET_OF_PROTECTION_DURATION_MS = TimeUnit.MINUTES.toMillis(2);
    private static final long TOP_COOLDOWN_MS = TimeUnit.MINUTES.toMillis(6);

    public static void trinketOfProtection(Player player) {
        // Check if the trinket is already active
        if (player.trinketOfProtectionActive) {
            long millisecondsRemaining = player.trinketOfProtectionTime * 600;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) % 60;
            player.sendMessage("Your trinket of protection is already active with " + minutes + " minutes " + seconds + " seconds remaining.");
        } else {
            // Calculate the remaining time since last activation
            long cooldownRemaining = TOP_COOLDOWN_MS - (System.currentTimeMillis() - player.trinketOfProtectionCooldown);

            if (cooldownRemaining > 0) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) % 60;
                player.sendMessage("Your trinket is currently on cooldown, " + minutes + " minutes " + seconds + " seconds remaining.");
            } else {
                // Activate the trinket
                player.gfx100(658);
                player.sendMessage("@red@You have activated your trinket of protection.");
                player.trinketOfProtectionActive = true;
                player.trinketOfProtectionTime = TRINKET_OF_PROTECTION_DURATION_MS / 600; // Store the duration in ticks
                player.trinketOfProtectionCooldown = System.currentTimeMillis();
            }
        }
    }

}
