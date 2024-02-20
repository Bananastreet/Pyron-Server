package io.xeros.content.commands.helper;

import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;

/**
 * Teleport the player to the moon custom zone.
 *
 * @author Emiel
 */
public class moonzone extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
            c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
            return;
        }
        if (Boundary.isIn(c, Boundary.OUTLAST_AREA)) {
            return;
        }
        if (!c.getDisplayName().toLowerCase().contains("grim") && !c.getDisplayName().toLowerCase().contains("haku")) {
            c.sendMessage("You do not have access to this zone.");
            return;
        }
        c.getPA().startTeleport(3057, 5480, 0, "modern", false);
    }
}
