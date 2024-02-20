package io.xeros.content.commands.all;

import io.xeros.content.bosschallenges.ChallengeHandler;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

import java.util.Optional;

public class bc extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        if (player.bc1 != 0)
        ChallengeHandler.inform(player);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Displays your current boss challenge.");
    }

}
