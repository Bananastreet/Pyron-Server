package io.xeros.content.worldevent.impl;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.content.commands.all.Star;
import io.xeros.content.worldevent.WorldEvent;
import io.xeros.content.worldevent.events.BossSpotlight;
import io.xeros.content.worldevent.events.ShootingStar;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.broadcasts.Broadcast;
import io.xeros.sql.DiscordWebhook;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ShootingStarWorldEvent implements WorldEvent {

    @Override
    public void init() {
        ShootingStar.startShootingStar();
    }

    @Override
    public void dispose() {
        if (!isEventCompleted()) {
            ShootingStar.endShootingStar();
        }
    }

    @Override
    public boolean isEventCompleted() {
        return !ShootingStar.active;
    }

    @Override
    public String getCurrentStatus() {
        return "World Event: @gre@Crashed Star";
    }

    @Override
    public String getEventName() {
        return "Shooting Star";
    }

    @Override
    public String getStartDescription() {
        return "crashes";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() {
        return Star.class;
    }

    @Override
    public void announce(List<Player> players) {
        new Broadcast("A shooting star has crashed, use ::star to mine the remnants!").submit();
        DiscordWebhook webhook = new DiscordWebhook(Configuration.EventsWebhook);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Shooting Star")
                .setDescription("A shooting star has crashed, use ::star to mine the remnants.")
                .setColor(Color.RED));
        try {
            if (Configuration.DiscordEnabled)
                webhook.execute(); //Handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
