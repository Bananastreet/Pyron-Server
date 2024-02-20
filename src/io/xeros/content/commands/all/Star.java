package io.xeros.content.commands.all;

import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.events.ShootingStar;
import io.xeros.model.entity.player.Player;

import java.util.Optional;

public class Star extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        if (ShootingStar.active) {
            player.getPA().spellTeleport(ShootingStar.currentStarX - 3, ShootingStar.currentStarY - 3, 0, false);
        } else {
            player.sendMessage("There is currently no crashed stars.");
        }
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Teleports to the crashed star.");
    }
}
