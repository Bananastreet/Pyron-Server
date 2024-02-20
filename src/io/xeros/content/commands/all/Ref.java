package io.xeros.content.commands.all;

import io.xeros.content.commands.Command;
import io.xeros.content.referral.Referrals;
import io.xeros.model.entity.player.Player;

import java.util.Optional;

public class Ref extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        Referrals.start(c);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens the referral dialogue");
    }

}
