package io.xeros.content.commands.owner;

import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.WorldEventContainer;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.ItemAssistant;

public class tiers extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        player.getPA().showInterface(45291);
        player.getPA().sendFrame126("Tokens: "+ player.getItems().getInventoryCount(23497), 45146);
    }
}
