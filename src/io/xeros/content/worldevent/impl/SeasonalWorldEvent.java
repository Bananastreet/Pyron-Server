package io.xeros.content.worldevent.impl;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.content.commands.all.Star;
import io.xeros.content.worldevent.WorldEvent;
import io.xeros.content.worldevent.events.ShootingStar;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.broadcasts.Broadcast;
import io.xeros.sql.DiscordWebhook;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SeasonalWorldEvent implements WorldEvent {

    public static boolean eventActive = false;

    @Override
    public void init() {
        eventActive = true;
    }

    @Override
    public void dispose() {
        if (!isEventCompleted()) {
            eventActive = false;
        }
    }

    @Override
    public boolean isEventCompleted() {
        return !eventActive;
    }

    @Override
    public String getCurrentStatus() {
        return "World Event: @gre@::seasonal";
    }

    @Override
    public String getEventName() {
        return "Seasonal Mass";
    }

    @Override
    public String getStartDescription() {
        return "started";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() {
        return Star.class;
    }

    @Override
    public void announce(List<Player> players) {
        new Broadcast("Go to ::seasonal for more seasonal monsters with faster respawns.").submit();
        DiscordWebhook webhook = new DiscordWebhook(Configuration.EventsWebhook);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Seasonal Mass")
                .setDescription("A seasonal mass has started, join in at ::seasonal.")
                .setColor(Color.RED));
        try {
            if (Configuration.DiscordEnabled)
                webhook.execute(); //Handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
