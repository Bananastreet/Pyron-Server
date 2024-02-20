package io.xeros.content.commands.all;

import java.text.DecimalFormat;
import java.util.Optional;

import io.xeros.content.combat.core.HitDispatcher;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;

/**
 * Teleport the player to the mage bank.
 *
 * @author Emiel
 */
public class Crystal extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		DecimalFormat df = new DecimalFormat("#.##");
		c.sendMessage("Your crystal damage boost is currently " + df.format(HitDispatcher.crystalReturnDamage(c)) + "x.");
		c.sendMessage("Your crystal accuracy boost is currently " + df.format(HitDispatcher.crystalReturnAccuracy(c)) + "x.");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Tells you your crystal damage and accuracy boosts.");
	}

}
