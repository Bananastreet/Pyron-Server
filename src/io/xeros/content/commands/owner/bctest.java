package io.xeros.content.commands.owner;

import io.xeros.content.bosschallenges.ChallengeHandler;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

public class bctest extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        ChallengeHandler.complete(player);
    }

}
