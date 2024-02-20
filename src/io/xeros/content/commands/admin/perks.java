package io.xeros.content.commands.admin;

import io.xeros.content.commands.Command;
import io.xeros.content.perks.PerkHandler;
import io.xeros.content.worldevent.WorldEventContainer;
import io.xeros.content.worldevent.impl.ShootingStarWorldEvent;
import io.xeros.model.entity.player.Player;

public class perks extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        c.getPerks().displayInterface();
    }

}
