package io.xeros.content.bosses;

import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.ItemAssistant;
import io.xeros.sql.DiscordWebhook;
import io.xeros.sql.etc.DonationHandler;
import io.xeros.sql.etc.VoteHandler;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;

public class Abomination {

    public static int bossID = 8262;

    public static boolean abominationActive = false;

    public static boolean abominationEnabled = true;

    public static int requiredCents = 15000;

    public static int otherLootchance = 5;

    public static String amountRemaining() {
        int cents = requiredCents - DonationHandler.totalCents;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String formattedAmount = decimalFormat.format((double) cents / 100);
        return formattedAmount;
    }

    public static void teleportToBoss(Player player) {
        int location = Misc.random(2) + 1;
        if (abominationActive) {
            if (location == 1)
                player.getPA().spellTeleport(3732, 3972, 0, false);
            if (location == 2)
                player.getPA().spellTeleport(3738, 3979, 0, false);
            if (location == 3)
                player.getPA().spellTeleport(3745, 3972, 0, false);
        } else {
            player.sendMessage("Abomination is not active, $"+amountRemaining() +" more in donations required.");
        }
    }

    public static void spawnBoss() {
        if (abominationEnabled) {
            PlayerHandler.newsMessage("The donation boss will spawn in 2 minutes, ::abomination!");
            DiscordWebhook webhook = new DiscordWebhook(Configuration.bossSpawns);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Abomination (donator boss)")
                    .setDescription("The abomination donator boss will spawn in 2 minutes at ::abomination")
                    .setColor(Color.RED));
            try {
                if (Configuration.DiscordEnabled)
                    webhook.execute(); //Handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
            abominationActive = true;
            CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    NPCSpawning.spawnNpc(bossID, 3738, 3972, 0, 0, 10);
                    PlayerHandler.newsMessage("The abomination donator boss has spawned, ::abomination!");
                    container.stop();
                }
            }, 200);
        } else {
            System.out.println("The abomination would of spawned but was disabled.");
        }
    }

    public static int tokensToGive(int damage) {
        if (damage < 150) {
            return 0;
        }
        if (damage >= 150 && damage < 1000) {
            return 8;
        }
        if (damage >= 1000 && damage < 2500) {
            return 20;
        }
        if (damage >= 2500) {
            return 30;
        }
        return 5;
    }

    public static void handleReward() {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player player = PlayerHandler.players[j];
                if (player.abominationDamage > 150) {
                    player.getNpcDeathTracker().add(NpcDef.forId(bossID).getName(), NpcDef.forId(bossID).getCombatLevel(), 50);
                    if (player.abominationDamage >= 150 && player.abominationDamage < 1000) {
                        int donatorTokens = tokensToGive(player.abominationDamage);
                        int exchangeTickets = Misc.random(15, 20);
                        player.getItems().addItemUnderAnyCircumstance(7478, donatorTokens);
                        player.getItems().addItemUnderAnyCircumstance(4067, exchangeTickets);
                        player.getItems().addItemUnderAnyCircumstance(995, Misc.random(3000000, 5000000));
                        player.sendMessage("You receive "+ donatorTokens +"x donator tokens, "+ exchangeTickets +"x exchange tickets and coins. Damage: "+player.abominationDamage);
                    }
                    if (player.abominationDamage >= 500 && Misc.hasOneOutOf(5)) {
                        player.getItems().addItemUnderAnyCircumstance(22415, 1);
                        PlayerHandler.executeGlobalMessage("@cr35@ @red@[ABOMINATION] @cya@" + player.getDisplayName() + " received a tome of experience.");
                    }
                    if (player.abominationDamage >= 1000 && player.abominationDamage < 2500) {
                        int donatorTokens = tokensToGive(player.abominationDamage);
                        int exchangeTickets = Misc.random(20, 25);
                        player.getItems().addItemUnderAnyCircumstance(7478, donatorTokens);
                        player.getItems().addItemUnderAnyCircumstance(4067, exchangeTickets);
                        player.getItems().addItemUnderAnyCircumstance(995, Misc.random(5000000, 7000000));
                        player.sendMessage("You receive "+ donatorTokens +"x donator tokens, "+ exchangeTickets +"x exchange tickets and coins. Damage: "+player.abominationDamage);
                    }
                    if (player.abominationDamage >= 2500) {
                        int donatorTokens = tokensToGive(player.abominationDamage);
                        int exchangeTickets = Misc.random(25, 30);
                        player.getItems().addItemUnderAnyCircumstance(7478, donatorTokens);
                        player.getItems().addItemUnderAnyCircumstance(4067, Misc.random(25, 30));
                        player.getItems().addItemUnderAnyCircumstance(995, Misc.random(7000000, 10000000));
                        player.sendMessage("You receive "+ donatorTokens +"x donator tokens, "+ exchangeTickets +"x exchange tickets and coins. Damage: "+player.abominationDamage);
                    }
                    if (player.abominationDamage > 150 && Misc.hasOneOutOf(otherLootchance)) {
                        int tokenAmount = Misc.random(2, 4);
                        int perkAmount = Misc.random(3, 5);
                     //   player.getItems().addItemUnderAnyCircumstance(5020, perkAmount);
                        player.getItems().addItemUnderAnyCircumstance(23497, tokenAmount);
                        PlayerHandler.executeGlobalMessage("@cr35@ @red@[ABOMINATION] @cya@" + player.getDisplayName() + " received "+ tokenAmount +" upgrade tokens");
                    }
                    if (player.abominationDamage > 150 && Misc.hasOneOutOf(40)) {
                        int randomPhat[] = {3706, 3707, 3708, 3709, 3710};
                        int reward = randomPhat[(int) (Math.random()*randomPhat.length)];
			            player.getItems().addItemUnderAnyCircumstance(reward, 1);
                        PlayerHandler.executeGlobalMessage("@cr35@ @red@[ABOMINATION] @cya@" + player.getDisplayName() + " received "+ ItemAssistant.getItemName(reward) + "");

                    }
                    if (player.abominationDamage > 1 && player.abominationDamage < 150) {
                        player.sendMessage("You did not do enough damage for a drop. Damage: "+player.abominationDamage);
                    }
                    player.abominationDamage = 0;
                }
            }
        }
        abominationActive = false;
    }

}
