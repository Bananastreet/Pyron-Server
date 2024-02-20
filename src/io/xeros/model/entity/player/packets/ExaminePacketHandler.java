package io.xeros.model.entity.player.packets;

import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCHandler;
import io.xeros.model.entity.npc.stats.NpcBonus;
import io.xeros.model.entity.npc.stats.NpcCombatDefinition;
import io.xeros.model.entity.npc.stats.NpcCombatSkill;
import io.xeros.model.entity.player.PacketType;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Right;
import io.xeros.model.shops.ShopAssistant;
import io.xeros.util.Misc;

public class ExaminePacketHandler implements PacketType {

    public static final int EXAMINE_ITEM = 134;
    public static final int EXAMINE_NPC = 137;

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        switch (packetType) {
            case EXAMINE_ITEM:
                int item = c.getInStream().readInteger();
                int count = c.getInStream().readInteger();
                ItemDef itemDefinition = ItemDef.forId(item);
                if (c.debugMessage) {
                    c.sendMessage("Examine item: " + item);
                }

                StringBuilder examine = new StringBuilder();
                StringBuilder itemValue = new StringBuilder();
                if (itemDefinition != null) {
                    if (itemDefinition.getDescription() != null && !itemDefinition.getDescription().isEmpty()) {
                        examine.append(itemDefinition.getDescription());
                        examine.append(" ");
                    } else {
                        examine.append(itemDefinition.getName());
                        examine.append(" ");
                    }

                    int value = ShopAssistant.getItemShopValue(item);
                    int stackValue = value * count;

                    var exchangePrice = ShopAssistant.getExchangeItemValue(item, "sellToShop");
                    var stackExchangePrice = exchangePrice * count;
                    var exchangeString = (exchangePrice > 0 && exchangePrice != Integer.MAX_VALUE ? "@bla@, Exchange: @blu@" + Misc.formatCoins(stackExchangePrice) + " @bla@(@blu@"+exchangePrice+"@bla@ea)" : "");
                    var itemName = itemDefinition.getName();
                    if (value > 0) {
                        if (count < 2) {
                            itemValue.append("Price of @blu@").append(itemName).append(": @bla@Value: @blu@").append(Misc.formatCoins(value)).append(exchangeString);
                        } else {
                            itemValue.append("Price of @blu@").append(count).append(" x ").append(itemName).append(": @bla@Value: @blu@").append(Misc.formatCoins(stackValue)).append(" @bla@(@blu@").append(value).append("@bla@ea)").append(exchangeString);
                        }
                        itemValue.append(" ");
                    }
                    if (!itemDefinition.isTradable()) {
                        itemValue.append("<col=800000>(untradeable)");
                    }
                }

                if (examine.length() > 0) {
                    c.sendMessage(examine.toString());
                }
                if (itemValue.length() > 0) {
                    c.sendMessage(itemValue.toString());
                }
                break;
            case EXAMINE_NPC:
                int npcIndex = c.getInStream().readUnsignedWord();
                if (c.debugMessage) {
                    c.sendMessage("Examined " + npcIndex);
                }

                NPC npc = NPCHandler.npcs[npcIndex];
                if (npc != null) {
                    String header = "@dre@";

                    NpcCombatDefinition definition = npc.getCombatDefinition();
                    if (definition != null) {

                        c.sendMessage(header + "[" + npc.getDefinition().getName() + "]");
                        c.sendMessage(header +
                                "Levels ["
                                + "Melee: " + definition.getLevel(NpcCombatSkill.ATTACK) + ", "
                                + "Ranged: " + definition.getLevel(NpcCombatSkill.RANGE) + ", "
                                + "Magic: " + definition.getLevel(NpcCombatSkill.MAGIC) + ", "
                                + "Defence: " + definition.getLevel(NpcCombatSkill.DEFENCE) + ", "
                                + "Strength: " + definition.getLevel(NpcCombatSkill.STRENGTH)
                                + "]"
                        );
                        c.sendMessage(header + "Bonuses:");
                        c.sendMessage(header +
                                " - Attack ["
                                + "Melee: " + definition.getAttackBonus(NpcBonus.ATTACK_BONUS) + ", "
                                + "Ranged: " + definition.getAttackBonus(NpcBonus.ATTACK_RANGE_BONUS) + ", "
                                + "Magic: " + definition.getAttackBonus(NpcBonus.ATTACK_MAGIC_BONUS) + ", "
                                + "Magic Damage: " + definition.getAttackBonus(NpcBonus.MAGIC_STRENGTH_BONUS) + ", "
                                + "Range Strength: " + definition.getAttackBonus(NpcBonus.RANGE_STRENGTH_BONUS)
                                + "]"
                        );
                        c.sendMessage(header +
                                " - Defence ["
                                + "Stab: " + definition.getDefenceBonus(NpcBonus.STAB_BONUS) + ", "
                                + "Slash: " + definition.getDefenceBonus(NpcBonus.SLASH_BONUS) + ", "
                                + "Crush: " + definition.getDefenceBonus(NpcBonus.CRUSH_BONUS) + ", "
                                + "Ranged: " + definition.getDefenceBonus(NpcBonus.RANGE_BONUS) + ", "
                                + "Magic: " + definition.getDefenceBonus(NpcBonus.MAGIC_BONUS)
                                + "]"
                        );

                    } else
                        c.sendMessage(header + "[" + npc.getDefinition().getName() + "] has no combat stats.");

                    if (c.getRights().contains(Right.OWNER)) {
                        c.sendMessage(header + "Position: " + npc.getPosition() + ", Size: " + npc.getSize() + ", ID: " + npc.getNpcId() + ", idx: " + npc.getIndex());
                    }
                    c.getPA().sendDropTableData("Click HERE to view drops for " + npc.getName(), npc.getIndex());
                }
                break;
        }
    }
}
