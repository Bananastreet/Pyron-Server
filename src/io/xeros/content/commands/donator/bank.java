package io.xeros.content.commands.donator;

import java.util.Optional;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Right;
import io.xeros.model.entity.player.mode.ModeType;

public class bank extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (c.inTrade || c.inDuel || c.getPosition().inWild()) {
            return;
        }
        if (c.getMode().getType() == ModeType.ULTIMATE_IRON_MAN) {
            c.sendMessage("Ultimate ironmen can not access banking");
            return;
        }
        if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
            c.sendMessage("@cr10@This player is currently at the pk district.");
            return;
        }
        if (Boundary.isIn(c, Boundary.OUTLAST) || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_AREA)) {
            c.sendMessage("You can not use this command in outlast");
            return;
        }
        if (c.tokensSpent < 5000 && !c.getRights().isOrInherits(Right.ADMINISTRATOR)) {
            c.sendMessage("@red@You need diamond donator to do this command");
            return;
        }
        c.getPA().c.itemAssistant.openUpBank();
        c.inBank = true;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens your bank");
    }

}


