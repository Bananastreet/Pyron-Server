package io.xeros.content.bosschallenges;

import io.xeros.Configuration;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.ItemAssistant;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChallengeHandler {

    public static final int boxID = 7500;

    public static boolean possible(Player player) {
        if (player.bc1Amount > 0) return false;
        if (player.bc2Amount > 0) return false;
        if (player.bc3Amount > 0) return false;
        if (player.bc4Amount > 0) return false;
        if (player.bc5Amount > 0) return false;
        return true;
    }

    public static void inform(Player player) {
        if (player.bc1Amount > 0) {
            player.sendMessage("[Boss Challenge] Boss 1: kill " + player.bc1Amount + "x " + getName(player.bc1));
        } else {
            player.sendMessage("[Boss Challenge] Boss 1: None");
        }
        if (player.bc2Amount > 0) {
            player.sendMessage("[Boss Challenge] Boss 2: kill " + player.bc2Amount + "x " + getName(player.bc2));
        } else {
            player.sendMessage("[Boss Challenge] Boss 2: None");
        }
        if (player.bc3Amount > 0) {
            player.sendMessage("[Boss Challenge] Boss 3: kill " + player.bc3Amount + "x " + getName(player.bc3));
        } else {
            player.sendMessage("[Boss Challenge] Boss 3: None");
        }
        if (player.bc4Amount > 0) {
            player.sendMessage("[Boss Challenge] Boss 4: kill " + player.bc4Amount + "x " + getName(player.bc4));
        } else {
            player.sendMessage("[Boss Challenge] Boss 4: None");
        }
        if (player.bc5Amount > 0) {
            player.sendMessage("[Boss Challenge] Boss 5: kill " + player.bc5Amount + "x " + getName(player.bc5));
        } else {
            player.sendMessage("[Boss Challenge] Boss 5: None");
        }
    }

    private static void randomLoot(Player player, int npcID) {
            if (npcID == 1672 || npcID == 1673 || npcID == 1674 || npcID == 1675 || npcID == 1676 || npcID == 1677) {
                return;
            }

        int[] challengeTable = {23497,  25837};
        int grabItem = challengeTable[new Random().nextInt(challengeTable.length)];
        String lootName = ItemAssistant.getItemName(grabItem);
        if (Misc.hasOneOutOf(100)) {
            player.getItems().addItemUnderAnyCircumstance(grabItem, 1);
            player.sendMessage("You've received a "+ lootName + " from the boss challenge table.");
        }
    }

    public static String getName(int npcID) {
        switch (npcID) {
            case 8184:
                return "Chambers of Xeric";
            case 8185:
                return "Theatre of Blood";
            case 8186:
                return "Avatar of Destruction";
            case 8187:
                return "The Inadequacy";
        }
        return NpcDef.forId(npcID).getName();
    }

    public static void killChecker(Player player, int npcID, boolean precomplete) {

        if (npcID == 10532) return; //avatar
        if (npcID == 3473) return; //the inadequacy
        if (player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) return;

        int typeKilled = 0;

        if (precomplete) {

        }

        if (NpcDef.forId(npcID).getName().equalsIgnoreCase(NpcDef.forId(player.bc1).getName())) typeKilled = 1;
        if (NpcDef.forId(npcID).getName().equalsIgnoreCase(NpcDef.forId(player.bc2).getName())) typeKilled = 2;
        if (NpcDef.forId(npcID).getName().equalsIgnoreCase(NpcDef.forId(player.bc3).getName())) typeKilled = 3;
        if (NpcDef.forId(npcID).getName().equalsIgnoreCase(NpcDef.forId(player.bc4).getName())) typeKilled = 4;
        if (NpcDef.forId(npcID).getName().equalsIgnoreCase(NpcDef.forId(player.bc5).getName())) typeKilled = 5;

        if (typeKilled == 1 || typeKilled == 2 || typeKilled == 3 || typeKilled == 4 || typeKilled == 5) {
            randomLoot(player, npcID);
        }

        if (typeKilled == 1 && player.bc1Amount == 1) {
            player.bc1Amount--;
            player.sendMessage("[Boss Challenge] You've finished killing "+ getName(npcID));
        }
        if (typeKilled == 2 && player.bc2Amount == 1) {
            player.bc2Amount--;
            player.sendMessage("[Boss Challenge] You've finished killing "+ getName(npcID));
        }
        if (typeKilled == 3 && player.bc3Amount == 1) {
            player.bc3Amount--;
            player.sendMessage("[Boss Challenge] You've finished killing "+ getName(npcID));
        }
        if (typeKilled == 4 && player.bc4Amount == 1) {
            player.bc4Amount--;
            player.sendMessage("[Boss Challenge] You've finished killing "+ getName(npcID));
        }
        if (typeKilled == 5 && player.bc5Amount == 1) {
            player.bc5Amount--;
            player.sendMessage("[Boss Challenge] You've finished killing "+ getName(npcID));
        }

        if (typeKilled == 1 && player.bc1Amount > 1) {
            player.bc1Amount--;
            player.sendMessage("[Boss Challenge] You need to kill "+ player.bc1Amount + " more "+ getName(npcID));
        }
        if (typeKilled == 2 && player.bc2Amount > 1) {
            player.bc2Amount--;
            player.sendMessage("[Boss Challenge] You need to kill "+ player.bc2Amount + " more "+ getName(npcID));
        }
        if (typeKilled == 3 && player.bc3Amount > 1) {
            player.bc3Amount--;
            player.sendMessage("[Boss Challenge] You need to kill "+ player.bc3Amount + " more "+ getName(npcID));
        }
        if (typeKilled == 4 && player.bc4Amount > 1) {
            player.bc4Amount--;
            player.sendMessage("[Boss Challenge] You need to kill "+ player.bc4Amount + " more "+ getName(npcID));
        }
        if (typeKilled == 5 && player.bc5Amount > 1) {
            player.bc5Amount--;
            player.sendMessage("[Boss Challenge] You need to kill "+ player.bc5Amount + " more "+ getName(npcID));
        }


        if (typeKilled == 1 && player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            complete(player);
        }
        if (typeKilled == 2 && player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            complete(player);
        }
        if (typeKilled == 3 && player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            complete(player);
        }
        if (typeKilled == 4 && player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            complete(player);
        }
        if (typeKilled == 5 && player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            complete(player);
        }


    }

    public static int taskAmount(int npcID) {
        switch (npcID) {
            case 6342: //barrelchest
                return Misc.random(6, 12);
                //barrows
            case 1672:
            case 1673:
            case 1674:
            case 1675:
            case 1676:
            case 1677:
                return Misc.random(25, 40);
            case 6496: //dagannoth supreme
            case 6497: //dagannoth prime
            case 6498: //dagannoth rex
                return Misc.random(5, 10);
            case 6477: //mutant tarn
                return 4;
            case 5779: //giant mole
                return Misc.random(5, 10);
            case 963: //kalphite queen
                return Misc.random(6, 12);
            case 6766: //lizardman shaman
                return Misc.random(10, 20);
            case 8713: //sarachnis
                return Misc.random(5, 8);
            case 499: //thermonuclear smoke devil
                return Misc.random(6, 10);
            case 7144: //demonic gorilla
                return Misc.random(15, 25);
            case 2215: //general graardor
            case 2205: //commander zilyana
            case 3129: //kril tsutsaroth
            case 3162: //kree arra
                return Misc.random(7, 12);
            case 2042: //zulrah
                return Misc.random(8, 15);
            case 8026: //vorkath
                return Misc.random(4, 8);
            case 239: //king black dragon
                return Misc.random(9, 14);
            case 6611: //vetion
                return Misc.random(3, 6);
            case 6503: //callisto
                return Misc.random(7, 11);
            case 6615: //scorpia
                return Misc.random(5, 9);
            case 6504: //venenatis
                return Misc.random(3, 5);
            case 6505: //chaos elemental
                return Misc.random(7, 11);
            case 6619: //chaos fanatic
                return Misc.random(6, 12);
            case 6618: //crazy archeologist
                return Misc.random(5, 10);
            case 8184: //cox
                return Misc.random(1, 2);
            case 8185: //tob
                return 1;
            case 8186: //avatar of destruction
                return Misc.random(4, 8);
            case 8187: //the inadequacy
                return Misc.random(3, 6);
            case 494: //kraken
                return Misc.random(5, 10);
            case 11246: //maledictus
                return Misc.random(3, 5);
        }
        return Misc.random(10, 20);
    }

    public static int[] bosses = {494, 8184, 8185, 8186, 8187, 6342, 6496, 6497, 6498, 6477, 5779, 963, 6766, 8713, 499,
            7144, 2215, 2205, 3129, 3162, 2042, 8026, 239, 6611, 6503, 6615, 6504, 6505, 6619, 6618, 1673, 1672, 1674, 1675, 1676, 1677};

    public static void assignChallenge(Player player) {
        if (!possible(player)) {
            player.sendMessage("@red@You already have a boss challenge assigned to you.");
            inform(player);
            return;
        }

        if (!player.getItems().playerHasItem(995, 10000000) && !player.getItems().playerHasItem(25837, 1)) {
            player.sendMessage("Obtaining a boss challenge costs 10 million coins.");
            return;
        }

        if (player.getItems().playerHasItem(25837, 1)) {
            player.getItems().deleteItem(25837, 1);
        } else {
            player.getItems().deleteItem(995, 10000000);
        }


        List<Integer> bossList = new ArrayList<>();
        for (int boss : bosses) {
            bossList.add(boss);
        }

        Collections.shuffle(bossList, new Random());

        player.bc1 = bossList.get(0);
        player.bc2 = bossList.get(1);
        player.bc3 = bossList.get(2);
        player.bc4 = bossList.get(3);
        player.bc5 = bossList.get(4);

        player.bc1Amount = taskAmount(player.bc1);
        player.bc2Amount = taskAmount(player.bc2);
        player.bc3Amount = taskAmount(player.bc3);
        player.bc4Amount = taskAmount(player.bc4);
        player.bc5Amount = taskAmount(player.bc5);

        inform(player);
    }

    public static void cancelChallenge(Player player) {
        if (player.bc1Amount == 0 && player.bc2Amount == 0 && player.bc3Amount == 0 && player.bc4Amount == 0 && player.bc5Amount == 0) {
            player.sendMessage("You do not have a boss challenge to cancel.");
            return;
        }
        if (player.getItems().playerHasItem(995, 5000000)) {
            player.getItems().deleteItem(995, 5000000);
            player.bc1Amount = 0;
            player.bc2Amount = 0;
            player.bc3Amount = 0;
            player.bc4Amount = 0;
            player.bc5Amount = 0;
            player.sendMessage("Your boss challenge has been cancelled.");
        } else {
            player.sendMessage("You need 5 million coins to cancel your challenge.");
        }
    }

    public static void complete(Player player) {
        player.totalBC++;
        player.sendMessage("You've completed your boss challenge. Total completions: " + player.totalBC);
        if (player.hasPerk(Player.BOSS_CHALLENGER) && Misc.hasOneOutOf(5)) {
            player.getItems().addItemUnderAnyCircumstance(boxID, 1);
            player.sendMessage("Your boss challenger perk granted you an extra box. ");
        }
        player.getItems().addItemUnderAnyCircumstance(boxID, 1);
        PlayerHandler.dropMessage(player.getDisplayNameFormatted() + " successfully finished a boss challenge!");
        if (Configuration.leaderboardEnabled) {
            LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MISCELLANEOUS, player.totalBC, io.xeros.content.leaderboard.impl.Misc.BOSS_CHALLENGES);
        }
    }

}
