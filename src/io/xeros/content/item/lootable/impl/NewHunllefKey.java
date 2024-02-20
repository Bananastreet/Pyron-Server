package io.xeros.content.item.lootable.impl;

import io.xeros.Configuration;
import io.xeros.model.Items;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.GameItem;
import io.xeros.model.items.ItemAssistant;
import io.xeros.sql.DiscordWebhook;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class NewHunllefKey {

    private static final int KEY = 23776;

  // public static int[] commonLoots = {1214, 454, 452, 1516, 1514, 1518, 450, 2362, 2, 13442, 537, 386, 3145};

    public static GameItem[] commonLoots = {
            new GameItem(21046, 2),//15% chest rate tomb
            new GameItem(995, 240000), //coins
            new GameItem(2996, 35),//pkp tickets
            new GameItem(537, 20 + Misc.random(10)),//dragon bones
            new GameItem(1306, 3),//dragon longsword
            new GameItem(11840),//dragon boots
            new GameItem(6889),//mages book
            new GameItem(2364, 100),//runite bar
            new GameItem(1514, 300),// magic logs
            new GameItem(1632, 80),//uncut dragonstone
            new GameItem(11230, 20 + Misc.random(30)),//darts
            new GameItem(1080, 6),//rune platelegs
            new GameItem(1128, 6),//rune platebody
            new GameItem(4087, 1),//dragon platelegs
            new GameItem(4585, 1),//dragon plateskirt
            new GameItem(4151, 1),//whip
            new GameItem(23804, 1),//imbuedifier

            new GameItem(21046, 2),//15% chest rate tomb
            new GameItem(995, 240000), //coins
            new GameItem(2996, 35),//pkp tickets
            new GameItem(537, 20 + Misc.random(10)),//dragon bones
            new GameItem(1306, 3),//dragon longsword
            new GameItem(11840),//dragon boots
            new GameItem(6889),//mages book
            new GameItem(2364, 100),//runite bar
            new GameItem(1514, 300),// magic logs
            new GameItem(1632, 80),//uncut dragonstone
            new GameItem(11230, 20 + Misc.random(30)),//darts
            new GameItem(1080, 6),//rune platelegs
            new GameItem(1128, 6),//rune platebody
            new GameItem(4087, 1),//dragon platelegs
            new GameItem(4585, 1),//dragon plateskirt
            new GameItem(4151, 1),//whip
            new GameItem(23804, 1),//imbuedifier

            new GameItem(21547, 1),//small foe bone
            new GameItem(21547, 1),//small foe bone
            new GameItem(21547, 1),//small foe bone
            new GameItem(21547, 1),//small foe bone
            new GameItem(21549, 1),//medium foe bone
            new GameItem(21549, 1),//medium foe bone
            new GameItem(21549, 1),//medium foe bone
            new GameItem(21551, 1),//large foe bone
            new GameItem(21551, 1),//large foe bone
            new GameItem(21553, 1)//rare foe bone
    };


    /*
    public static GameItem[] uncommonLoots = {
            new GameItem(4088, 1),
            new GameItem(4586, 1),
            new GameItem(2528, 1),
            new GameItem(4588, 1),
            new GameItem(1306, 1),
            new GameItem(1713, 1),
            new GameItem(6572, 1),  //TODO: Change amounts
    };
*/
  public static GameItem[] rareLoots = {
          new GameItem(23832, 1), //corrupted bow string
          new GameItem(23975, 1), //crystal body-----------------------------2
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23995, 1), //blade of saeldor
          new GameItem(23848, 1), //corrupt legs
          new GameItem(23842, 1), //corrupt helm
          new GameItem(23845, 1), //corrupt body
          new GameItem(23848, 1), //corrupt legs
          new GameItem(23842, 1), //corrupt helm
          new GameItem(23845, 1), //corrupt body  	 RARES//////////////////////////////////
          new GameItem(23975, 1), //crystal body-----------------------------1
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23975, 1), //crystal body-----------------------------2
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23975, 1), //crystal body-----------------------------3
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23975, 1), //crystal body-----------------------------4
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23975, 1), //crystal body-----------------------------5
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs
          new GameItem(23975, 1), //crystal body-----------------------------6
          new GameItem(23971, 1), //crystal helm
          new GameItem(23979, 1), //crystal legs


          new GameItem(23757, 1), //younglleff pet
          new GameItem(25865, 1) //boffa
  };

    public static void lootKey(Player player) {
        GameItem randomCommonLoot = Misc.getRandomItem(List.of(commonLoots));
        //GameItem randomUncommonLoot = Misc.getRandomItem(List.of(uncommonLoots));
        GameItem randomRareLoot = Misc.getRandomItem(List.of(rareLoots));
        int roll = Misc.random(1, 50);
        boolean hasChestRate = player.getItems().playerHasItem(21046);
        int rareChance = hasChestRate ? 2 : 1;
        if (player.getItems().playerHasItem(KEY) && player.getItems().freeSlots() > 3) {
            player.getItems().deleteItem(KEY, 1);
            if (hasChestRate) {
                player.getItems().deleteItem(21046, 1);
            }

            if (roll <= rareChance) {
                player.sendMessage("You have received a rare reward!");
                player.getItems().addItemUnderAnyCircumstance(randomRareLoot.getId(), randomRareLoot.getAmount());
                PlayerHandler.executeGlobalMessage("@bla@[@cya@HUNLLEF@bla@] "+ player.getDisplayName() +"@pur@ has just received a @bla@" + ItemAssistant.getItemName(randomRareLoot.getId()) + "!");
            } else {
       //         if (Misc.random(1, 10) >= 8) {
       //             player.sendMessage("You have received an uncommon loot.");
       //             player.getItems().addItemUnderAnyCircumstance(randomUncommonLoot.getId(), randomUncommonLoot.getAmount());
       //         } else {
                    player.sendMessage("You have received a common loot.");
                    player.getItems().addItemUnderAnyCircumstance(randomCommonLoot.getId(), randomCommonLoot.getAmount());
        //        }
            }
        } else {
            player.sendMessage("You need a hunllefs key and 3 free inventory spaces.");
        }
    }

}
