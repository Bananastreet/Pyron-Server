package io.xeros.content.commands.admin;

import io.xeros.content.commands.Command;
import io.xeros.content.wogw.Wogw;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;

public class Wellsink extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        c.getDH().sendStatement("A total of @red@" + Misc.formatCoins(Wogw.getTotalCoinsUsed()) + " @bla@coins have been contributed to the well.");
    }

}
