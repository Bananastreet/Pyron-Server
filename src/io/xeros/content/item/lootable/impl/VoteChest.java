package io.xeros.content.item.lootable.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xeros.Server;
import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.event.eventcalendar.EventChallenge;
import io.xeros.content.item.lootable.LootRarity;
import io.xeros.content.item.lootable.Lootable;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.GameItem;
import io.xeros.util.Misc;

public class VoteChest implements Lootable {

    public static final int KEY = 22093; //vote key heree
    private static final int ANIMATION = 881;

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(6666), //flippers15
                new GameItem(23285), //flippers15
                new GameItem(6585), //jester hat
                new GameItem(24015), //elven top
                new GameItem(24018), //elven skirt
                new GameItem(6859))); //jester scarf
        items.put(LootRarity.UNCOMMON, Arrays.asList(
                new GameItem(6666), //flippers15
                new GameItem(23285), //flippers15
                new GameItem(6585), //jester hat
                new GameItem(6859))); //jester scarf


        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(9470), //gnome scarf
                new GameItem(6548), //gnome scarf
                new GameItem(1037), //bunny ears
                 new GameItem(1053), //green hween
                new GameItem(1055), //blue hween
        new GameItem(1057), //red hween
        new GameItem(11847) //black hween
        ));
    }

    private static GameItem randomChestRewards(Player c, int chance) {
        int random = Misc.random(chance);
        int rareChance = 90;
        int uncommonChance = 50;
        if (c.getItems().playerHasItem(21046)) {
            rareChance = 89;
            c.getItems().deleteItem(21046, 1);
            c.sendMessage("@red@You sacrifice your @cya@tablet @red@for an increased drop rate." );
            c.getEventCalendar().progress(EventChallenge.USE_X_CHEST_RATE_INCREASE_TABLETS, 1);
        }
        List<GameItem> itemList = random < uncommonChance ? items.get(LootRarity.COMMON) : random >= uncommonChance && random <= rareChance ? items.get(LootRarity.UNCOMMON) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }

    private static void votePet(Player c) {
        int petchance = Misc.random(1500);
        if (petchance >= 1499) {
            c.getItems().addItem(21262, 1);
            c.getCollectionLog().handleDrop(c, 5, 21262, 1);
            PlayerHandler.executeGlobalMessage("@red@- "+ c.getDisplayName() +"@blu@ has just received the @red@Vote Genie Pet");
            c.sendMessage("@red@@cr10@You pet genie is waiting in your bank, waiting to serve you as his master.");
            c.gfx100(1028);
        }
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    @Override
    public void roll(Player c) {
        if (c.getItems().playerHasItem(KEY)) {
            c.getItems().deleteItem(KEY, 1);
            Achievements.increase(c, AchievementType.VOTE_CHEST_UNLOCK, 1);
            c.startAnimation(ANIMATION);
            GameItem reward = randomChestRewards(c, 100);
            String name = ItemDef.forId(reward.getId()).getName();
            if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
                Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
            }
        } else {
            c.sendMessage("You need a vote key to open this chest.");
        }
    }
}
