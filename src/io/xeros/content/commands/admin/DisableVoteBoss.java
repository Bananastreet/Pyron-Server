package io.xeros.content.commands.admin;

import io.xeros.content.bosses.VoteBoss;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

public class DisableVoteBoss extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (VoteBoss.voteBossEnabled == true) {
            VoteBoss.voteBossEnabled = false;
            c.sendMessage("You have disabled the vote boss.");
        } else {
            VoteBoss.voteBossEnabled = true;
            c.sendMessage("You have enabled the vote boss.");
        }
    }
}
