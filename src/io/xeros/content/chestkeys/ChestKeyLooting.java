package io.xeros.content.chestkeys;

import io.xeros.content.event.eventcalendar.EventChallenge;
import io.xeros.model.Items;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.ItemAssistant;
import io.xeros.util.Misc;

public class ChestKeyLooting {

    /**
     * IF YOU ADD LOOT TO THIS, ALSO ADD TO {@link io.xeros.content.item.lootable.impl.ChestKey}
     * SO IT WILL SHOW ON THE LOOT TABLE INTERFACE. NEED TO MERGE THIS INTO THE OTHER CLASS soonTM
     */

    static int[][] COMMON = {
            {Items.COAL_NOTED, 30, 50}, {Items.RUNITE_ORE_NOTED, 5, 15},
            {Items.ADAMANTITE_ORE_NOTED, 8, 23}, {Items.MITHRIL_ORE_NOTED, 10, 30},
            {Items.STEEL_BAR_NOTED, 10, 30}, {Items.MITHRIL_BAR_NOTED, 5, 15}, {Items.GRIMY_TORSTOL_NOTED, 7, 13},
            {Items.GRIMY_SNAPDRAGON_NOTED, 7, 12}, {Items.GRIMY_RANARR_WEED_NOTED, 7, 12},
            {Items.GRIMY_CADANTINE_NOTED, 7, 12}, {Items.UNCUT_DRAGONSTONE_NOTED, 20, 30},
            {Items.UNCUT_DIAMOND_NOTED, 30, 70}, {Items.YEW_LOGS_NOTED, 20, 30},
            {Items.MAGIC_LOGS_NOTED, 20, 30}, {Items.RAW_SHARK_NOTED, 10, 20},
            {Items.RAW_MONKFISH_NOTED, 30, 40}, {Items.RAW_ANGLERFISH_NOTED, 10, 20}
    };

    static int[][] UNCOMMON = {
            {Items.DRAGON_HARPOON, 1, 1}, {Items.ABYSSAL_WHIP, 1, 1}, {Items.AMULET_OF_FURY, 1, 1}, {Items.BERSERKER_RING, 1, 1},
            {Items.WARRIOR_RING, 1, 1}, {Items.ARCHERS_RING, 1, 1}, {Items.SEERS_RING, 1, 1}, {Items.RANGER_BOOTS, 1, 1},
            {Items.INFINITY_BOOTS, 1, 1}, {Items.DRAGON_BOOTS, 1, 1}, {Items.DRAGON_AXE, 1, 1}, {Items.DRAGON_PICKAXE, 1, 1}
    };

    static int[][] RARE = {
            {Items.KRAKEN_TENTACLE, 1, 1}, {Items.TRIDENT_OF_THE_SEAS, 1, 1}, {Items.TANZANITE_FANG, 1, 1},
            {Items.MAGIC_FANG, 1, 1}, {Items.SERPENTINE_VISAGE, 1, 1}, {Items.DRACONIC_VISAGE, 1, 1}, {Items.AVERNIC_DEFENDER_HILT, 1, 1},
            {Items.PEGASIAN_BOOTS, 1, 1}, {Items.PRIMORDIAL_BOOTS, 1, 1}, {Items.ETERNAL_BOOTS, 1, 1}, {Items.BANDOS_GODSWORD, 1, 1},
            {Items.ZAMORAK_GODSWORD, 1, 1}, {Items.SARADOMIN_GODSWORD, 1, 1}, {Items.ARMADYL_GODSWORD, 1, 1}, {Items.DRAGON_WARHAMMER, 1, 1}
    };

    public static void lootChest(Player player) {
        if (!player.getItems().playerHasItem(432)) {
            player.getDH().sendStatement("You need a chest key to loot the chest.");
            return;
        }
        if (player.getItems().freeSlots() < 2) {
            player.getDH().sendStatement("You need at least 2 free inventory slots to loot the chest.");
            return;
        }

        player.getItems().deleteItem(Items.CHEST_KEY, 1);
        player.startAnimation(832);
        player.treasureChests += 1;

        var coinAmount = Misc.random(120000, 300000);
        player.getItems().addItem(995, coinAmount);

        var usingChestBonus = player.getItems().playerHasItem(Items.CHEST_RATE_BONUS);
        if (usingChestBonus) {
            player.getItems().deleteItem(Items.CHEST_RATE_BONUS, 1);
            player.getEventCalendar().progress(EventChallenge.USE_X_CHEST_RATE_INCREASE_TABLETS, 1);
            player.sendMessage("You sacrifice your chest bonus tablet for an increased drop rate.");
        }

        var rareChance = Misc.hasOneOutOf(usingChestBonus ? 425 : 500);
        var uncommonChance = Misc.hasOneOutOf(usingChestBonus ? 85 : 100);
        var randomCommon = Misc.random(COMMON.length - 1);
        var randomUncommon = Misc.random(UNCOMMON.length - 1);
        var randomRare = Misc.random(RARE.length - 1);
        var item = rareChance ? RARE[randomRare][0] : uncommonChance ? UNCOMMON[randomUncommon][0] : COMMON[randomCommon][0];
        var minAmt = rareChance ? RARE[randomRare][1] : uncommonChance ? UNCOMMON[randomUncommon][1] : COMMON[randomCommon][1];
        var maxAmt = rareChance ? RARE[randomRare][2] : uncommonChance ? UNCOMMON[randomUncommon][2] : COMMON[randomCommon][2];
        var amount = Misc.random(minAmt, maxAmt);

        player.getItems().addItem(item, amount);
        player.sendMessage("You loot the chest and find <col=800000>" + amount + " x " + ItemAssistant.getItemName(item) + "</col> and <col=800000>" + Misc.formatNumber(coinAmount) + " coins.</col>");
        player.sendMessage("You have now looted " + player.getTreasureChests() + " chests");

        if (rareChance) {
            PlayerHandler.dropMessage(player.getDisplayName() + " has received " + Misc.getAorAn(ItemAssistant.getItemName(item)) + " from a chest key! (#" + player.getTreasureChests() + ")");
        }
    }

}
