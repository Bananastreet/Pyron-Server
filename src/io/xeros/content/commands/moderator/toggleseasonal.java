package io.xeros.content.commands.moderator;

import java.awt.*;
import java.io.IOException;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.sql.DiscordWebhook;

/**
 * Activates the seasonal mass
 *
 * @author Bubly
 */
public class toggleseasonal extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (Configuration.seasonalMass) {
            Configuration.seasonalMass = false;
            c.sendMessage("The seasonal mass has been set to inactive.");
            PlayerHandler.newsMessage("The seasonal mass is no longer active.");
        } else {
            Configuration.seasonalMass = true;
            c.sendMessage("The seasonal mass has been set to active.");
            PlayerHandler.newsMessage("The seasonal mass area is now active, ::seasonal");
            DiscordWebhook webhook = new DiscordWebhook(Configuration.bossSpawns);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("Seasonal Mass")
                    .setDescription("The seasonal mass area is now active, ::seasonal")
                    .setColor(Color.RED));
            try {
                if (Configuration.DiscordEnabled)
                    webhook.execute(); //Handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
