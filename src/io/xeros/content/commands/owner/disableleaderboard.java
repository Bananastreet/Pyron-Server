package io.xeros.content.commands.owner;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.player.Player;

/**
 * Send the item IDs of all matching items to the player.
 *
 * @author Emiel
 *
 */
public class disableleaderboard extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (Configuration.leaderboardEnabled) {
            Configuration.leaderboardEnabled = false;
        } else {
            Configuration.leaderboardEnabled = true;
        }
        c.sendMessage("Leaderboard is currently set to: "+Configuration.leaderboardEnabled);
    }
}
