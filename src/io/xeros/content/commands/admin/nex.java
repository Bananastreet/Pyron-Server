package io.xeros.content.commands.admin;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

public class nex extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {

        if (Configuration.nexEnabled) {
            Configuration.nexEnabled = false;
            c.sendMessage("@red@Nex is now disabled.");
        } else {
            Configuration.nexEnabled = true;
            c.sendMessage("@gre@Nex is now enabled.");
        }
    }
}
