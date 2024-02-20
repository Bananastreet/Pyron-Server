package io.xeros.content.commands.admin;

import io.xeros.content.commands.Command;
import io.xeros.content.referral.Referrals;
import io.xeros.model.entity.player.Player;

public class Checkref extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        c.sendMessage(Referrals.checkReferral(input));
    }

}
