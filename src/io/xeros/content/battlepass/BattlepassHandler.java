package io.xeros.content.battlepass;

import io.xeros.content.dialogue.DialogueBuilder;
import io.xeros.content.dialogue.DialogueOption;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;
import io.xeros.model.items.ItemAssistant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattlepassHandler {
    public static Map<Integer, GameItem> freePassRewards = new HashMap<>();
    public static Map<Integer, GameItem> premiumPassRewards = new HashMap<>();

    private static final ZoneId TIMEZONE = ZoneId.of("America/Denver");

    public static void openBattlepass(Player player) {
        player.battlepassPage = 1;
        displayPage(player, 1, "1 to 8");
        loadUnclaimed(player);
        refreshInterface(player);
        sendDescription(player);
        player.getPA().showInterface(9000);
    }

    public static void refreshInterface(Player player) {
        int experienceDiff = BattlepassLevels.calculateExperienceForLevel(player.battlepassLevel + 1) - player.battlepassExperience;
        player.getPA().sendFrame126(player.premiumBattlepass ? "@yel@Premium Battlepass" : "@whi@Free Battlepass", 9010);
        player.getPA().sendFrame126("Current Level: @gre@" + player.battlepassLevel, 9011);
        player.getPA().sendFrame126("Current XP: @gre@" + player.battlepassExperience, 9012);
        player.getPA().sendFrame126("XP till level: @gre@" + experienceDiff, 9013);
        sendDescription(player);
        sendTimeRemaining(player);
        loadUnclaimed(player);
    }

    public static void initialize() {
        FreePassData.loadFreePassData();
        PremiumPassData.loadPremiumPassData();
    }

    public static void takeUnclaimed(Player player) {
        List<GameItem> unclaimedItems = new ArrayList<>(player.getUnclaimedItems());

        for (GameItem item : unclaimedItems) {
            player.getItems().addItemUnderAnyCircumstance(item.getId(), item.getAmount());
        }
        player.getUnclaimedItems().clear();
        loadUnclaimed(player);
    }



    private static GameItem getFreeItemForLevel(int level) {
        return freePassRewards.getOrDefault(level, new GameItem(-1, -1)); // Return a default GameItem if the level isn't found in rewards
    }

    private static GameItem getPremiumItemForLevel(int level) {
        return premiumPassRewards.getOrDefault(level, new GameItem(-1, -1)); // Return a default GameItem if the level isn't found in rewards
    }
    public static void loadUnclaimed(Player player) {
        List<GameItem> unclaimedItems = player.getUnclaimedItems();

        if (unclaimedItems.isEmpty()) {
            for (int slot = 0; slot < 50; slot++) {
                player.getPA().itemOnInterface(-1, 0, 9005, slot);
            }
        } else {
            int slot = 0;
            for (GameItem item : unclaimedItems) {
                player.getPA().itemOnInterface(item.getId(), item.getAmount(), 9005, slot);
                slot++;
            }
            // Fill remaining slots with -1 if there are fewer items than slots
            while (slot < 50) {
                player.getPA().itemOnInterface(-1, 0, 9005, slot);
                slot++;
            }
        }
    }



    public static void addToUnclaimed(Player player, int level) {
        GameItem freeItem = freePassRewards.get(level);
        GameItem premiumItem = premiumPassRewards.get(level);

        if (freeItem != null && !player.getClaimedFreeLevels().contains(level)) {
            addUnclaimed(player, freeItem);
            player.getClaimedFreeLevels().add(level);
        } else {
            player.sendMessage("You have already banked the reward for free level " + level);
        }
        if (player.premiumBattlepass) {
            if (premiumItem != null && !player.getClaimedPremiumLevels().contains(level)) {
                addUnclaimed(player, premiumItem);
                player.getClaimedPremiumLevels().add(level);
            } else {
                player.sendMessage("You have already banked the reward for premium level " + level);
            }
        }
    }


    private static void addUnclaimed(Player player, GameItem item) {
        player.getUnclaimedItems().add(item);
    }

    public static boolean handleButton(Player player, int buttonId) {
        switch (buttonId) {

            case 35044:
                if (player.battlepassPage == 7) {
                    player.sendMessage("[Battlepass] You have reached the last page of rewards.");
                } else {
                    player.battlepassPage++;
                    selectPage(player);
                }
                return true;

            case 35043:
                if (player.battlepassPage == 1) {
                    player.sendMessage("[Battlepass] You are already viewing the first page of rewards.");
                } else {
                    player.battlepassPage--;
                    selectPage(player);
                }
                return true;
            case 35048:
                if (!player.getUnclaimedItems().isEmpty()) {
                    takeUnclaimed(player);
                } else {
                    player.sendMessage("[Battlepass] You do not have any rewards to claim at this moment in time.");
                }
                return true;
            case 35054:
                if (player.premiumBattlepass) {
                    player.sendMessage("[Battlepass] You already own the premium battlepass!");
                } else {
                    handlePurchase(player,false);
                }
                return true;
            case 35056:
                handlePurchase(player,true);
                return true;
            case 35061:
                player.getPA().closeAllWindows();
                return true;
        }
        return false;
    }

    public static void handlePurchase(Player player, Boolean gift) {
        if (!gift) {
            player.start(new DialogueBuilder(player)
                    .statement("The Premium Battlepass costs 50 donator tokens, are you sure?")
                    .option(new DialogueOption("Yes purchase premium.", p -> purchaseBattlepass(player)),
                            new DialogueOption("I've changed my mind.", p -> player.getPA().closeAllWindows())));
        } else {
            player.start(new DialogueBuilder(player)
                    .statement("Gifting the premium battlepass costs 50 donator tokens, are you sure?")
                    .option(new DialogueOption("Yes I'd like to continue.", p -> giftBattlepass(player)),
                            new DialogueOption("I've changed my mind.", p -> player.getPA().closeAllWindows())));
        }
    }

    public static void purchaseBattlepass(Player player) {
        boolean canAfford = player.getItems().playerHasItem(7478, 50);
        if (!canAfford) {
            player.getDH().sendStatement("You do not have enough donator tokens to purchase premium.");
            player.nextChat = 0;
        } else {
            player.getItems().deleteItem(7478, 50);
            player.premiumBattlepass = true;
            player.getDH().sendStatement("You have purchased the premium battlepass!");
            player.nextChat = 0;
            if (player.battlepassLevel >= 1) {
                for (int level = 1; level <= player.battlepassLevel; level++) {
                    GameItem premiumItem = getPremiumItemForLevel(level);
                    addUnclaimed(player, premiumItem);
                    player.sendMessage("[Battlepass] You have a new item ready to claim: " + ItemAssistant.getItemName(premiumItem.getId()));
                }
            }
        }
    }

    public static void giftBattlepass(Player player) {
        boolean canAfford = player.getItems().playerHasItem(7478, 50);
        if (!canAfford) {
            player.getDH().sendStatement("You do not have enough donator tokens to gift premium.");
            player.nextChat = 0;
        } else {
            player.getItems().deleteItem(7478, 50);
            player.getDH().sendStatement("Give this ticket to the player you'd like to gift the battlepass!");
            player.nextChat = 0;
            player.getItems().addItem(5020,1);

        }
    }

    public static void selectPage(Player player) {
        switch (player.battlepassPage) {
            case 1:
                displayPage(player, 1, "1 to 8");
                break;
            case 2:
                displayPage(player, 2, "9 to 16");
                break;
            case 3:
                displayPage(player, 3, "17 to 24");
                break;
            case 4:
                displayPage(player, 4, "25 to 32");
                break;
            case 5:
                displayPage(player, 5, "33 to 40");
                break;
            case 6:
                displayPage(player, 6, "41 to 48");
                break;
            case 7:
                displayPage(player, 7, "49 to 56");
                break;
            default:
                // Handle cases beyond page 7 if needed
                break;
        }
    }

    public static void displayPage(Player player, int pageNumber, String levels) {
        int startingItem = (pageNumber - 1) * 8 + 1;
        int endingItem = pageNumber * 8;

        int slot = 0;
        for (int level = startingItem; level <= endingItem; level++) {
            GameItem freeItem = getFreeItemForLevel(level);
            GameItem premiumItem = getPremiumItemForLevel(level);

            if (freeItem.getId() != -1 && premiumItem.getId() != -1) {
                player.getPA().itemOnInterface(freeItem.getId(), freeItem.getAmount(), 9111, slot);
                player.getPA().itemOnInterface(premiumItem.getId(), premiumItem.getAmount(), 9101, slot);
                slot++;
            }
        }
        player.getPA().sendFrame126("@or1@Viewing Page: @gre@" + pageNumber +"@or1@ - Levels @gre@" + levels,9018);
    }

    public static void sendDescription(Player player) {
        String rawDescription = "Introducing the Pyron Battle Pass! Choose between standard and premium tiers for access to exclusive rewards. The standard pass offers unique cosmetics and in-game currency, while the premium pass unlocks a wealth of bonuses like early access to limited-time items, XP boosts, exclusive emotes, and more.";

        // Manually wrap the description without adding periods
        int maxLength = 28;
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] words = rawDescription.split("\\s+");
        int currentLineLength = 0;

        for (String word : words) {
            if (currentLineLength + word.length() > maxLength) {
                descriptionBuilder.append("\\n"); // Add line break
                currentLineLength = 0;
            }
            descriptionBuilder.append(word).append(" ");
            currentLineLength += word.length() + 1; // +1 for the space
        }

        String description = descriptionBuilder.toString().replaceAll("\\n", "\\\\n");
        player.getPA().sendFrame126(description, 9020);
    }


    public static void sendTimeRemaining(Player player) {
        ZonedDateTime today = ZonedDateTime.now(TIMEZONE);
        ZonedDateTime nextReset = today.withDayOfMonth(1).plusMonths(1);
        long daysRemaining = ChronoUnit.DAYS.between(today, nextReset);
        player.getPA().sendFrame126("@or1@Time Remaining: @gre@" + daysRemaining + "@or1@ days", 9019);
    }

}
