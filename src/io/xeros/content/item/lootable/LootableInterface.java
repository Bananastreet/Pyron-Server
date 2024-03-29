package io.xeros.content.item.lootable;

import io.xeros.content.item.lootable.impl.*;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerAssistant;
import io.xeros.model.items.GameItem;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shows {@link Lootable} tables.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class LootableInterface {

    private static final int INTERFACE_ID = 44_942;

    // Common table
    private static final int COMMON_SCROLLABLE_INTERFACE_ID = 45143;
    private static final int COMMON_INVENTORY_INTERFACE_ID = 45144;

    // Rare table
    private static final int RARE_SCROLLABLE_INTERFACE_ID = 45183;
    private static final int RARE_INVENTORY_INTERFACE_ID = 45184;

    private static final int VIEW_TABLE_BUTTON_START_ID = 175211;

    private static final int CURRENT_VIEW_CONFIG_ID = 1354;

    private static final int[] BUTTONS = {175211, 175214, 175217, 175220, 175223, 175226, 175229,
            175232, 175235, 175238, 175241, 175244, 175247, 175250, 175253, 176_000, 176_003};

    public enum LootableView {
        VOTE_KEY(new VoteChest()),
        WILDERNESS_KEY(new WildernessKey()),
        CHEST_KEY(new ChestKey()),
        CORRUPTED_SLAYER_CHEST(new CorruptedSlayerKey()),
        //HUNNLEFS_KEY(new HunllefChest()),
        //SERENS_KEY(new SerenChest()),
        //HESPORI_KEY(new HesporiChest()),
        MYSTERY_BOX(new NormalMysteryBox(null)),
        SUPER_MYSTERY_BOX(new SuperMysteryBox(null)), // Item sprite won't draw small for some reason..
        ULTRA_MYSTERY_BOX(new UltraMysteryBox(null)),
        PVP_MYSTERY_BOX(new PVPMysteryBox(null)),
        Rares_MYSTERY_BOX(new RaresMysteryBox(null)),
        Godwars_MYSTERY_BOX(new GodwarsMysteryBox(null)),
        MASTER_MYSTERY_CHEST(new FoeMysteryBox(null)),
        //SLAYER_MYSTERY_CHEST(new SlayerMysteryBox(null)),
        BRIMSTONE_KEY(new KonarChest()),
        RAIDS(new RaidsChestRare()),
        TOB(new TheatreOfBloodChest()),
        VOTE_MYSTERY_BOX(new VoteMysteryBox()),
        CRYSTAL_CHEST(new CrystalChest()),
        PVM_CASKET(new PvmCasket()),
        LARRANS_CHEST(new LarransChest()),

        ;

        private List<GameItem> common;
        private List<GameItem> rare;

        LootableView(Lootable lootable) {
            this.common = new ArrayList<>();
            this.rare = new ArrayList<>();

            List<GameItem> addingCommon = lootable.getLoot().get(LootRarity.COMMON);
            List<GameItem> addingUncommon = lootable.getLoot().get(LootRarity.UNCOMMON);
            List<GameItem> addingRare = lootable.getLoot().get(LootRarity.RARE);

            if (addingCommon != null)
                common.addAll(lootable.getLoot().get(LootRarity.COMMON));
            if (addingUncommon != null)
                common.addAll(lootable.getLoot().get(LootRarity.UNCOMMON));
            if (addingRare != null)
                rare.addAll(lootable.getLoot().get(LootRarity.RARE));

            common = common.stream().filter(Misc.distinctByKey(GameItem::getId)).collect(Collectors.toList());
            rare = rare.stream().filter(Misc.distinctByKey(GameItem::getId)).collect(Collectors.toList());

            common = Collections.unmodifiableList(common);
            rare = Collections.unmodifiableList(rare);
        }

        public int getButtonId() {
            return VIEW_TABLE_BUTTON_START_ID + (ordinal() * 5);
        }
    }

    public static void openInterface(Player player) {
        open(player, LootableView.VOTE_KEY);
    }

    public static void openSpecificTable(Player player, LootableView lootTable) {
        open(player, lootTable);
    }

    private static void open(Player player, LootableView view) {
        player.getPA().sendConfig(CURRENT_VIEW_CONFIG_ID, view.ordinal());
        player.getPA().resetScrollBar(COMMON_SCROLLABLE_INTERFACE_ID);
        player.getPA().resetScrollBar(RARE_SCROLLABLE_INTERFACE_ID);
//        player.getItems().sendItemContainer(COMMON_INVENTORY_INTERFACE_ID, view.common);
//        player.getItems().sendItemContainer(RARE_INVENTORY_INTERFACE_ID, view.rare);
        PlayerAssistant.sendItems(player,COMMON_INVENTORY_INTERFACE_ID, view.common, 1);
        PlayerAssistant.sendItems(player,RARE_INVENTORY_INTERFACE_ID, view.rare, 1);
        player.getPA().showInterface(INTERFACE_ID);
    }

    public static boolean button(Player player, int buttonId) {
        for (int index = 0; index < BUTTONS.length; index++) {
            if (buttonId == BUTTONS[index]) {
                open(player, LootableView.values()[index]);
                return true;
            }
        }

        return false;
    }
}
