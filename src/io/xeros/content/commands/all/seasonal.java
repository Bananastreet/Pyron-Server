package io.xeros.content.commands.all;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import io.xeros.Configuration;
import io.xeros.content.bosses.Abomination;
import io.xeros.content.bosses.VoteBoss;
import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.impl.SeasonalMass;
import io.xeros.content.worldevent.impl.SeasonalWorldEvent;
import io.xeros.model.entity.player.Player;
import io.xeros.sql.DiscordWebhook;

public class seasonal extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (c.getPosition().inWild()) {
            c.sendMessage("You can only use this command outside the wilderness.");
            return;
        }
        if (!Configuration.seasonalMass) {
            c.sendMessage("There is not currently a seasonal mass active.");
            return;
        }
        c.getPA().spellTeleport(2468, 9435, 0, false);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Takes you to the seasonal event mass cave.");
    }

}
