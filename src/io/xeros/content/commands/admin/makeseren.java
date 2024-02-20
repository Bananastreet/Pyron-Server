package io.xeros.content.commands.admin;

import io.xeros.content.bosses.wildypursuit.FragmentOfSeren;
import io.xeros.content.commands.Command;
import io.xeros.content.worldevent.events.monsterhunt.MonsterHunt;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

public class makeseren extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        MonsterHunt.Npcs randomNpc = MonsterHunt.Npcs.FRAGMENT_OF_SEREN;
        FragmentOfSeren.currentSeren = NPCSpawning.spawnNpcOld(FragmentOfSeren.NPC_ID, 3163, 5334, 0, 1, 1000, randomNpc.getMaxHit(), randomNpc.getAttack(), randomNpc.getDefence()/*, false*/);

    }
}
