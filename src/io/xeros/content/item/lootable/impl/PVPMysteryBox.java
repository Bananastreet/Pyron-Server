package io.xeros.content.item.lootable.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xeros.content.item.lootable.LootRarity;
import io.xeros.content.item.lootable.MysteryBoxLootable;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;

//import QuickUltra.Rarity;

/**
 * Revamped a simple means of receiving a random item based on chance.
 *
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class PVPMysteryBox extends MysteryBoxLootable {

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
                        new GameItem(22625),//statius helm
                        new GameItem(22628),//statius body
                        new GameItem(22631),//statius legs
                        new GameItem(22647),//zuriel staff
                        new GameItem(22650),//zuriel hood
                        new GameItem(22653),//zuriel top
                        new GameItem(22656),//zuriel bottoms
                        new GameItem(22638),//morrigans coif
                        new GameItem(22641),//morrigans body
                        new GameItem(22644)//morrigans chaps

        ));
        items.put(LootRarity.UNCOMMON, //50% chance
                Arrays.asList(
                        new GameItem(22625),//statius helm
                        new GameItem(22628),//statius body
                        new GameItem(22631),//statius legs
                        new GameItem(22647),//zuriel staff
                        new GameItem(22650),//zuriel hood
                        new GameItem(22653),//zuriel top
                        new GameItem(22656),//zuriel bottoms
                        new GameItem(22638),//morrigans coif
                        new GameItem(22641),//morrigans body
                        new GameItem(22644)//morrigans chaps
        ));

        items.put(LootRarity.RARE,//8% chance
                Arrays.asList(
                        new GameItem(22622),//swh
                        new GameItem(22616),//vesta chain
                        new GameItem(22619),//vesta legs
                        new GameItem(22610),//vesta spear
                        new GameItem(22613)//vls
                ));
    }

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public PVPMysteryBox(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 10560;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }
}