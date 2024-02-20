package io.xeros.content.combat.stats;

import java.util.HashMap;
import java.util.Map;

import io.xeros.Configuration;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardManager;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.content.leaderboard.impl.Boss;
import io.xeros.content.leaderboard.impl.Boxes;
import io.xeros.content.leaderboard.impl.Raid;
import io.xeros.content.minigames.tob.TobConstants;
import io.xeros.model.Npcs;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class NPCDeathTracker {

	/**
	 * The player this is relative to
	 */
	private final Player player;

	/**
	 * A mapping of npcs names with their corresponding kill count
	 */
	private Map<String, Integer> tracker = new HashMap<>();

	/**
	 * Creates a new {@link NPCDeathTracker} object for a singular player
	 *
	 * @param player
	 *            the player
	 */
	public NPCDeathTracker(Player player) {
		this.player = player;
	}

	/**
	 * Made a mistake with kc, this will fix it.
	 * Goes through every npc death tracker and makes the name lowercase.
	 */
	public void normalise() {
		Map<String, Integer> newTracker = new HashMap<>();
		Map<String, Integer> oldTracker = tracker;
		tracker = newTracker;

		for (Map.Entry<String, Integer> entry : oldTracker.entrySet()) {
			String key = entry.getKey().toLowerCase();
			if (newTracker.containsKey(key)) {
				newTracker.put(key, newTracker.get(key) + entry.getValue());
			} else {
				newTracker.put(key, entry.getValue());
			}
		}

		// Fixes an issue with the old Kree'arra npc definition name being different from the current one
		tracker.put(NpcDef.forId(Npcs.KREEARRA).getName().toLowerCase(), tracker.getOrDefault("kree arra", 0)
				+ tracker.getOrDefault("kree'arra", 0));
		tracker.remove("kree arra"); // Remove the old one or we'll have ever-expanding kc!
	}

	public int getKc(String name) {
		name = name.toLowerCase();

		if (name.equalsIgnoreCase(TobConstants.THEATRE_OF_BLOOD)) {
			return player.tobCompletions;
		}

		if (name.equalsIgnoreCase("Chambers of Xeric") || name.equalsIgnoreCase("cox")) {
			return player.raidCount;
		}

		return tracker.getOrDefault(name, 0);
	}


	/**
	 * Attempts to add a kill to the total amount of kill for a single npc
	 *
	 * @param name
	 *            the name of the npc
	 */
	public void add(String name, int combatLevel, int bossPoints) {
		if (name == null) {
			return;
		} else {
			name = name.toLowerCase();
			int kills = (tracker.get(name) == null ? 0 : tracker.get(name)) + 1;
			String killsStr = Integer.toString(kills);
			tracker.put(name, kills);
			if (name.equalsIgnoreCase("none")) {
				return;
			}

			if (name.contains("clue-scroll")) {
				return;
			}

			if (name.equalsIgnoreCase("abomination")) {
				displayKcMessage(name, kills, bossPoints);
				return;
			}

			if (Boss.Companion.getBoss(name) != null && Configuration.leaderboardEnabled) {
				LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.Companion.getBoss(name));
			}

		/*if (name.toLowerCase().contains("maiden")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.RAIDS, kills, Raid.THEATRE_OF_BLOOD);
		}

		if (name.equalsIgnoreCase("nex")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.NEX);
		}

		if (name.equalsIgnoreCase("avatar of destruction")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.AVATAR_OF_DESTRUCTION);
		}

		if (name.equalsIgnoreCase("zulrah")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.ZULRAH);
		}

		if (name.equalsIgnoreCase("kraken")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.KRAKEN);
		}

		if (name.equalsIgnoreCase("fragment of seren")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.SEREN);
		}

		if (name.equalsIgnoreCase("bryophyta")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.BRYOPHYTA);
		}

		if (name.equalsIgnoreCase("hunllef")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.HUNLLEF);
		}

		if (name.equalsIgnoreCase("obor")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.OBOR);
		}

		if (name.equalsIgnoreCase("dagannoth prime")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.DAGANNOTH_PRIME);
		}

		if (name.equalsIgnoreCase("dagannoth rex")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.DAGANNOTH_REX);
		}

		if (name.equalsIgnoreCase("dagannoth supreme")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.DAGANNOTH_SUPREME);
		}

		if (name.equalsIgnoreCase("giant mole")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.GIANT_MOLE);
		}

		if (name.equalsIgnoreCase("kalphite queen")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.KALPHITE_QUEEN);
		}

		if (name.equalsIgnoreCase("lizardman shaman")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.LIZARDMAN_SHAMAN);
		}

		if (name.equalsIgnoreCase("sarachnis")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.SARACHNIS);
		}

		if (name.equalsIgnoreCase("thermonuclear smoke devil")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.THERMY);
		}

		if (name.equalsIgnoreCase("demonic gorilla")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.DEMONIC_GORILLAS);
		}

		if (name.equalsIgnoreCase("commander zilyana")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.SARA_GWD);
		}

		if (name.toLowerCase().contains("tsutsaroth")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.ZAMMY_GWD);
		}

		if (name.equalsIgnoreCase("general graardor")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.BANDOS_GWD);
		}

		if (name.equalsIgnoreCase("kree'arra")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.ARMA_GWD);
		}

		if (name.equalsIgnoreCase("corporeal beast")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CORP);
		}

		if (name.equalsIgnoreCase("cerberus")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CERBERUS);
		}

		if (name.equalsIgnoreCase("abyssal sire")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.ABYSSAL_SIRE);
		}

		if (name.equalsIgnoreCase("vorkath")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.VORKATH);
		}

		if (name.equalsIgnoreCase("alchemical hydra")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.HYDRA);
		}

		if (name.toLowerCase().contains("nightmare")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.NIGHTMARE);
		}

		if (name.toLowerCase().contains("brassican")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.BRASSICAN);
		}

		if (name.equalsIgnoreCase("king black dragon")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.KBD);
		}

		if (name.equalsIgnoreCase("vet'ion")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.VETION);
		}

		if (name.equalsIgnoreCase("callisto")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CALLISTO);
		}

		if (name.equalsIgnoreCase("scorpia")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.SCORPIA);
		}

		if (name.equalsIgnoreCase("venenatis")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.VENENATIS);
		}

		if (name.equalsIgnoreCase("chaos elemental")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CHAOS_ELEMENTAL);
		}

		if (name.equalsIgnoreCase("chaos fanatic")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CHAOS_FANATIC);
		}

		if (name.equalsIgnoreCase("crazy archeologist")) {
			LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.BOSSES, kills, Boss.CRAZY_ARCHEOLOGIST);
		}

		 */

		if (killsStr.endsWith("00") && combatLevel <= 150) {//every 100
			displayKcMessage(name, kills, bossPoints);
		} else if (killsStr.endsWith("0") && combatLevel <= 250) {//every 10
			displayKcMessage(name, kills, bossPoints);
		} else if (combatLevel > 250) {
			displayKcMessage(name, kills, bossPoints);
		}
	}
}

	public void displayKcMessage(String name, int kills, int bossPoints) {
		StringBuilder builder = new StringBuilder();
		String formatted = name.replaceAll("_", " ");
		builder.append("Your " + WordUtils.capitalize(formatted)
				+ " kill count is: <col=FF0000>" + kills + "</col>");

		if (bossPoints > 0) {
			builder.append(", +<col=FF0000>" + bossPoints + "</col> points.");
		} else {
			builder.append(".");
		}

		player.sendMessage(builder.toString());
	}

	/**
	 * Determines the total amount of kills
	 *
	 * @return the total kill count
	 */
	public long getTotal() {
		return tracker.values().stream().mapToLong(Integer::intValue).sum();
	}

	/**
	 * A mapping of npcs with their corresponding kill count
	 *
	 * @return the map
	 */
	public Map<String, Integer> getTracker() {
		return tracker;
	}
}
