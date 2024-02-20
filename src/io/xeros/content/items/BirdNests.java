package io.xeros.content.items;

import io.xeros.model.Items;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.ItemAssistant;
import io.xeros.util.Misc;

public class BirdNests {

    public static void loot(Player player, String type, int itemId) {
        if (!player.getItems().playerHasItem(itemId)) {
            return;
        }
        if (player.getItems().freeSlots() < 2) {
            player.sendMessage("You need at least 2 free inventory spaces to loot a bird nest.");
            return;
        }
        player.getItems().deleteItem(itemId, 1);
        player.getItems().addItem(Items.BIRD_NEST, 1);
        StringBuilder lootString = new StringBuilder("You loot the nest and find: ");

        var loot = getLootForType(type, itemId);
        var amount = 1;
        player.getItems().addItem(loot, amount);
        lootString.append("<col=800000>").append(amount).append(" x ").append(ItemAssistant.getItemName(loot));
        player.sendMessage(lootString.toString());
    }

    private static int getLootForType(String type, int itemId) {
        switch (type) {
            case "EGG":
                switch (itemId) {
                    case Items.BIRD_NEST_6:
                        return Items.BIRDS_EGG;
                    case Items.BIRD_NEST_2:
                        return Items.BIRDS_EGG_3;
                    case Items.BIRD_NEST_3:
                        return Items.BIRDS_EGG_2;
                }
                break;

            case "RING":
                var possibleRings = new int[] { Items.GOLD_RING, Items.SAPPHIRE_RING, Items.EMERALD_RING, Items.RUBY_RING, Items.DIAMOND_RING};
                return possibleRings[Misc.random(possibleRings.length - 1)];

            case "SEED":
                var possibleSeeds = new int[] { Items.ACORN, Items.APPLE_TREE_SEED, Items.WILLOW_SEED, Items.BANANA_TREE_SEED,
                Items.ORANGE_TREE_SEED, Items.CURRY_TREE_SEED, Items.MAPLE_SEED, Items.PINEAPPLE_SEED, Items.PAPAYA_TREE_SEED,
                Items.YEW_SEED, Items.PALM_TREE_SEED, Items.CALQUAT_TREE_SEED, Items.SPIRIT_SEED, Items.DRAGONFRUIT_TREE_SEED,
                Items.MAGIC_SEED, Items.TEAK_SEED, Items.MAHOGANY_SEED, Items.CELASTRUS_SEED, Items.REDWOOD_TREE_SEED };
                return possibleSeeds[Misc.random(possibleSeeds.length - 1)];
        }
        return -1;
    }

    public static boolean firstClickItem(Player player, int itemId) {
        switch(itemId) {
            case Items.BIRD_NEST_2:
            case Items.BIRD_NEST_3:
            case Items.BIRD_NEST_6:
                loot(player, "EGG", itemId);
                return true;

            case Items.BIRD_NEST_5:
                loot(player, "RING", itemId);
                return true;

            case Items.BIRD_NEST_4:
                loot(player, "SEED", itemId);
                return true;
        }
        return false;
    }

}
