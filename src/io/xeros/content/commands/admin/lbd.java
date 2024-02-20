package io.xeros.content.commands.admin;

import io.xeros.content.bosses.wildypursuit.FragmentOfSeren;
import io.xeros.content.commands.Command;
import io.xeros.content.leaderboards.LeaderboardInterface;
import io.xeros.content.polls.PollTab;
import io.xeros.content.worldevent.events.monsterhunt.MonsterHunt;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

public class lbd extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        LeaderboardInterface.openInterface(player);
    }
}
