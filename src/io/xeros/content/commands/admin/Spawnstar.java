package io.xeros.content.commands.admin;

import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.WorldEventContainer;
import io.xeros.content.worldevent.impl.ShootingStarWorldEvent;
import io.xeros.model.entity.player.Player;

public class Spawnstar extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        WorldEventContainer.getInstance().startEvent(new ShootingStarWorldEvent());
    }

}
