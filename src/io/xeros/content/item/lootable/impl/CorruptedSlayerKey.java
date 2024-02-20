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
import io.xeros.model.Items;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;
import io.xeros.util.Misc;

public class CorruptedSlayerKey implements Lootable {

    private static final int KEY = 3460;
    private static final int ANIMATION = 881;

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(

                new GameItem(995, 1500000), //1.5m coins
                new GameItem(4586, 3), //3 dragon skirts
                new GameItem(537, 100), //100 Dragon Bones
                new GameItem(452, 50), //50 rune ore
                new GameItem(450, 100), //100 addy ore
                new GameItem(1516, 100), //100 yew logs
                new GameItem(1514, 50), //50 magic logs
                new GameItem(1632, 25), //25 Uncut Dragonstones
                new GameItem(6571, 1), //Uncut Onyx
              //  new GameItem(5020, 1), //Perk Ticket
                new GameItem(26500, 3))); //3 Teleport To Task Scrolls

        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(23497, 1), //upgrade token
                new GameItem(25365, 1), //1 hour cox boost
                new GameItem(11740, 1), //1 hour 10% drop boost
                new GameItem(7478, 10), //10 Donator Tokens
                new GameItem(25087, 1), //1 hour double drop chance
                new GameItem(6199, 1) //Mystery Box
        ));
    }

    private static GameItem randomChestRewards(Player c) {
        int random = Misc.random(100);
        int rareChance = 90;
        if (c.getItems().playerHasItem(21046)) {
            rareChance = 85;
            c.getItems().deleteItem(21046, 1);
            c.sendMessage("@red@You sacrifice your @cya@tablet @red@for an increased drop rate." );
            c.getEventCalendar().progress(EventChallenge.USE_X_CHEST_RATE_INCREASE_TABLETS, 1);
        }
        List<GameItem> itemList = random < rareChance ? items.get(LootRarity.COMMON) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    @Override
    public void roll(Player c) {
        if (c.getItems().playerHasItem(KEY)) {
            c.getItems().deleteItem(KEY, 1);
            c.startAnimation(ANIMATION);
            GameItem reward = randomChestRewards(c);
            int amount = reward.getAmount();
            if (c.getPermAttributes().getOrDefault(Player.BATTLEPASS_WINTER_2023)) {
                amount *= 2;
            }
            if (!c.getItems().addItem(reward.getId(), amount)) {
                Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, amount);
            }
        } else {
            c.sendMessage("@red@This chest requires a corrupted slayer key to open.");
        }
    }

}