package io.xeros.content.worldevent.impl;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import io.xeros.Configuration;
import io.xeros.content.commands.Command;
import io.xeros.content.commands.all.Outlast;
import io.xeros.content.worldevent.events.tournaments.TourneyManager;
import io.xeros.content.worldevent.WorldEvent;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Position;
import io.xeros.model.entity.player.broadcasts.Broadcast;
import io.xeros.sql.DiscordWebhook;

public class TournamentWorldEvent implements WorldEvent {

    private final TourneyManager tourney = TourneyManager.getSingleton();

    @Override
    public void init() {
        tourney.openLobby();
    }

    @Override
    public void dispose() {
        tourney.endGame();
    }

    @Override
    public boolean isEventCompleted() {
        return !tourney.isLobbyOpen() && !tourney.isArenaActive();
    }

    @Override
    public String getCurrentStatus() {
        return tourney.getTimeLeft();
    }

    @Override
    public String getEventName() {
        return "Outlast";
    }

    @Override
    public String getStartDescription() {
        return "starts";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() {
        return Outlast.class;
    }

    @Override
    public void announce(List<Player> players) {
        new Broadcast("An Outlast " + tourney.getTournamentType() + " tournament will begin soon, type ::outlast or click HERE to join!").addTeleport(new Position(3112, 3508, 0)).submit();
        PlayerHandler.newsMessage("An Outlast " + tourney.getTournamentType() + " tournament will begin soon, type ::outlast to join!");
        DiscordWebhook webhook = new DiscordWebhook(Configuration.EventsWebhook);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(""+tourney.getTournamentType() + " Tournament")
                .setDescription("A tournament will begin soon, enter the portal at ::outlast to join")
                .setColor(Color.RED));
        try {
            if (Configuration.DiscordEnabled)
                webhook.execute(); //Handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
