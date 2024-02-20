package io.xeros.content.chestkeys;

import io.xeros.Server;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Misc;

public class RandomChestKey {

    public static boolean doubleKeys;
    private static final int chestKey = 432;

    private static int getChance(String skillName) {
        switch (skillName) {
            case "woodcutting":
            case "thieving":
            case "mining":
            case "fishing":
            case "cooking":
            case "herblore":
                return 1250;
            case "smithing":
            case "firemaking":
                return 1000;
            case "runecrafting":
            case "farming":
                return 350;
            case "slayer":
            case "agility":
                return 250;
            default:
                return -1;
        }
    }

    public static void handleChance(Player player, String skillName) {
        if (Boundary.isIn(player, Boundary.HESPORI)) {
            return;
        }
        if (getChance(skillName) == -1) { return; }
        var originalChance = getChance(skillName);
        //   if (player.getItems().playerHasItem(26062, 1) || player.getItems().playerHasItem(26146, 1)) {
        //    originalChance *= 0.9;
     //   }
        if (player.hasPerk(Player.CHEST_HUNTER)) {
            originalChance *= 0.9;
        }
        var chance = Misc.hasOneOutOf(originalChance);
        var keyAmount = doubleKeys ? 2 : 1;
        var chestKeyGrammar = doubleKeys ? "chest keys" : "chest key";

      /*  if (Misc.hasOneOutOf(30000)) {
            if (player.getItems().hasRoomInInventory(26062, 1)) {
                player.getItems().addItem(26062, 1);
                PlayerHandler.executeGlobalMessage("@cr35@ @blu@[SIGIL] @red@" + player.getDisplayName() + " has a received Sigil of Treasure from " + Misc.capitalize(skillName) + "!");
            } else {
                Server.itemHandler.createGroundItem(player, 26062, player.absX, player.absY, player.heightLevel, 1, player.getIndex());
            }
        } */

      /*  if (Misc.hasOneOutOf(6000)) {
            player.sendMessage("You have received a perk ticket.");
            PlayerHandler.executeGlobalMessage("@cr35@ @blu@[PERKS] @red@" + player.getDisplayName() + " has a received perk ticket from " + Misc.capitalize(skillName) + "!");
            if (player.getItems().hasRoomInInventory(5020, 1)) {
                player.getItems().addItem(5020, 1);
            } else {
                Server.itemHandler.createGroundItem(player, 5020, player.absX, player.absY, player.heightLevel, 1, player.getIndex());
            }
        } */

        if (chance) {
            if (player.getItems().hasRoomInInventory(chestKey, keyAmount)) {
                player.getItems().addItem(chestKey, keyAmount);
            } else {
                Server.itemHandler.createGroundItem(player, chestKey, player.absX, player.absY, player.heightLevel, keyAmount, player.getIndex());
            }

            //PlayerHandler.dropMessage(player.getDisplayName() + " has received " + keyAmount + " x " +
                  //  chestKeyGrammar + " from " + Misc.capitalize(skillName) + "!");
        }
    }

}
