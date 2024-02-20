package io.xeros.content.commands.owner;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

public class afk extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (Configuration.AFK) {
            Configuration.AFK = false;
            c.sendMessage("afk status now set to " +Configuration.AFK);
        } else {
            Configuration.AFK = true;
            c.sendMessage("afk status now set to " +Configuration.AFK);
        }
    }

}
