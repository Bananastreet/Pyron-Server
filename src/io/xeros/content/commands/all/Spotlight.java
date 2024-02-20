package io.xeros.content.commands.all;

import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.events.BossSpotlight;
import io.xeros.content.worldevent.events.ShootingStar;
import io.xeros.model.entity.player.Player;

import java.util.Optional;

public class Spotlight extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        if (BossSpotlight.active) {
            player.sendMessage("@blu@{} currently on spotlight.", BossSpotlight.selectedBoss.getNpcName());
        } else {
            player.sendMessage("There are no bosses currently on spotlight.");
        }
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Find out which boss is currently on spotlight.");
    }

}
