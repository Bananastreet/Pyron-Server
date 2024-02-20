package io.xeros.content.upgrade;

import io.xeros.Configuration;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.ItemAssistant;
import io.xeros.sql.DiscordWebhook;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;

public class UpgradeHandler {

	private final int maxItems = 50; 
	
	private Player player;

	public UpgradeHandler(Player player) {
		this.player = player;
	}
	
	private static final UpgradeData[] data = UpgradeData.values();
	
	public void init(Player player) {
		player.setUpgradeType(UpgradeType.WEAPON);
	}
	
	public void openInterface() {
		player.setUpgradeType(UpgradeType.WEAPON);
		selectTab(113082);
		player.getPA().showInterface(29000);
	}
	
	public boolean selectTab(int buttonId) {
		switch(buttonId) {
			case 113074:
				player.getPA().closeAllWindows();
				return true;

			case 113082:
				player.setUpgradeType(UpgradeType.WEAPON);
				player.getPA().sendFrame126("Weapon Upgrades", 29003);
				loadList();
				return true;

			case 113083:
				player.setUpgradeType(UpgradeType.ARMOR);
				player.getPA().sendFrame126("Armour Upgrades", 29003);
				loadList();
				return true;

			case 113084:
				player.setUpgradeType(UpgradeType.TOOL);
				player.getPA().sendFrame126("Misc Upgrades", 29003);
				loadList();
				return true;
		}
		return false;
	}
	
	public void clearList() {
		for(int i = 29021; i < 29021 + maxItems; i++) {
			player.getPA().sendFrame126("", i);
		}
	}
	
	public void loadList() {
		clearList();
		int frame = 29021;
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				player.getPA().sendFrame126(Misc.capitalizeJustFirst(data.name().replace("_", " ")), frame++);
				if (frame >= 29021 + maxItems) {
					System.err.println("You are placing a value greater than the max list items");
					return;
				}
			}
		}
	}
	
	public void clearItems() {
		for(int i = 0; i < 12; i++)
			player.getPA().itemOnInterface(-1, -1, 29081, i);
		player.getPA().itemOnInterface(-1, -1, 29009, 0);
	}
	
	public void displayItems(int buttonId) {
		clearItems();
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				if(buttonId == data.getClickId()) {
					for(int i = 0; i < data.getIngredients().length; i++)
						player.getPA().itemOnInterface(data.getIngredients()[i].getId(), data.getIngredients()[i].getAmount(), 29081, i);
					player.getPA().itemOnInterface(data.getResultItem(), 1, 29009, 0);
					player.getPA().itemOnInterface(data.getSafeItem(), 1, 29018, 0);
				}
			}
		}
	}
	
	public boolean button(int buttonId) {
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				if (buttonId == data.getClickId()) {
					player.setCurrentUpgrade(data);
					//player.sendMessage("Selected: "+data);
					player.getPA().sendFrame126("Success rate: "+ data.getSuccessRate() +"%", 29008);
					displayItems(buttonId);
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkVirtual() {
		if(player.getCurrentUpgrade().getOtherCurrency()) {
			if(player.getSlayer().getPoints() >= player.getCurrentUpgrade().getCurrencyAmount()) {
				player.sendMessage("You had enough slayer points for this!");
				player.getSlayer().setPoints(player.getSlayer().getPoints() - player.getCurrentUpgrade().getCurrencyAmount());
				return true;
			} else {
				player.sendMessage("You do not have enough slayer points! You have: " +player.getSlayer().getPoints());
				return false;
			}
		}
		return true;
	}
	
	public void upgrade() {
		if (player.getCurrentUpgrade() != null) {
			for(int i = 0; i < player.getCurrentUpgrade().getIngredients().length; i++) {
				if(!player.getItems().playerHasItem(player.getCurrentUpgrade().getIngredients()[i].getId(), player.getCurrentUpgrade().getIngredients()[i].getAmount())) {
					player.getDH().sendStatement("You don't have the required items to create the "+ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem()));
					//player.getDH().sendNpcChat2("You don't have the required ingredients to create", "the "+ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem())+".", 3562, "Crazy Professor");
					player.nextChat = -1;
					//player.sendMessage("You are missing: " +player.getCurrentUpgrade().getIngredients()[i].getId());
					return;
				}
			}
			if(checkVirtual()) {
				int randomInt = Misc.random(100);
				if(randomInt <= player.getCurrentUpgrade().getSuccessRate()) {
					for(int k = 0; k < player.getCurrentUpgrade().getIngredients().length; k++) {
						player.getItems().deleteItem2(player.getCurrentUpgrade().getIngredients()[k].getId(), player.getCurrentUpgrade().getIngredients()[k].getAmount());
					}
					player.getDH().sendItemStatement("You have successfully created the "+ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem())+".", player.getCurrentUpgrade().getResultItem());
					//player.getDH().sendNpcChat2("Congratulations, you successfully created the", ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem()) + ".", 3562, "Crazy Professor");
					player.nextChat = -1;
					player.getItems().addItem(player.getCurrentUpgrade().getResultItem(), 1);
					//AchievementHandler.activate(player, AchievementList.UPGRADE_1, 1);//NEW ACHIEVEMENTS
					//if (player.getCurrentUpgrade().getSuccessRate() != 100) {
					PlayerHandler.dropMessage(player.getDisplayNameFormatted() + " successfully crafted: " + ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem()) + "!");
					DiscordWebhook webhook = new DiscordWebhook(Configuration.AchievementsWebhook);
					webhook.addEmbed(new DiscordWebhook.EmbedObject()
							.setTitle("Upgrade Craftsman")
							.setDescription(player.getDisplayName() + " has successfully crafted the " + ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem()) + ".")
							.setColor(Color.RED));
					try {
						if (Configuration.DiscordEnabled)
							webhook.execute(); //Handle exception
					} catch (IOException e) {
						e.printStackTrace();
					}
					//}
				} else {
					for(int k = 0; k < player.getCurrentUpgrade().getIngredients().length; k++) {
						if (player.getCurrentUpgrade().getIngredients()[k].getId() != player.getCurrentUpgrade().getSafeItem()) 
							player.getItems().deleteItem2(player.getCurrentUpgrade().getIngredients()[k].getId(), player.getCurrentUpgrade().getIngredients()[k].getAmount());
					}
					player.getDH().sendStatement("You failed to create the "+ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem())+".");
					//player.getDH().sendNpcChat2("Aww, unfortunately you failed to create the", ItemAssistant.getItemName(player.getCurrentUpgrade().getResultItem()) + ". Better luck next time.", 3562, "Crazy Professor");
					player.nextChat = -1;
					//player.sendMessage("Unlucky! You rolled a " +randomInt);
				}
			}
		} else {
			player.sendMessage("You must select an item first!");
			player.nextChat = -1;
			}
	}
	
}