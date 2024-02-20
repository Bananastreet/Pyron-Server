package io.xeros.content.item.lootable;

import io.xeros.Configuration;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardSaveData;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.content.leaderboard.UpdateLeaderboardData;
import io.xeros.content.leaderboard.impl.Boxes;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.GameItem;
import io.xeros.model.items.ItemAssistant;
import io.xeros.sql.DiscordWebhook;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public abstract class MysteryBoxLootable implements Lootable {

    public abstract int getItemId();

    /**
     * The player object that will be triggering this event
     */
    private final Player player;

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public MysteryBoxLootable(Player player) {
        this.player = player;
    }

    /**
     * Can the player open the mystery box
     */
    public boolean canMysteryBox = true;

    /**
     * The prize received
     */
    private int mysteryPrize;

    private int mysteryAmount;

    private int spinNum;

    /**
     * The chance to obtain the item
     */
    private int random;
    private boolean active;
    private final int INTERFACE_ID = 47000;
    private final int ITEM_FRAME = 47101;

    public boolean isActive() {
        return active;
    }

    public void draw() {
        openInterface();
        if (spinNum == 0) {
            for (int i = 0; i < 66; i++) {
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                GameItem NotPrize = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity()));
                final int NOT_PRIZE_ID = NotPrize.getId();
                sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID, 1);
            }
        } else {
            for (int i = spinNum * 50 + 16; i < spinNum * 50 + 66; i++) {
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                final int NOT_PRIZE_ID = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity())).getId();
                sendItem(i, (spinNum + 1) * 50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
            }
        }
        spinNum++;
    }

    public void spin() {

        // Server side checks for spin
        if (!canMysteryBox) {
            player.sendMessage("Please finish your current spin.");
            return;
        }
        if (!player.getItems().playerHasItem(getItemId())) {
            player.sendMessage("You require a mystery box to do this.");
            return;
        }

        // Delete box
        player.getItems().deleteItem(getItemId(), 1);
        // Initiate spin
        player.sendMessage(":resetBox");
        for (int i = 0; i < 66; i++) {
            player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
        }
        spinNum = 0;
        player.sendMessage(":spin");
        process();
    }

    public void process() {
        player.getPA().closeAllWindows();
        mysteryPrize = -1;
        mysteryAmount = -1;
        canMysteryBox = false;
        active = true;
        setMysteryPrize();

        // Send items to interface
        // Move non-prize items client side if you would like to reduce server load
        if (spinNum == 0) {
            for (int i = 0; i < 66; i++) {
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                GameItem NotPrize = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity()));
                final int NOT_PRIZE_ID = NotPrize.getId();
                sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID, 1);
            }
        } else {
            for (int i = spinNum * 50 + 16; i < spinNum * 50 + 66; i++) {
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                final int NOT_PRIZE_ID = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity())).getId();
                sendItem(i, (spinNum + 1) * 50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
            }
        }

        spinNum++;
        openInterface();
    }

    public void setMysteryPrize() {
        random = Misc.random(100);

        List<GameItem> itemList;

        if (random < 50) {
            itemList = getLoot().get(MysteryBoxRarity.COMMON.getLootRarity());
        } else if (random < 89) {
            itemList = getLoot().get(MysteryBoxRarity.UNCOMMON.getLootRarity());
        } else {
            itemList = getLoot().get(MysteryBoxRarity.RARE.getLootRarity());
        }

        GameItem item = Misc.getRandomItem(itemList);
        mysteryPrize = item.getId();
        mysteryAmount = item.getAmount();
    }


    public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID, int amount) {
        if (i == prizeSlot) {
            player.getPA().mysteryBoxItemOnInterface(PRIZE_ID, amount, ITEM_FRAME, i);
        } else {
            player.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, amount, ITEM_FRAME, i);
        }
    }

    public void openInterface() {
        player.boxCurrentlyUsing = getItemId();
        spinNum = 0;
        player.getPA().sendString(ItemDef.forId(getItemId()).getName(), 47002);
        player.getPA().showInterface(INTERFACE_ID);
    }

    public void canMysteryBox() {
        canMysteryBox = true;

    }

    public void quickOpen() {

        if (player.getUltraInterface().isActive() || player.getSuperBoxInterface().isActive() || player.getNormalBoxInterface().isActive() || player.getFoeInterface().isActive()) {
            player.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");

            return;
        }
        if (!(player.getSuperMysteryBox().canMysteryBox) || !(player.getNormalMysteryBox().canMysteryBox) || !(player.getPetMysteryBox().canMysteryBox) ||
                !(player.getUltraMysteryBox().canMysteryBox) || !(player.getFoeMysteryBox().canMysteryBox) ||
                !(player.getYoutubeMysteryBox().canMysteryBox) || !(player.getPVPMysteryBox().canMysteryBox) || !(player.getRaresMysteryBox().canMysteryBox)
        ) {
            player.getPA().showInterface(47000);
            player.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
            return;
        }
        if (player.getItems().playerHasItem(getItemId(), 1)) {
            player.getItems().deleteItem(getItemId(), 1);
            setMysteryPrize();
            roll(player);
        } else {
            player.sendMessage("@blu@You have used your last mystery box.");
        }
    }

    @Override
    public void roll(Player player) {
        if (mysteryPrize == -1) {
            canMysteryBox = true;
            player.getNormalMysteryBox().canMysteryBox();
            player.getUltraMysteryBox().canMysteryBox();
            player.getPVPMysteryBox().canMysteryBox();
            player.getRaresMysteryBox().canMysteryBox();
            player.getSuperMysteryBox().canMysteryBox();
            player.getFoeMysteryBox().canMysteryBox();
            player.getPetMysteryBox().canMysteryBox();
            player.getYoutubeMysteryBox().canMysteryBox();
            return;
        }
        String boxName = ItemDef.forId(getItemId()).getName();
        if (boxName.toLowerCase().equals("mystery box")) {
            player.mysteryBoxes++;
            player.sendMessage("You have now opened "+ player.mysteryBoxes + " mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.mysteryBoxes, Boxes.NORMAL);
            }
        }
        if (boxName.toLowerCase().equals("super mystery box")) {
            player.superBoxes++;
            player.sendMessage("You have now opened "+ player.superBoxes + " super mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.superBoxes, Boxes.SUPER);
            }
        }
        if (boxName.toLowerCase().equals("ultra mystery box")) {
            player.ultraBoxes++;
            player.sendMessage("You have now opened "+ player.ultraBoxes + " ultra mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.ultraBoxes, Boxes.ULTRA);
            }
        }
        if (boxName.toLowerCase().equals("pvp mystery box")) {
            player.pvpBoxes++;
            player.sendMessage("You have now opened "+ player.pvpBoxes + " pvp mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.pvpBoxes, Boxes.PVP);
            }
        }
        if (boxName.toLowerCase().equals("rares mystery box")) {
            player.raresBoxes++;
            player.sendMessage("You have now opened "+ player.raresBoxes + " rares mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.raresBoxes, Boxes.Rares);
            }
        }
        if (boxName.toLowerCase().equals("godwars mystery box")) {
            player.godwarsBoxes++;
            player.sendMessage("You have now opened "+ player.godwarsBoxes + " godwars mystery boxes.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.godwarsBoxes, Boxes.Godwars);
            }
        }
        if (boxName.toLowerCase().equals("master mystery chest")) {
            player.masterBoxes++;
            player.sendMessage("You have now opened "+ player.masterBoxes + " master mystery chests.");
            if (Configuration.leaderboardEnabled) {
                LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MYSTERY_BOXES, player.masterBoxes, Boxes.CHEST);
            }
        }

        player.sendMessage("Trying to add item: " +mysteryPrize);
        player.getItems().addItemUnderAnyCircumstance(mysteryPrize, mysteryAmount);
        if (random > 85 || ItemDef.forId(getItemId()).getName().toLowerCase().contains("master") && ItemDef.forId(getItemId()).getName().toLowerCase().contains("vitur") || ItemDef.forId(getItemId()).getName().toLowerCase().contains("master") && ItemDef.forId(getItemId()).getName().toLowerCase().contains("twisted")) {
            String name = ItemDef.forId(mysteryPrize).getName();
            String itemName = ItemDef.forId(getItemId()).getName();
            PlayerHandler.executeGlobalMessage("[<col=CC0000>" + itemName + "</col>] <col=255>"
                    + player.getDisplayName()
                    + "</col> hit the jackpot and got a <col=CC0000>" + name + "</col>!");
            DiscordWebhook webhook = new DiscordWebhook(Configuration.DropsWebhook);
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle(itemName)
                    .setDescription(player.getDisplayName() + " hit the box jackpot and received a "+ name)
                    .setColor(Color.RED));
            try {
                if (Configuration.DiscordEnabled)
                    webhook.execute(); //Handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        active = false;
        player.inDonatorBox = true;

        // Reward message


        // Can now spin again
        canMysteryBox = true;
        player.getNormalMysteryBox().canMysteryBox();
        player.getUltraMysteryBox().canMysteryBox();
        player.getPVPMysteryBox().canMysteryBox();
        player.getRaresMysteryBox().canMysteryBox();
        player.getSuperMysteryBox().canMysteryBox();
        player.getFoeMysteryBox().canMysteryBox();
        player.getPetMysteryBox().canMysteryBox();
        player.getYoutubeMysteryBox().canMysteryBox();
    }
}
