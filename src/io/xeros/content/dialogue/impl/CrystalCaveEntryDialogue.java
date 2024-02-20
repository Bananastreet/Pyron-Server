package io.xeros.content.dialogue.impl;

import io.xeros.content.bosses.Hunllef;
import io.xeros.content.dialogue.DialogueBuilder;
import io.xeros.content.dialogue.DialogueOption;
import io.xeros.model.entity.player.Player;

public class CrystalCaveEntryDialogue extends DialogueBuilder {


    public CrystalCaveEntryDialogue(Player player) {
        super(player);
        setNpcId(8761);
                    npc("Which cave would you like to go to?")
                    .option(new DialogueOption("Regular", p -> enterCave(player, 0, false)),
                            new DialogueOption("No-Cannon", p -> enterCave(player, 4, false)),
                            new DialogueOption("Premium (Cost: 3m Coins)", p -> enterCave(player, 0, true)));

    }

    private void enterCave(Player c, int floor, boolean fee) {
        c.getPA().closeAllWindows();
        if ((!c.getSlayer().getTask().isPresent() || !c.getSlayer().getTask().get().getPrimaryName().contains("crystalline")) && !c.getItems().playerHasItem(23951)) {
          // c.sendMessage("@red@You must have a crystalline task to go in this cave.");
            //return;
        }
        if (fee) {
            if (!c.getItems().playerHasItem(995, 3000000)) {
                c.sendMessage("@red@You need 3m coins.");
                return;
            }
            c.getItems().deleteItem2(995, 3000000);
            floor = 8;
        }
        c.objectDistance = 5;
        c.getPA().movePlayer(3225, 12445, floor);
    }
}
