package io.xeros.content;

import io.xeros.model.entity.player.Player;

public class ChargeTrident {

	private final Player player;

	public ChargeTrident(Player player) {
		this.player = player;
	}

	public void chargeToxicTrident() {
		if (player.getToxicTridentCharge() >= 2500) {
			player.sendMessage("Your trident already has 2,500 charges.");
			return;
		}
		int chargesAdded = 0;
		while (conditionsMet("swamp")) {
			player.getItems().deleteItem2(554, 5);
			player.getItems().deleteItem2(560, 1);
			player.getItems().deleteItem2(562, 1);
			player.getItems().deleteItem2(12934, 1);
			player.setToxicTridentCharge(player.getToxicTridentCharge() + 1);
			chargesAdded++;
		}
		if (chargesAdded > 0) {
			player.sendMessage("You successfully added " + chargesAdded + " charges to your trident.");
		}
	}

	public void chargeTrident() {
		if (player.getTridentCharge() >= 2500) {
			player.sendMessage("Your trident already has 2,500 charges.");
			return;
		}
		int chargesAdded = 0;
		while (conditionsMet("sea")) {
			int price = player.getRechargeItems().hasItem(13129) ? 800
					: player.getRechargeItems().hasItem(13130) ? 600
					: player.getRechargeItems().hasItem(13131) ? 400
					: player.getRechargeItems().hasItem(13132) ? -1 : 1000;

			player.getItems().deleteItem2(554, 5);
			player.getItems().deleteItem2(560, 1);
			player.getItems().deleteItem2(562, 1);
			if (price != -1) { player.getItems().deleteItem2(995, price); }
			player.setTridentCharge(player.getTridentCharge() + 1);
			chargesAdded++;
		}
		if (chargesAdded > 0) {
			player.sendMessage("You successfully added " + chargesAdded + " charges to your trident.");
		}
	}

	public boolean conditionsMet(String weapon) {
		switch (weapon) {
			case "sea":
				int price = player.getRechargeItems().hasItem(13129) ? 800
						: player.getRechargeItems().hasItem(13130) ? 600
						: player.getRechargeItems().hasItem(13131) ? 400
						: player.getRechargeItems().hasItem(13132) ? -1 : 1000;

				if (player.getTridentCharge() >= 2500) {
					return false;
				}
				if (!player.getItems().playerHasItem(995, price == -1 ? -1 : price * 10)) {
					player.sendMessage("You need coins to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(554, 5)) {
					player.sendMessage("You need fire runes to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(560, 1)) {
					player.sendMessage("You need death runes to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(562, 1)) {
					player.sendMessage("You need chaos runes to charge your trident.");
					return false;
				}
				return true;

			case "swamp":
				if (!player.getItems().playerHasItem(12899, 1)) {
					return false;
				}
				if (player.getToxicTridentCharge() >= 2500) {
					return false;
				}
				if (!player.getItems().playerHasItem(12934, 1)) {
					player.sendMessage("You need Zulrah scales to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(554, 5)) {
					player.sendMessage("You need fire runes to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(560, 1)) {
					player.sendMessage("You need death runes to charge your trident.");
					return false;
				}
				if (!player.getItems().playerHasItem(562, 1)) {
					player.sendMessage("You need chaos runes to charge your trident.");
					return false;
				}
				return true;
		}
		return false;
	}

}
