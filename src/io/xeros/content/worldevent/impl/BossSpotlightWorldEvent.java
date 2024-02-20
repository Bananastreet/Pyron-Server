package io.xeros.content.worldevent.impl;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.content.commands.all.Spotlight;
import io.xeros.content.worldevent.WorldEvent;
import io.xeros.content.worldevent.events.BossSpotlight;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.broadcasts.Broadcast;
import io.xeros.sql.DiscordWebhook;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class BossSpotlightWorldEvent implements WorldEvent {

    @Override
    public void init() {
        BossSpotlight.start();
    }

    @Override
    public void dispose() {
        if (!isEventCompleted()) {
            BossSpotlight.end();
        }
    }

    @Override
    public boolean isEventCompleted() {
        return !BossSpotlight.active;
    }

    @Override
    public String getCurrentStatus() {
        return "World Event: @gre@Boss Spotlight";
    }

    @Override
    public String getEventName() {
        return "Boss Spotlight";
    }

    @Override
    public String getStartDescription() {
        return "starts";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() { return Spotlight.class; }

    @Override
    public void announce(List<Player> players) {
        new Broadcast(BossSpotlight.selectedBoss.getNpcName() + " now on spotlight, kill for 5% boosted drop rate!").submit();
        DiscordWebhook webhook = new DiscordWebhook(Configuration.EventsWebhook);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(BossSpotlight.selectedBoss.getNpcName() + " Spotlight")
                .setDescription(BossSpotlight.selectedBoss.getNpcName() + " now on spotlight with a 5% boosted drop rate.")
                .setColor(Color.RED));
        try {
            if (Configuration.DiscordEnabled)
                webhook.execute(); //Handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
