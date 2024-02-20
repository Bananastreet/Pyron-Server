package io.xeros.content.commands.admin;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

/**
 * Open the banking interface.
 *
 * @author Emiel
 */
public class toggleclanlogs extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (Configuration.ccLogsEnabled) {
            Configuration.ccLogsEnabled = false;
            c.sendMessage("CC Logs is now set to: " + Configuration.ccLogsEnabled);
        } else {
            Configuration.ccLogsEnabled = true;
            c.sendMessage("CC Logs is now set to: " + Configuration.ccLogsEnabled);
        }
    }
}
