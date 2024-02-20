package io.xeros.content.commands.all;

import java.util.Optional;

import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;


public class openthread extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        int articleNumber = Integer.parseInt(input);
        c.getPA().sendFrame126("https://vanguardrsps.com/forums/showthread.php?tid="+ articleNumber, 12000);
        c.sendMessage("Attempting to open forum thread #"+ articleNumber);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens a web page for the chosen forum thread");
    }

}
