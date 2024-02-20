package io.xeros.content.commands.owner;

import io.xeros.content.bosses.Abomination;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;
import io.xeros.net.login.RS2LoginProtocol;
import io.xeros.sql.etc.DonationHandler;

public class addten extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        DonationHandler.totalCents += 1000;
        player.sendMessage("The total is now at: $"+ Abomination.amountRemaining());
    }
}
