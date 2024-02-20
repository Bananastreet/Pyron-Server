package io.xeros.content.bosses;

import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.content.skills.skillrewards.SeasonalTickets;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.sql.DiscordWebhook;
import io.xeros.sql.etc.VoteHandler;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;

public class VoteBoss {

    public static int bossID = 7414;

    public static boolean voteBossActive = false;

    public static boolean voteBossEnabled = true;

    public static int requiredVotes = 45;

    public static int perkDamageRequired = 100;
    public static int perkTicketChance = 5;

    public static int randomDropChance = 3;

    public static int votesRemaining() {
        return requiredVotes - VoteHandler.totalVotes;
    }

    public static void teleportToBoss(Player player) {
        int location = Misc.random(2) + 1;
        if (voteBossActive) {
            if (location == 1)
                player.getPA().spellTeleport(3664, 5786, 0, false);
            if (location == 2)
                player.getPA().spellTeleport(3676, 5780, 0, false);
            if (location == 3)
                player.getPA().spellTeleport(3686, 5791, 0, false);
        } else {
            player.sendMessage("Vote boss is not active, it will spawn in "+votesRemaining() +" votes.");
        }
    }

    public static void spawnVoteBoss() {
        if (voteBossEnabled) {
            PlayerHandler.newsMessage("The vote boss will spawn in 2 minutes, ::voteboss!");
            DiscordWebhook webhook = new DiscordWebhook(Configuration.bossSpawns);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Vote Boss")
                    .setDescription("The vote boss will spawn in 2 minutes, ::voteboss")
                    .setColor(Color.RED));
            try {
                if (Configuration.DiscordEnabled)
                    webhook.execute(); //Handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
            voteBossActive = true;
            CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    NPCSpawning.spawnNpc(bossID, 3674, 5785, 0, 0, 35);
                    PlayerHandler.newsMessage("The vote boss has spawned, ::voteboss!");
                    container.stop();
                }
            }, 200);
        } else {
            System.out.println("The vote boss would of spawned but was disabled.");
        }
    }

    public static void handleReward() {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player player = PlayerHandler.players[j];
                if (player.voteBossDamage > 0) {
                    int coinAmount = player.voteBossDamage * 2000;
                    if (player.getPermAttributes().getOrDefault(Player.BATTLEPASS_WINTER_2023)) {
                        coinAmount *= 2;
                    }
                    int ticketAmount = Math.round(player.voteBossDamage / 10);
                    player.getItems().addItemUnderAnyCircumstance(SeasonalTickets.ticketID, ticketAmount);
                    player.sendMessage("You have received "+ ticketAmount + "x seasonal tickets from the vote boss.");
                    player.getItems().addItemUnderAnyCircumstance(995, coinAmount);
                    player.sendMessage("You have received " + Misc.formatNumber(coinAmount) + " coins for dealing " + player.voteBossDamage + " damage.");
                  /*  if (player.voteBossDamage > perkDamageRequired && Misc.hasOneOutOf(perkTicketChance)) {
                        PlayerHandler.executeGlobalMessage("@cr35@ @blu@[VOTE BOSS] @cya@" + player.getDisplayName() + " received a perk ticket!");
                        player.getItems().addItemUnderAnyCircumstance(5020, 1);
                    } */
                    if (player.voteBossDamage > 125 && Misc.hasOneOutOf(5)) {
                        PlayerHandler.executeGlobalMessage("@cr35@ @blu@[VOTE BOSS] @cya@" + player.getDisplayName() + " received an upgrade token!");
                        player.getItems().addItemUnderAnyCircumstance(23497, 1);
                    }
                    if (player.voteBossDamage > 125 && Misc.hasOneOutOf(6)) {
                        PlayerHandler.executeGlobalMessage("@cr35@ @blu@[VOTE BOSS] @cya@" + player.getDisplayName() + " received a tome of experience!");
                        player.getItems().addItemUnderAnyCircumstance(22415, 1);
                    }
                    if (player.voteBossDamage > 50 && Misc.hasOneOutOf(7)) {
                        PlayerHandler.executeGlobalMessage("@cr35@ @blu@[VOTE BOSS] @cya@" + player.getDisplayName() + " received a drop rate scroll!");
                        player.getItems().addItemUnderAnyCircumstance(11740, 1);
                    }
                    if (player.voteBossDamage > perkDamageRequired) {
                        int randomDrop = Misc.random(3);
                        if (randomDrop == 1) {
                            player.sendMessage("You received a vote crystal from the vote boss.");
                            player.getItems().addItemUnderAnyCircumstance(23933, 1);
                        } else if (randomDrop == 2) {
                            player.sendMessage("You received an xp lamp from the vote boss.");
                            player.getItems().addItemUnderAnyCircumstance(2528, 1);
                        } else if (randomDrop == 3) {
                            player.sendMessage("You received an xp lamp from the vote boss.");
                            player.getItems().addItemUnderAnyCircumstance(2528, 1);
                        } else {
                            player.sendMessage("You received a vote crystal from the vote boss.");
                            player.getItems().addItemUnderAnyCircumstance(23933, 1);
                        }
                    }
                    player.voteBossDamage = 0;
                }
            }
        }
        voteBossActive = false;
    }

}
