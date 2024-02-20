package io.xeros.content.item.lootable.impl;

import io.xeros.content.item.lootable.LootRarity;
import io.xeros.content.item.lootable.MysteryBoxLootable;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Revamped a simple means of receiving a random item based on chance.
 *
 * @author  Bubly
 * @date September 19th, 2023
 */
public class GodwarsMysteryBox extends MysteryBoxLootable {

    /**
     * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
     */
    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    /**
     * Stores an array of items into each map with the corresponding rarity to the list
     */
    static {
        items.put(LootRarity.COMMON,
                Arrays.asList(
                        new GameItem(11804), //bgs
                        new GameItem(11832), //bcp
                        new GameItem(11834), //bandos tassets
                        new GameItem(11802), //ags
                        new GameItem(11785), //armadyl crossbow
                        new GameItem(11826), //armadyl helm
                        new GameItem(11828), //armadyl chestplate
                        new GameItem(11830), //armadyl chainskirt
                        new GameItem(11824), //z spear
                        new GameItem(11806), //sgs
                        new GameItem(11791) //staff of the dead



                ));
        items.put(LootRarity.UNCOMMON,
                Arrays.asList(
                        new GameItem(11804), //bgs
                        new GameItem(11832), //bcp
                        new GameItem(11834), //bandos tassets
                        new GameItem(11802), //ags
                        new GameItem(11785), //armadyl crossbow
                        new GameItem(11826), //armadyl helm
                        new GameItem(11828), //armadyl chestplate
                        new GameItem(11830), //armadyl chainskirt
                        new GameItem(11824), //z spear
                        new GameItem(11806), //sgs
                        new GameItem(11791) //staff of the dead
                ));

        items.put(LootRarity.RARE,//8% chance
                Arrays.asList(
                        new GameItem(26235), //zaryte vambraces
                        new GameItem(26376), //torva full helm
                        new GameItem(26378), //torva body
                        new GameItem(26380) //torva legs
                ));
    }

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public GodwarsMysteryBox(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 10563;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }
}