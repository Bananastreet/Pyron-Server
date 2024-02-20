package io.xeros.content.commands.admin;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

/**
 * Open the banking interface.
 *
 * @author Emiel
 */
public class togglediscord extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (Configuration.DiscordEnabled) {
            Configuration.DiscordEnabled = false;
            c.sendMessage("Discord is now set to: " + Configuration.DiscordEnabled);
        } else {
            Configuration.DiscordEnabled = true;
            c.sendMessage("Discord is now set to: " + Configuration.DiscordEnabled);
        }
    }
}
