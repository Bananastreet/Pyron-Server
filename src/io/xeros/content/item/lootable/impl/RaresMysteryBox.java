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
public class RaresMysteryBox extends MysteryBoxLootable {

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
                        new GameItem(1038),//normal phat
                        new GameItem(1040),//normal phat
                        new GameItem(1042),//normal phat
                        new GameItem(1044),//normal phat
                        new GameItem(1046),//normal phat
                        new GameItem(1048),//normal phat
                        new GameItem(1050),//santa
                        new GameItem(1053),//normal hween
                        new GameItem(1055),//normal hween
                        new GameItem(1057),//normal hween
                        new GameItem(1037),//bunny ears
                        new GameItem(1419),//scythe
                        new GameItem(26258),//hallowee jumper
                        new GameItem(10507)//reindeer hat

                ));
        items.put(LootRarity.UNCOMMON, //50% chance
                Arrays.asList(
                        new GameItem(1038),//normal phat
                        new GameItem(1040),//normal phat
                        new GameItem(1042),//normal phat
                        new GameItem(1044),//normal phat
                        new GameItem(1046),//normal phat
                        new GameItem(1048),//normal phat
                        new GameItem(1050),//santa
                        new GameItem(1053),//normal hween
                        new GameItem(1055),//normal hween
                        new GameItem(1057),//normal hween
                        new GameItem(1037),//bunny ears
                        new GameItem(1419),//scythe
                        new GameItem(26258),//hallowee jumper
                        new GameItem(10507)//reindeer hat
                ));

        items.put(LootRarity.RARE,//8% chance
                Arrays.asList(
                        new GameItem(6855),//cyan halloween set
                        new GameItem(11862),//black partyhat
                        new GameItem(11863),//rainbow partyhat
                        new GameItem(13344),//inverted santa
                        new GameItem(21859),//cyan halloween set
                        new GameItem(13343),//black santa
                        new GameItem(3881),//lime santa
                        new GameItem(3882),//cyan santa
                        new GameItem(3883),//lava santa
                        new GameItem(11847),//black h'ween
                        new GameItem(4084),//sled
                        new GameItem(5607),//grain
                        new GameItem(4566),//rubber chicken
                        new GameItem(4565),//basket of eggs
                        new GameItem(20836)//giant present
                ));
    }

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public RaresMysteryBox(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 10561;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }
}