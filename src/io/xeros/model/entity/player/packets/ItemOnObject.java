package io.xeros.model.entity.player.packets;

/**
 * @author Ryan / Lmctruck30
 */

import java.util.Objects;

import io.xeros.Server;
import io.xeros.content.item.lootable.impl.HunllefChest;
import io.xeros.content.item.lootable.impl.NewHunllefKey;
import io.xeros.content.items.UseItem;
import io.xeros.model.collisionmap.WorldObject;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.PacketType;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.xeros.model.multiplayersession.MultiplayerSessionStage;
import io.xeros.model.multiplayersession.MultiplayerSessionType;
import io.xeros.model.multiplayersession.duel.DuelSession;
import io.xeros.model.tickable.impl.WalkToTickable;
import io.xeros.util.Misc;

public class ItemOnObject implements PacketType {

	public static final int[] dharoks = {4716, 4718, 4720, 4722};
	public static final int[] karils = {4732, 4734, 4736, 4738};
	public static final int[] ahrims = {4708, 4710, 4712, 4714};
	public static final int[] guthans = {4724, 4726, 4728, 4730};
	public static final int[] torags = {4745, 4747, 4749, 4751};
	public static final int[] veracs = {4753, 4755, 4757, 4759};

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getMovementState().isLocked() || c.getLock().cannotInteract(c))
			return;
		if (c.isFping()) {
			/**
			 * Cannot do action while fping
			 */
			return;
		}
		c.interruptActions();

		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readInteger();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();

		c.objectX = objectX;
		c.objectY = objectY;
		c.xInterfaceId = -1;
		c.getPA().stopSkilling();

		WorldObject object = ClickObject.getObject(c, objectId, objectX, objectY);

		if (object == null) {
			return;
		}

		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		Position size = object.getObjectSize();
		c.setTickable(new WalkToTickable(c, object.getPosition(), size.getX(), size.getY(), player1 -> {

			if (objectId == 39238) {

				if (itemId == 995) {
					int inventoryAmountCoins = c.getItems().getItemAmount(995);
					if (c.getItems().playerHasItem(995, inventoryAmountCoins)) {
						c.getItems().deleteItem(995, inventoryAmountCoins);
						c.getItems().addItemUnderAnyCircumstance(13204, inventoryAmountCoins / 1000);
						c.sendMessage("@red@You have converted "+ Misc.formatNumber(inventoryAmountCoins) + " coins to platinum tokens.");
						return;
					}
				}

				if (itemId == 13204) {
					int inventoryAmountTokens = c.getItems().getItemAmount(13204);
					int inventoryAmountCoins = c.getItems().getItemAmount(995);
					if (inventoryAmountTokens > 2000000) {
						c.sendMessage("@red@You can only do this with 2m tokens or less in your inventory.");
						return;
					}
					if (inventoryAmountCoins > 0) {
						c.sendMessage("@red@Please bank your coins before doing this");
						return;
					}
					if (c.getItems().playerHasItem(13204, inventoryAmountTokens)) {
						c.getItems().deleteItem(13204, inventoryAmountTokens);
						c.getItems().addItemUnderAnyCircumstance(995, inventoryAmountTokens * 1000);
						c.sendMessage("@red@You have converted "+ Misc.formatNumber(inventoryAmountTokens) + " platinum tokens to coins.");
						return;
					}
				}
			}

			if (objectId == 36087) {
				if (itemId == 23776) {
					NewHunllefKey.lootKey(c);
				}
			}

			if (objectId == 10583) {
				if (itemId == 4716 || itemId == 4718 || itemId == 4720 || itemId == 4722) {
					if (c.getItems().playerHasAllItems(dharoks)) {
						c.getItems().deleteItem(4716, 1);
						c.getItems().deleteItem(4718, 1);
						c.getItems().deleteItem(4720, 1);
						c.getItems().deleteItem(4722, 1);
						c.getItems().addItem(12877, 1);
					}
				}
				if (itemId == 4732 || itemId == 4734 || itemId == 4736 || itemId == 4738) {
					if (c.getItems().playerHasAllItems(karils)) {
						c.getItems().deleteItem(4732, 1);
						c.getItems().deleteItem(4734, 1);
						c.getItems().deleteItem(4736, 1);
						c.getItems().deleteItem(4738, 1);
						c.getItems().addItem(12883, 1);
					}
				}
				if (itemId == 4708 || itemId == 4710 || itemId == 4712 || itemId == 4714) {
					if (c.getItems().playerHasAllItems(ahrims)) {
						c.getItems().deleteItem(4708, 1);
						c.getItems().deleteItem(4710, 1);
						c.getItems().deleteItem(4712, 1);
						c.getItems().deleteItem(4714, 1);
						c.getItems().addItem(12881, 1);
					}
				}
				if (itemId == 4724 || itemId == 4726 || itemId == 4728 || itemId == 4730) {
					if (c.getItems().playerHasAllItems(guthans)) {
						c.getItems().deleteItem(4724, 1);
						c.getItems().deleteItem(4726, 1);
						c.getItems().deleteItem(4728, 1);
						c.getItems().deleteItem(4730, 1);
						c.getItems().addItem(12873, 1);
					}
				}
				if (itemId == 4745 || itemId == 4747 || itemId == 4749 || itemId == 4751) {
					if (c.getItems().playerHasAllItems(torags)) {
						c.getItems().deleteItem(4745, 1);
						c.getItems().deleteItem(4747, 1);
						c.getItems().deleteItem(4749, 1);
						c.getItems().deleteItem(4751, 1);
						c.getItems().addItem(12879, 1);
					}
				}
				if (itemId == 4753 || itemId == 4755 || itemId == 4757 || itemId == 4759) {
					if (c.getItems().playerHasAllItems(veracs)) {
						c.getItems().deleteItem(4753, 1);
						c.getItems().deleteItem(4755, 1);
						c.getItems().deleteItem(4757, 1);
						c.getItems().deleteItem(4759, 1);
						c.getItems().addItem(12875, 1);
					}
				}
			}

			c.getFarming().handleItemOnObject(itemId, objectId, objectX, objectY);
			switch (c.objectId) {

				case 2030: //Allows for items to be used from both sides of the furnace
					c.objectDistance = 4;
					c.objectXOffset = 3;
					c.objectYOffset = 3;
					break;
				case 26782:
					c.objectDistance = 7;
					break;
				case 33320:
					c.objectDistance = 5;
					break;
				case 33311:
					c.objectDistance = 3;//hespori
					break;
				case 18818:
				case 409:
				case 42163:
					c.objectDistance = 3;
					break;
				case 884:
					c.objectDistance = 5;
					c.objectXOffset = 3;
					c.objectYOffset = 3;
					break;

				case 28900:
					c.objectDistance = 3;
					break;


				default:
					c.objectDistance = 1;
					c.objectXOffset = 0;
					c.objectYOffset = 0;
					break;

			}

			c.facePosition(objectX, objectY);
			UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		}));
	}

}
