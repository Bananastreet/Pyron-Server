package io.xeros.content.commands.all;

import io.xeros.Server;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

import java.util.Optional;

public class sigils extends Command {

    public static boolean buttons(Player player, int buttonID) {
        switch (buttonID) {
            case 179191:
            case 179192:
            case 179193:
            case 179194:
            case 179195:
            case 179196:
            case 179197:
            case 179198:
            case 179199:
            case 179200:
                displayInformation(player, buttonID);
                return true;
        }
        return false;
    }

    public static void sigilNames(Player player) {
        player.getPA().sendFrame126("Sigil of Brutality", 46015); //Sigil 1
        player.getPA().sendFrame126("Sigil of Wealth", 46016); //Sigil 2
        player.getPA().sendFrame126("Sigil of Piety", 46017); //Sigil 3
        player.getPA().sendFrame126("Sigil of Archery", 46018); //Sigil 4
        player.getPA().sendFrame126("Sigil of Wizardy", 46019); //Sigil 5
        player.getPA().sendFrame126("Sigil of Experience", 46020); //Sigil 6
        player.getPA().sendFrame126("Sigil of Treasure", 46021); //Sigil 7
        player.getPA().sendFrame126("Sigil of Blood", 46022); //Sigil 8
        player.getPA().sendFrame126("--Coming soon", 46023); //Sigil 9
        player.getPA().sendFrame126("Sigil of Pyron", 46024); //Sigil 10
    }

    public static void clearInterface(Player player) {
        player.getPA().showInterface(46000);
        sigilNames(player);
        player.getPA().sendFrame34(25990, 0, 46005, 1);
        player.getPA().sendFrame126("Sigil of Brutality", 46006); //Name of current sigil
        player.getPA().sendFrame126("  Melee attacks will have a 20%", 46008); //Information line 1
        player.getPA().sendFrame126("  chance of 5 damage being", 46009); //Information line 2
        player.getPA().sendFrame126("  added to your hit.", 46010); //Information line 3
        player.getPA().sendFrame126("  Possible chance of receiving", 46012); //Obtaining line 1
        player.getPA().sendFrame126("  from rare raids keys.", 46013); //Obtaining line 2
        player.getPA().sendFrame126("                           ", 46014); //Obtaining line 3
    }

    public static void displayInformation(Player player, int buttonID) {
        switch (buttonID) {
            case 179191: //first sigil
                clearInterface(player);
                break;
            case 179192: //second sigil
                sigilNames(player);
                player.getPA().sendFrame34(26017, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Wealth", 46006); //Name of current sigil
                player.getPA().sendFrame126("  This sigil adds a 3% boost", 46008); //Information line 1
                player.getPA().sendFrame126("  to your drop rate multiplier.", 46009); //Information line 2
                player.getPA().sendFrame126("                             ", 46010); //Information line 3
                player.getPA().sendFrame126("  Purchase from the donator", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  token store.         ", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179193: //third sigil
                sigilNames(player);
                player.getPA().sendFrame34(26041, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Piety", 46006); //Name of current sigil
                player.getPA().sendFrame126("  The rate at which your prayer", 46008); //Information line 1
                player.getPA().sendFrame126("  drains is reduced by 50%.", 46009); //Information line 2
                player.getPA().sendFrame126("                             ", 46010); //Information line 3
                player.getPA().sendFrame126("  Purchase from the tournament", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  manager npc.         ", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179194: //fourth sigil
                sigilNames(player);
                player.getPA().sendFrame34(26065, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Archery", 46006); //Name of current sigil
                player.getPA().sendFrame126("  Range attacks will have a", 46008); //Information line 1
                player.getPA().sendFrame126("  20% chance of 3 damage being", 46009); //Information line 2
                player.getPA().sendFrame126("  added to your hit.", 46010); //Information line 3
                player.getPA().sendFrame126("  Purchase from the slayer", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  point store.         ", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179195: //fifth sigil
                sigilNames(player);
                player.getPA().sendFrame34(26128, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Wizardy", 46006); //Name of current sigil
                player.getPA().sendFrame126("  Magic attacks will have a", 46008); //Information line 1
                player.getPA().sendFrame126("  20% chance of 6 damage being", 46009); //Information line 2
                player.getPA().sendFrame126("  added to your hit.", 46010); //Information line 3
                player.getPA().sendFrame126("  Purchase from the vote", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  point store.         ", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179196: //sixth sigil
                sigilNames(player);
                player.getPA().sendFrame34(26032, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Experience", 46006); //Name of current sigil
                player.getPA().sendFrame126("  All experience gained will", 46008); //Information line 1
                player.getPA().sendFrame126("  be 15% higher.", 46009); //Information line 2
                player.getPA().sendFrame126("                      ", 46010); //Information line 3
                player.getPA().sendFrame126("  Dropped by all of the", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  revenants in the wilderness.", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179197: //sixth sigil
                sigilNames(player);
                player.getPA().sendFrame34(26062, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Treasure", 46006); //Name of current sigil
                player.getPA().sendFrame126("  Odds of receiving a chest key", 46008); //Information line 1
                player.getPA().sendFrame126("  will be 10% higher", 46009); //Information line 2
                player.getPA().sendFrame126("                      ", 46010); //Information line 3
                player.getPA().sendFrame126("  Random chance to receive", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  while skilling.", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179198: //sixth sigil
                sigilNames(player);
                player.getPA().sendFrame34(26125, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Blood", 46006); //Name of current sigil
                player.getPA().sendFrame126("  The blood fury will heal", 46008); //Information line 1
                player.getPA().sendFrame126("  double the amount of HP.", 46009); //Information line 2
                player.getPA().sendFrame126("                      ", 46010); //Information line 3
                player.getPA().sendFrame126("  Obtain from the upgrade", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  craftsman.", 46013); //Obtaining line 2
                player.getPA().sendFrame126("                       ", 46014); //Obtaining line 3
                break;
            case 179200: //last sigil
                sigilNames(player);
                player.getPA().sendFrame34(26146, 0, 46005, 1);
                player.getPA().sendFrame126("Sigil of Pyron", 46006); //Name of current sigil
                player.getPA().sendFrame126("  This sigil has the combined", 46008); //Information line 1
                player.getPA().sendFrame126("  effects of all other sigils.", 46009); //Information line 2
                player.getPA().sendFrame126("                             ", 46010); //Information line 3
                player.getPA().sendFrame126("  Obtained through the upgrade", 46012); //Obtaining line 1
                player.getPA().sendFrame126("  craftsman by combining all", 46013); //Obtaining line 2
                player.getPA().sendFrame126("  of the other sigils.", 46014); //Obtaining line 3
                break;
        }
    }

    @Override
    public void execute(Player player, String commandName, String input) {
        clearInterface(player);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Information about sigils");
    }

}
