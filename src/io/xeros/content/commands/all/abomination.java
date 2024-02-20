package io.xeros.content.commands.all;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import io.xeros.Configuration;
import io.xeros.content.bosses.Abomination;
import io.xeros.content.bosses.VoteBoss;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;
import io.xeros.sql.DiscordWebhook;

public class abomination extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (c.getPosition().inWild()) {
            c.sendMessage("You can only use this command outside the wilderness.");
            return;
        }
        if (!Abomination.abominationEnabled) {
            c.sendMessage("The abomination is currently disabled.");
            return;
        }
        Abomination.teleportToBoss(c);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Takes you to the donator boss, abomination.");
    }

}
