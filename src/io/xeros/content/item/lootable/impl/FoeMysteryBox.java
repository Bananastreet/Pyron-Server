package io.xeros.content.item.lootable.impl;

import io.xeros.content.item.lootable.LootRarity;
import io.xeros.content.item.lootable.MysteryBoxLootable;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import QuickUltra.Rarity;

/**
 * Revamped a simple means of receiving a random item based on chance.
 * 
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class FoeMysteryBox extends MysteryBoxLootable {

	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

	/**
	 * Stores an array of items into each map with the corresponding rarity to the list
	 */
	static {
		items.put(LootRarity.COMMON, //50% chance
				Arrays.asList(
					new GameItem(11283),
					new GameItem(19553),
						new GameItem(19550),
						new GameItem(19547),
						new GameItem(19544),
						new GameItem(21034),
						new GameItem(12924),
						new GameItem(11832),
						new GameItem(11834),
						new GameItem(11907),
						new GameItem(23956),
						new GameItem(21006),
						new GameItem(22006),
						new GameItem(22978)



						));
			items.put(LootRarity.UNCOMMON, //50% chance
					Arrays.asList(
							new GameItem(13239),
							new GameItem(13235),
							new GameItem(13237),
							new GameItem(4067, 200),
							new GameItem(11666),
							new GameItem(25859),
							new GameItem(23831),
							new GameItem(23956)
					));

			items.put(LootRarity.RARE,//8% chance
					Arrays.asList(
							//new GameItem(20997),
							//new GameItem(22325),
							new GameItem(22325),
							new GameItem(20997),
							new GameItem(12825),
							new GameItem(25859),
							new GameItem(11862),
							new GameItem(11863),
							new GameItem(24423),
							new GameItem(12817),
							new GameItem(10556),//attacker icon
							new GameItem(10557),//collector icon
							new GameItem(10558),//defender icon
							new GameItem(10559)//Healer icon



							));
		}

    /**
	 * Constructs a new myster box to handle item receiving for this player and this player alone
	 *
	 * @param player the player
	 */
	public FoeMysteryBox(Player player) {
		super(player);
	}

	@Override
	public int getItemId() {
		return 8167;
	}

	@Override
	public Map<LootRarity, List<GameItem>> getLoot() {
		return items;
	}
}