package io.xeros.content.commands.admin;

import io.xeros.content.bosses.wildypursuit.FragmentOfSeren;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

public class fixseren extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        FragmentOfSeren.isActive = false;
        c.sendMessage("Fragment of seren is now set to "+ FragmentOfSeren.isActive);
    }

}
