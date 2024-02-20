package io.xeros.content.bosses;

import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.content.bosschallenges.ChallengeHandler;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.sql.DiscordWebhook;
import io.xeros.sql.etc.VoteHandler;
import io.xeros.util.Location3D;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;

public class inadequacy {

    public static final int bossID = 3473;


    public static void handleReward(Location3D location, NPC npc, int amountOfDrops, int npcId) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player player = PlayerHandler.players[j];
                if (player.inadequacyDamage > 150) {
                    ChallengeHandler.killChecker(player, 8187, false);
                    Location3D playerLocation = new Location3D(player.getX(), player.getY(), player.getHeight());
                    Server.getDropManager().create(player, npc, playerLocation, amountOfDrops, 3473);
                    player.getNpcDeathTracker().add(NpcDef.forId(npcId).getName(), NpcDef.forId(npcId).getCombatLevel(), 10);
                    player.inadequacyDamage = 0;
                }
            }
        }
    }

}
