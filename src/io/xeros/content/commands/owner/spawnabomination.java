package io.xeros.content.commands.owner;

import io.xeros.content.bosses.Abomination;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

/**
 * Open a specific shop.
 *
 * @author Emiel
 *
 */
public class spawnabomination extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        Abomination.spawnBoss();
    }
}
