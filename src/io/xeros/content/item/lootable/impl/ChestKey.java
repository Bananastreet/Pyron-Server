package io.xeros.content.item.lootable.impl;

import io.xeros.content.item.lootable.LootRarity;
import io.xeros.content.item.lootable.Lootable;
import io.xeros.model.Items;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestKey implements Lootable {

    /**
     * THIS IS ONLY USED TO DISPLAY LOOT ON THE INTERFACE
     * TO ADD LOOT, SEE {@link io.xeros.content.chestkeys.ChestKeyLooting}
     * IF YOU ADD TO ChestKeyLooting.java, ADD TO THIS SO IT SHOWS ON INTERFACE
     */

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(Items.COAL_NOTED, 50),
                new GameItem(Items.RUNITE_ORE_NOTED, 15),
                new GameItem(Items.ADAMANTITE_ORE_NOTED, 23),
                new GameItem(Items.MITHRIL_ORE_NOTED, 30),
                new GameItem(Items.STEEL_BAR_NOTED, 30),
                new GameItem(Items.MITHRIL_BAR_NOTED, 15),
                new GameItem(Items.GRIMY_TORSTOL_NOTED, 13),
                new GameItem(Items.GRIMY_SNAPDRAGON_NOTED, 12),
                new GameItem(Items.GRIMY_RANARR_WEED_NOTED, 12),
                new GameItem(Items.GRIMY_CADANTINE_NOTED, 12),
                new GameItem(Items.UNCUT_DRAGONSTONE_NOTED, 30),
                new GameItem(Items.UNCUT_DIAMOND_NOTED, 70),
                new GameItem(Items.YEW_LOGS_NOTED, 30),
                new GameItem(Items.MAGIC_LOGS_NOTED, 30),
                new GameItem(Items.RAW_SHARK_NOTED, 20),
                new GameItem(Items.RAW_MONKFISH_NOTED, 40),
                new GameItem(Items.RAW_ANGLERFISH_NOTED, 20)));

        items.put(LootRarity.UNCOMMON, Arrays.asList(
                new GameItem(Items.DRAGON_HARPOON, 1),
                new GameItem(Items.DRAGON_AXE, 1),
                new GameItem(Items.DRAGON_PICKAXE, 1),
                new GameItem(Items.ABYSSAL_WHIP, 1),
                new GameItem(Items.AMULET_OF_FURY, 1),
                new GameItem(Items.BERSERKER_RING, 1),
                new GameItem(Items.WARRIOR_RING, 1),
                new GameItem(Items.ARCHERS_RING, 1),
                new GameItem(Items.SEERS_RING, 1),
                new GameItem(Items.RANGER_BOOTS, 1),
                new GameItem(Items.DRAGON_BOOTS, 1),
                new GameItem(Items.INFINITY_BOOTS, 1)));

        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(Items.KRAKEN_TENTACLE, 1),
                new GameItem(Items.TRIDENT_OF_THE_SEAS, 1),
                new GameItem(Items.TANZANITE_FANG, 1),
                new GameItem(Items.MAGIC_FANG, 1),
                new GameItem(Items.SERPENTINE_VISAGE, 1),
                new GameItem(Items.DRACONIC_VISAGE, 1),
                new GameItem(Items.AVERNIC_DEFENDER_HILT, 1),
                new GameItem(Items.PEGASIAN_BOOTS, 1),
                new GameItem(Items.PRIMORDIAL_BOOTS, 1),
                new GameItem(Items.ETERNAL_BOOTS, 1),
                new GameItem(Items.BANDOS_GODSWORD, 1),
                new GameItem(Items.ZAMORAK_GODSWORD, 1),
                new GameItem(Items.SARADOMIN_GODSWORD, 1),
                new GameItem(Items.ARMADYL_GODSWORD, 1),
                new GameItem(Items.DRAGON_WARHAMMER, 1)));
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    @Override
    public void roll(Player c) {

    }
}
