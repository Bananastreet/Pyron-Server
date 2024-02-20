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
public class PetMysteryBox extends MysteryBoxLootable {

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
                        new GameItem(12653), //kbd
                        new GameItem(13178), //callisto
                        new GameItem(13179), //vetion
                        new GameItem(13177), //venenatis
                        new GameItem(13247), //cerberus hellpuppy
                        new GameItem(12921), //snakeling
                        new GameItem(13181), //scorpia
                        new GameItem(30020) //corrupt beast



                ));
        items.put(LootRarity.UNCOMMON, //50% chance
                Arrays.asList(
                        new GameItem(26348), //nexling
                        new GameItem(12650), //graardor
                        new GameItem(12649), //kree arra
                        new GameItem(12651), //zilyana
                        new GameItem(12652), //kril
                        new GameItem(12644), //dag prime
                        new GameItem(12645), //dag rex
                        new GameItem(12643), //dag rex
                        new GameItem(11995) //chaos elemental
                ));

        items.put(LootRarity.RARE,//8% chance
                Arrays.asList(
                        new GameItem(30011), //imp
                        new GameItem(30015), //shadow warrior
                        new GameItem(30016), //shadow archer
                        new GameItem(30017), //shadow wizard
                        new GameItem(22473), //lil zik tob pet
                        new GameItem(23939), //seren
                         new GameItem(20851) //olmlet
                ));
    }

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public PetMysteryBox(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 6834;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }
}