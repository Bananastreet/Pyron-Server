package io.xeros.content.commands.admin;

import io.xeros.content.commands.Command;
import io.xeros.content.referral.Referrals;
import io.xeros.model.entity.player.Player;

public class Topref extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (input.isEmpty()) {
            c.sendMessage(Referrals.getTopReferral());
            return;
        }
        String[] args = input.split(" ");
        Referrals.topRefs(c, Integer.parseInt(args[0]));

    }

}
