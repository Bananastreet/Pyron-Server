package io.xeros.content.bosses.wildypursuit;

import io.xeros.Server;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCHandler;
import io.xeros.model.entity.npc.NPCSpawning;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.PathFinder;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Location3D;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/18/19
 * Keep in mind this is built ontop a poor system and architecture. Did the best with what I was given.
 */
public class FragmentOfSeren {

	public static final int KEY = 6792;

	/**
	 * Variables
	 */
	public static final int FRAGMENT_ID = 8920;
	public static final int NPC_ID = 8918;
	public static final int CRYSTAL_WHIRLWIND = 8921;

	//Can Seren be attacked or not
	public static boolean isAttackable = true;
	
	public static final int SPAWN_ANIMATION = 8370;
	public static final int TOUCHED_ANIMATED = 8371;
	public static final int DEATH_ANIMATION = 8373;
	public static final int SPAWN_MINIONS_ANIMATION = 8380;
	public static final int MULTI_ATTACK_ANIMATION = 8376;
	public static final int CRYSTAL_DEATH_ANIMATION = 8424;
	
	//Three active crystals spawned in to give health
	public static final ArrayList<NPC> activePillars = new ArrayList<>();
	
	public static int specialAmount;

	public static boolean isActive = false;
	
	public static NPC currentSeren;
	
	/**
	 * Handles Seren spawning in the crystals
	 * @param player
	 */
	public static void handleSpecialAttack(Player player) {
		int[][] coords = {{2, 0}, {-2, 0}, {0, 2}};
		
		if (currentSeren.isDead()) {
			return;
		}
		
		if (currentSeren.getHealth().getCurrentHealth() <= 600 && specialAmount == 0) {
			currentSeren.forceChat("I will ruin you all!");
			currentSeren.startAnimation(8380);
			currentSeren.underAttackBy = -1;
			currentSeren.underAttack = false;
			isAttackable = false;
			NPCHandler.queenAttack = "SPECIAL";
			specialAmount++;
			activePillars.clear();
			for(int i = 0; i < 3; i++) {
				if (!PathFinder.getPathFinder().accessable(player, currentSeren.getX() + coords[i][0], currentSeren.getY() + coords[i][1])) {
					activePillars.add(NPCSpawning.spawnNpcOld(CRYSTAL_WHIRLWIND, currentSeren.getX(), currentSeren.getY(), currentSeren.getHeight(), 0, 200, 0, 0, 0));
				} else {
					activePillars.add(NPCSpawning.spawnNpcOld(CRYSTAL_WHIRLWIND, currentSeren.getX() + coords[i][0], currentSeren.getY() + coords[i][1], currentSeren.getHeight(), 0, 200, 0, 0, 0));
				}
			}
			createWhirlWindEvent();
		}
	}
	
	/**
	 * Handles the on-going event of healing Seren until all crystals are dead
	 */
	public static void createWhirlWindEvent() {
		CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				NPC seren = NPCHandler.getNpc(NPC_ID);

				boolean gaveHealth = false;
				for(NPC pillar : activePillars) {
					if (pillar != null && !pillar.isDead() && pillar.getHealth().getCurrentHealth() > 0) {
						if (seren != null) {
							gaveHealth = true;
							seren.getHealth().increase(10);
						}
					}
				}

				if (gaveHealth) {
					seren.forceChat("Yes... feed me!");
				} else {
					isAttackable = true;
					container.stop();
				}
			}
			
		}, Misc.toCycles(10, TimeUnit.SECONDS));
	}
	
	/**
	 * This was here before, but assuming rewards all fighters within the area
	 */
	public static void rewardPlayers(NPC npc) {
		isActive = false;
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.SEREN_ROOM))
		.forEach(p -> {
				if (p.getIceQueenDamageCounter() >= 80 && p.getIceQueenDamageCounter() <= 350) {
					Server.getDropManager().create(p, npc, new Location3D(p.getX(), p.getY(), p.getHeight()), 1, NPC_ID);
					p.sendMessage("You received one drop from doing "+ p.getIceQueenDamageCounter() + " damage.");
					p.getNpcDeathTracker().add(NpcDef.forId(NPC_ID).getName(), NpcDef.forId(NPC_ID).getCombatLevel(), 0);
					p.setIceQueenDamageCounter(0);
				} else if (p.getIceQueenDamageCounter() > 400) {
					Server.getDropManager().create(p, npc, new Location3D(p.getX(), p.getY(), p.getHeight()), 1, NPC_ID);
					Server.getDropManager().create(p, npc, new Location3D(p.getX(), p.getY(), p.getHeight()), 1, NPC_ID);
					p.getNpcDeathTracker().add(NpcDef.forId(NPC_ID).getName(), NpcDef.forId(NPC_ID).getCombatLevel(), 0);
					p.sendMessage("You received two drops from doing "+ p.getIceQueenDamageCounter() + " damage.");
					p.setIceQueenDamageCounter(0);
				} else {
					p.sendMessage("@blu@You didn't do enough damage to the boss to receive a reward.");
					p.setIceQueenDamageCounter(0);
				}

		});
	}
	
	public static void removePillar(NPC npc) {
		int removeIndex = -1;
		for(int i = 0; i < activePillars.size(); i++) {
			if (activePillars.get(i).absX == npc.absX && activePillars.get(i).absY == npc.absY) {
				removeIndex = i;
				break;
			}
		}
		if (removeIndex != -1) {
			activePillars.remove(removeIndex);
		}
	}
}
