package io.xeros.content.bosses;

import io.xeros.Server;
import io.xeros.content.bosschallenges.ChallengeHandler;
import io.xeros.content.bosspoints.BossPoints;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.content.leaderboard.impl.Boxes;
import io.xeros.content.wogw.Wogw;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Location3D;
import io.xeros.util.Misc;

public class AvatarOfDestruction {

    public static int ticketChance(Player player) {
        int chance = 80;
        if (player.tokensSpent > 50000) {
            chance -= 5;
        }
        if (player.tokensSpent > 25000) {
            chance -= 5;
        }
        if (player.tokensSpent > 10000) {
            chance -= 5;
        }
        if (player.tokensSpent > 5000) {
            chance -= 5;
        }
        if (player.tokensSpent > 2500) {
            chance -= 5;
        }
        if (player.tokensSpent > 1000) {
            chance -= 5;
        }
        if (player.tokensSpent > 250) {
            chance -= 5;
        }
        if (Wogw._20_PERCENT_DROP_RATE_TIMER > 0) {
            chance -= 10;
        }
        return chance;
    }

    public static int bossID = 10532;

    public static void handleReward(Location3D location, NPC npc, int amountOfDrops, int npcId) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player player = PlayerHandler.players[j];
                if (player.avatarDamage > 150) {
                    ChallengeHandler.killChecker(player, 8186, false);
                    //if (Misc.hasOneOutOf(ticketChance(player))) {
                      //  player.getItems().addItemUnderAnyCircumstance(5020, 1);
                      //  player.sendMessage("@red@You've received a perk ticket from Avatar.");
                       // PlayerHandler.executeGlobalMessage("@cr36@@red@[Avatar] " + player.getDisplayName() + " has received a perk ticket from Avatar");
                   // }
                    Location3D playerLocation = new Location3D(player.getX(), player.getY(), player.getHeight());
                    Server.getDropManager().create(player, npc, playerLocation, amountOfDrops, 10532);
                    player.getNpcDeathTracker().add(NpcDef.forId(npcId).getName(), NpcDef.forId(npcId).getCombatLevel(), 10);
                    BossPoints.addPoints(player, 10, false);
                    player.avatarDamage = 0;

                }
            }
        }
    }

}
