package io.xeros.model.entity.player.packets.itemoptions;

import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.content.DiceHandler;
import io.xeros.content.bosses.Cerberus;
import io.xeros.content.bosses.hespori.*;
import io.xeros.content.bosses.mimic.MimicCasket;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.magic.NonCombatSpellData;
import io.xeros.content.combat.magic.SanguinestiStaff;
import io.xeros.content.dialogue.DialogueBuilder;
import io.xeros.content.dwarfmulticannon.Cannon;
import io.xeros.content.item.lootable.impl.*;
import io.xeros.content.items.BirdNests;
import io.xeros.content.items.CluescrollRateIncreaseScroll;
import io.xeros.content.items.Packs;
import io.xeros.content.items.Starter;
import io.xeros.content.items.pouch.RunePouch;
import io.xeros.content.lootbag.LootingBag;
import io.xeros.content.miniquests.magearenaii.MageArenaII;
import io.xeros.content.skills.DoubleExpScroll;
import io.xeros.content.skills.SkillHandler;
import io.xeros.content.skills.SkillPetRateIncreaseScroll;
import io.xeros.content.skills.hunter.Hunter;
import io.xeros.content.skills.hunter.trap.impl.BirdSnare;
import io.xeros.content.skills.hunter.trap.impl.BoxTrap;
import io.xeros.content.skills.prayer.Bone;
import io.xeros.content.skills.prayer.Prayer;
import io.xeros.content.skills.runecrafting.Pouches;
import io.xeros.content.skills.slayer.SlayerUnlock;
import io.xeros.content.teleportation.TeleportTablets;
import io.xeros.content.trails.TreasureTrails;
import io.xeros.content.trinkets.activateTrinket;
import io.xeros.model.Items;
import io.xeros.model.Npcs;
import io.xeros.model.entity.player.*;
import io.xeros.model.items.ItemAssistant;
import io.xeros.model.multiplayersession.MultiplayerSessionType;
import io.xeros.model.multiplayersession.duel.DuelSession;
import io.xeros.model.multiplayersession.duel.DuelSessionRules.Rule;
import io.xeros.model.multiplayersession.flowerpoker.FlowerData;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;
import io.xeros.util.discord.Discord;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.xeros.content.DiceHandler.DICING_AREA;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ItemOptionOne implements PacketType {

    /**
     * The position the mithril seed was planted
     */
    private static final int[] position = new int[2];

    public void plantMithrilSeed(Player c) {
        position[0] = c.absX;
        position[1] = c.absY;
        GlobalObject object1 = new GlobalObject(FlowerData.getRandomFlower().objectId, position[0], position[1], c.getHeight(), 3, 10, 120, -1);
        Server.getGlobalObjects().add(object1);
        c.getPA().walkTo(1, 0);
        c.facePosition(position[0] - 1, position[1]);
        c.sendMessage("You planted a flower!");
        c.getItems().deleteItem(299, c.getItems().getInventoryItemSlot(299), 1);
    }

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        if (c.getMovementState().isLocked())
            return;
        c.interruptActions();
        int interfaceId = c.getInStream().readUnsignedWord();
        int itemSlot = c.getInStream().readUnsignedWord();
        int itemId = c.getInStream().readUnsignedWord();
        if (c.debugMessage) {
            c.sendMessage(String.format("ItemClick[item=%d, option=%d, interface=%d, slot=%d]", itemId, 1, interfaceId, itemSlot));
        }

        if (itemSlot >= c.playerItems.length || itemSlot < 0) {
            return;
        }
        if (itemId != c.playerItems[itemSlot] - 1) {
            return;
        }
        if (c.isDead || c.getHealth().getCurrentHealth() <= 0) {
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
        c.lastClickedItem = itemId;

        if (c.isFping() && itemId != 299 && itemId != 300) {
            return;
        }

        if (activateTrinket.isTrinket(itemId)) {
            activateTrinket.activateTrinket(itemId, c);
            return;
        }

        c.getHerblore().clean(itemId);
        if (c.getFood().isFood(itemId)) {
            c.getFood().eat(itemId, itemSlot);
            return;
        } else if (c.getPotions().isPotion(itemId)) {
            c.getPotions().handlePotion(itemId, itemSlot);
            return;
        }
        if (c.getQuesting().handleItemClick(itemId)) {
            return;
        }
        Optional<Bone> bone = Prayer.isOperableBone(itemId);
        if (bone.isPresent()) {
            c.getPrayer().bury(bone.get());
            return;
        }
        TeleportTablets.operate(c, itemId);
        Packs.openPack(c, itemId);
        if (LootingBag.isLootingBag(itemId)) {
            c.getLootingBag().toggleOpen();
            return;
        }
        if (RunePouch.isRunePouch(itemId)) {
            c.getRunePouch().openRunePouch();
            return;
        }
        if (Cannon.clickItem(c, itemId)) {
            return;
        }
        if (SanguinestiStaff.clickItem(c, itemId, 1)) {
            return;
        }
        if (TreasureTrails.firstClickItem(c, itemId)) {
            return;
        }
        if (BirdNests.firstClickItem(c, itemId)) {
            return;
        }

        switch (itemId) {

            case 22944:
                if (Configuration.DOUBLE_DROPS_TIMER > 1) {
                    c.sendMessage("Double drops are already active.");
                } else {
                    if (c.getItems().playerHasItem(22944)) {
                        c.getItems().deleteItem(22944, 1);
                        Configuration.DOUBLE_DROPS_TIMER = Misc.toCycles(60, TimeUnit.MINUTES);
                        PlayerHandler.newsMessage(c.getDisplayNameFormatted() + " activated double drops for one hour.");
                    }
                }
                break;

            case 7500:
                int[] commonB = {23497, 6585, 11840, 22415, 22416, 7629, 21046, 6731, 6733, 6735, 6737, 25837};
                int[] uncommonB = {11740, 25087, 25365, 26706, 23804, 29056, 24495, 22106, 19701, 13277, 12936, 6199, 13271};
                int[] rareB = {22946, 22948, 22949, 22950, 10858, 22944};
                int chance = Misc.random(100);

                if (!c.getItems().playerHasItem(7500)) {
                    c.sendMessage("You do not have a boss challenge mystery box to open.");
                } else {
                    c.getItems().deleteItem(7500, 1);

                    int grabItem;
                    String message;

                    if (chance < 75) {
                        grabItem = commonB[new Random().nextInt(commonB.length)];
                        message = "Common";
                    } else if (chance <= 95) {
                        grabItem = uncommonB[new Random().nextInt(uncommonB.length)];
                        message = "Uncommon";
                    } else {
                        grabItem = rareB[new Random().nextInt(rareB.length)];
                        message = "Rare";
                    }

                    String lootName = ItemAssistant.getItemName(grabItem);
                    if (message.toLowerCase().equalsIgnoreCase("rare")) {
                        PlayerHandler.dropMessage(c.getDisplayNameFormatted() + " received a "+ lootName +" from a boss challenge box.");
                    }
                    c.sendMessage(message + " loot: " + lootName + " from the boss mystery box.");
                    c.getItems().addItemUnderAnyCircumstance(grabItem, 1);
                }
                break;

            case 90: // trinket mystery box
                int[] trinkets = {75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 86};
                if (c.getItems().playerHasItem(90)) {
                    c.getItems().deleteItem(90, 1);
                    int randomIndex = new Random().nextInt(trinkets.length);

                    int randomTrinket = trinkets[randomIndex];
                    String trinketName = ItemAssistant.getItemName(randomTrinket);
                    c.sendMessage("You've received a "+ trinketName +" from the mystery box.");

                    c.getItems().addItemUnderAnyCircumstance(randomTrinket, 1);
                } else {
                    c.sendMessage("You do not have a trinket mystery box to open.");
                }
                break;

            case 87:
                if (c.getItems().playerHasItem(87)) {
                    c.getItems().deleteItem(87, 1);
                    new CelastrusBonus().activate(c);
                    new NoxiferBonus().activate(c);
                    new KeldaBonus().activate(c);
                    new BuchuBonus().activate(c);
                    new KronosBonus().activate(c);
                } else {
                    c.sendMessage("You do not currently have a server boost scroll to redeem.");
                }
                break;

            case 2996:
                int ticketsToConvert = c.getItems().getItemAmount(2996);
                if (ticketsToConvert > 0 && c.getItems().playerHasItem(2996, ticketsToConvert)) {
                    c.sendMessage("You have converted " + ticketsToConvert + " pkp tickets into pkp.");
                    c.getItems().deleteItem(2996, ticketsToConvert);
                    c.pkp += ticketsToConvert;
                }
                break;
            
            case 7478:
                c.getShops().openShop(9);
              break;
                case 7774:
                c.getShops().openShop(199);
                break;
            case 6855:
                if (c.getItems().playerHasItem(6855)) {
                    c.getItems().deleteItem(6855, 1);
                    c.getItems().addItemUnderAnyCircumstance(3700, 1);
                    c.getItems().addItemUnderAnyCircumstance(3701, 1);
                    c.getItems().addItemUnderAnyCircumstance(3702, 1);
                    c.getItems().addItemUnderAnyCircumstance(3703, 1);
                    c.getItems().addItemUnderAnyCircumstance(3704, 1);
                    c.getItems().addItemUnderAnyCircumstance(3705, 1);
                }
                break;
            case 13173:
                if (c.getItems().playerHasItem(13173)) {
                    c.getItems().deleteItem(13173, 1);
                    c.getItems().addItem(1038, 1);
                    c.getItems().addItem(1040, 1);
                    c.getItems().addItem(1042, 1);
                    c.getItems().addItem(1044, 1);
                    c.getItems().addItem(1046, 1);
                    c.getItems().addItem(1048, 1);
                }
                break;
            case 26388: //Ecumenical key shard
                if (c.getItems().playerHasItem(26388, 50)) {
                    c.getItems().deleteItem(26388, 50);
                    c.getItems().addItem(Items.ECUMENICAL_KEY, 1);
                    c.sendMessage("You combine 50 Ecumenical key shards to form an Ecumenical key.");
                } else {
                    c.sendMessage("You need 50 Ecumenical key shards to form an Ecumenical key.");
                }
                break;
            case 23947:
                if (c.getItems().playerHasItem(23947)) {
                    PlayerAssistant.ringOfCharosTeleport(c);
                }
                break;
            case 26500:
                if (c.getItems().playerHasItem(26500)) {
                    c.getItems().deleteItem(26500, 1);
                    PlayerAssistant.ringOfCharosTeleport(c);
                }
                break;
            case Items.STARDUST:
                c.getShops().openShop(110);
                break;
            case MageArenaII.SYMBOL_ID:
                MageArenaII.handleEnchantedSymbol(c);
                return;
            case Items.MIMIC:
                MimicCasket.open(c);
                break;
            case 21730:
                c.sendMessage("Fallen from the centre of a Grotesque Guardian. This could be attached");
                c.sendMessage("to a pair of Bandos boots...");
                break;
            case 23517:
                if (c.getItems().playerHasItem(946)) {
                    c.getItems().deleteItem(23517, 1);
                    c.getItems().addItem(224, 100);
                } else {
                    c.sendMessage("You need a knife to open this.");
                }
                break;
            case ResourceBoxSmall.BOX_ITEM:
                new ResourceBoxSmall().roll(c);
                break;
            case ResourceBoxMedium.BOX_ITEM:
                new ResourceBoxMedium().roll(c);
                break;
            case ResourceBoxLarge.BOX_ITEM:
                new ResourceBoxLarge().roll(c);
                break;
            case 21034:
                c.getDH().sendDialogues(345, 9120);
                break;
            case 21079:
                c.getDH().sendDialogues(347, 9120);
                break;
            case 22477:
                c.sendMessage("Attach it onto a dragon defender to make avernic defender.");
                break;

            case 23185:
                if (!c.getPA().morphPermissions()) {
                    return;
                }
                for (int i = 0; i <= 12; i++) {
                    c.setSidebarInterface(i, 6014);
                }
                c.npcId2 = 9415;
                c.isNpc = true;
                c.playerStandIndex = -1;
                c.setUpdateRequired(true);
                c.morphed = true;
                c.setAppearanceUpdateRequired(true);
                break;
            case 19564:
                if (c.wildLevel > 30) {
                    c.sendMessage("You can't teleport above level 30 in the wilderness.");
                    return;
                }
                c.getPA().startTeleport(Configuration.START_LOCATION_X, Configuration.START_LOCATION_Y, 0, "pod", false);
                break;
            case 13188:
                c.startAnimation(7514);
                c.gfx0(1282);
                break;
            case 2841:
                if (!c.getItems().playerHasItem(2841)) {
                    c.sendMessage("You need an Bonus XP Scroll to do this!");
                    return;
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                        || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    c.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, no need to use that now!");
                    return;
                } else if (c.xpScroll == false && c.getItems().playerHasItem(2841)) {
                    c.getItems().deleteItem(2841, 1);
                    DoubleExpScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 1 hour of bonus experience.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, 60);
                    c.getQuestTab().updateInformationTab();
                } else if (c.xpScroll == true) {
                    c.sendMessage("@red@You already used this up.");
                }
                break;
            case 7968:
                if (!c.getItems().playerHasItem(7968)) {
                    c.sendMessage("You need an Bonus Pet Scroll to do this!");
                    return;
                }
                if (c.skillingPetRateScroll == false && c.getItems().playerHasItem(7968)) {
                    c.getItems().deleteItem(7968, 1);
                    SkillPetRateIncreaseScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 30 minutes of bonus skilling pet rate increase.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_SKILLING_PET_RATE, TimeUnit.MINUTES, 30);
                } else if (c.skillingPetRateScroll == true) {
                    c.sendMessage("@red@You already have a pet bonus going.");
                }
                break;
            case 25087:
                if (!c.getItems().playerHasItem(25087)) {
                    c.sendMessage("You need a double drop scroll chance scroll to activate.");
                    return;
                }
                if (c.doubleDropScrollActive == false && c.getItems().playerHasItem(25087)) {
                    c.getItems().deleteItem(25087, 1);
                    CluescrollRateIncreaseScroll.openScrollDoubleDrops(c);
                    c.sendMessage("@red@You have activated 1 hour of 20% double drop chance.");
                } else if (c.doubleDropScrollActive == true) {
                    c.sendMessage("@red@You already have a double drop scroll.");
                }
                break;
            case 19837:
                if (!c.getItems().playerHasItem(19837)) {
                    c.sendMessage("You need a 20 minute 50% damage scroll to activate.");
                    return;
                }
                if (c.fiftyDamageScrollActive == false && c.getItems().playerHasItem(19837)) {
                    c.getItems().deleteItem(19837, 1);
                    CluescrollRateIncreaseScroll.openScrollFiftyDamage(c);
                    c.sendMessage("@red@You have activated 20 minutes of 50% bonus damage.");
                } else if (c.fiftyDamageScrollActive == true) {
                    c.sendMessage("@red@You already have a damage scroll actived.");
                }
                break;
            case 22415:
                if (!c.getItems().playerHasItem(22415)) {
                    c.sendMessage("You need a tome of experience to activate.");
                    return;
                }
                if (c.tomeOfExperienceActive == false && c.getItems().playerHasItem(22415)) {
                    c.getItems().deleteItem(22415, 1);
                    CluescrollRateIncreaseScroll.openTomeOfExperience(c);
                    c.sendMessage("@red@You have activated 1 hour of double experience.");
                } else if (c.tomeOfExperienceActive == true) {
                    c.sendMessage("@red@You already have a tome of experience actived.");
                }
                break;
            case 22416:
                if (!c.getItems().playerHasItem(22416)) {
                    c.sendMessage("You need a tome of pets to activate.");
                    return;
                }
                if (c.tomeOfPetsActive == false && c.getItems().playerHasItem(22416)) {
                    c.getItems().deleteItem(22416, 1);
                    CluescrollRateIncreaseScroll.openTomeOfPets(c);
                    c.sendMessage("@red@You have activated 1 hour of boosted pet chance");
                } else if (c.tomeOfPetsActive == true) {
                    c.sendMessage("@red@You already have a tome of pets actived.");
                }
                break;
            case 26706:
                if (!c.getItems().playerHasItem(26706)) {
                    c.sendMessage("You need a 1 hour 10% damage scroll to activate.");
                    return;
                }
                if (c.damageScrollActive == false && c.getItems().playerHasItem(26706)) {
                    c.getItems().deleteItem(26706, 1);
                    CluescrollRateIncreaseScroll.openScrollDamage(c);
                    c.sendMessage("@red@You have activated 1 hour of 10% bonus damage.");
                } else if (c.damageScrollActive == true) {
                    c.sendMessage("@red@You already have a damage scroll actived.");
                }
                break;
            case 25365:
                if (!c.getItems().playerHasItem(25365)) {
                    c.sendMessage("You need a 1 hour 10% drop boost scroll to activate.");
                    return;
                }
                if (c.coxScrollActive == false && c.getItems().playerHasItem(25365)) {
                    c.getItems().deleteItem(25365, 1);
                    CluescrollRateIncreaseScroll.openScrollCox(c);
                    c.sendMessage("@red@You have activated 1 hour of boosted CoX rare key chance.");
                    //c.getPA().sendGameTimer(ClientGameTimer.DROPS, TimeUnit.MINUTES, 60);
                } else if (c.coxScrollActive == true) {
                    c.sendMessage("@red@You already have a raids boost scroll actived.");
                }
                break;
            case 29056:
                if (!c.getItems().playerHasItem(29056)) {
                    c.sendMessage("You need a 1 hour seasonal scroll to activate.");
                    return;
                }
                if (c.seasonalScrollActive == false && c.getItems().playerHasItem(29056)) {
                    c.getItems().deleteItem(29056, 1);
                    CluescrollRateIncreaseScroll.openSeasonalDrops(c);
                    c.sendMessage("@red@You have activated 1 hour of double seasonal tickets.");
                } else if (c.seasonalScrollActive == true) {
                    c.sendMessage("@red@You already have a seasonal scroll active.");
                }
                break;
            case 11740:
                if (!c.getItems().playerHasItem(11740)) {
                    c.sendMessage("You need a 1 hour 10% drop boost scroll to activate.");
                    return;
                }
                if (c.dropScrollActive == false && c.getItems().playerHasItem(11740)) {
                    c.getItems().deleteItem(11740, 1);
                    CluescrollRateIncreaseScroll.openScrollDrops(c);
                    c.sendMessage("@red@You have activated 1 hour of 10% bonus drop rate.");
                    //c.getPA().sendGameTimer(ClientGameTimer.DROPS, TimeUnit.MINUTES, 60);
                } else if (c.dropScrollActive == true) {
                    c.sendMessage("@red@You already have a drop rate scroll actived.");
                }
                break;
            case 24460:
                if (!c.getItems().playerHasItem(24460)) {
                    c.sendMessage("You need a Faster clues scroll to do this!");
                    return;
                }
                if (c.fasterCluesScroll == false && c.getItems().playerHasItem(24460)) {
                    c.getItems().deleteItem(24460, 1);
                    CluescrollRateIncreaseScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 30 minutes of bonus clues for pvm and skilling.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_CLUES, TimeUnit.MINUTES, 30);
                } else if (c.fasterCluesScroll == true) {
                    c.sendMessage("@red@You already have a faster clue rate going.");
                }
                break;

            case 12885:
            case 13277:
            case 19701:
            case 13245:
            case 12007:
            case 22106:
            case 12936:
            case 24495:
                c.getDH().sendDialogues(361, Npcs.BOSS_POINT_SHOP);
                break;

            case 6830:
                c.getItems().deleteItem(6830, 1);
                c.gfx100(263);
                Starter.addStarterItems(c);
                break;

            case 6829://yt video giveaway box tier 1
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6829, 1);
                    c.gfx100(263);
                    c.getItems().addItem(6828, 1);//super m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    Discord.writeServerSyncMessage("[YOUTUBE VIDEO BOX T1] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a video box t1!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;
            case 6831://yt video giveaway box tier2
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6831, 1);
                    c.gfx100(263);
                    c.getItems().addItem(13346, 1);//ultra m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    Discord.writeServerSyncMessage("[YOUTUBE VIDEO BOX T2] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a video box t2!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;

            case 6832://yt stream giveaway box tier 1
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6832, 1);
                    c.gfx100(263);
                    c.getItems().addItem(12789, 2);//refer box
                    c.getItems().addItem(6199, 1);//normal m box
                    c.getItems().addItem(6828, 1);//super m box
                    c.getItems().addItem(13346, 1);//ultra m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    Discord.writeServerSyncMessage("[YOUTUBE STREAM BOX T1] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a stream box t1!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;
            case 6833://yt stream giveaway box tier2
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6833, 1);
                    c.gfx100(263);
                    c.getItems().addItem(6199, 2);//normal m box
                    c.getItems().addItem(6828, 2);//super m box
                    c.getItems().addItem(13346, 2);//ultra m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    Discord.writeServerSyncMessage("[YOUTUBE STREAM BOX T2] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a stream box t2!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;
            case 1004:
                if (c.getItems().playerHasItem(1004)) {
                    c.getItems().deleteItem(1004, 1);
                    c.getItems().addItem(995, 20000000);
                    c.sendMessage("You claim the coin stack and it turns into 20m gp.");
                }
                break;
            case 8899:
                if (c.getItems().playerHasItem(8899)) {
                    c.getItems().deleteItem(8899, 1);
                    c.getItems().addItem(995, 50000000);
                    c.sendMessage("You claim the coin stack and it turns into 50m gp.");
                }
                break;
            case 6121:
                if (c.barbarian == false && c.getItems().playerHasItem(6121)) {
                    c.getItems().deleteItem(6121, 1);
                    c.barbarian = true;
                    c.breakVials = true;
                    c.getDH().sendStatement("You may now use ::vials to turn off and on vial smashing!", "It is now set to on.");

                } else if (c.barbarian == true) {
                    c.sendMessage("You already learned how to do this");
                }
                break;
            case 299:
            case 300:
                if (Boundary.isIn(c, Boundary.FLOWER_POKER_AREA)) {
                    if (c.isFping())
                        c.getFlowerPoker().plantSeed(c, true, false);
                    return;
                }
                if (c.getRegionProvider().getClipping(c.absX, c.absY, c.heightLevel) != 0
                        || Server.getGlobalObjects().anyExists(c.absX, c.absY, c.heightLevel)) {
                    c.sendMessage("You cannot plant a flower here.");
                    return;
                }
                if (!c.getItems().playerHasItem(15098, 1)) {
                    c.sendMessage("You need to purchase a dice bag to use mithril seeds.");
                    return;
                }
                if (!Boundary.isIn(c, DICING_AREA)) {
                    c.sendMessage("You must be in the dice area to plant mithril seeds.");
                    return;
                }
                if (System.currentTimeMillis() - c.lastPlant < 250) {
                    return;
                }
                c.lastPlant = System.currentTimeMillis();
                plantMithrilSeed(c);
                break;
            case 13438: //slayer m chest
                if (c.getItems().playerHasItem(13438)) {
                    new SlayerMysteryBox(c).quickOpen();
                }
                break;
            case 21027: //dark relic
                c.inLamp = true;
                c.usingLamp = true;
                c.normalLamp = true;
                c.antiqueLamp = false;
                c.sendMessage("You rub the lamp...");
                c.getPA().showInterface(2808);
                break;

            case 13148:
                c.start(new DialogueBuilder(c).statement("This lamp will reset a skill of your choice.").exit(plr -> {
                    c.inLamp = true;
                    c.usingLamp = true;
                    c.normalLamp = true;
                    c.antiqueLamp = false;
                    c.sendMessage("You have rubbed the skill reset lamp.");
                    c.getPA().showInterface(2808);
                }));
                break;
            case 12789:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(12789)) {
                    c.inDonatorBox = true;
                    c.getYoutubeMysteryBox().openInterface();
                    return;
                }
                break;
            case 10560:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(10560)) {
                    c.inDonatorBox = true;
                    c.getPVPMysteryBox().openInterface();
                    return;
                }
                break;
            case 10561:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(10561)) {
                    c.inDonatorBox = true;
                    c.getRaresMysteryBox().openInterface();
                    return;
                }
                break;
            case 10563:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(10563)) {
                    c.inDonatorBox = true;
                    c.getGodwarsMysteryBox().openInterface();
                    return;
                }
                break;
            case 13346:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(13346)) {
                    c.inDonatorBox = true;
                    c.getUltraMysteryBox().openInterface();
                    return;
                }
                break;
            case 6199:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(6199)) {
                    c.getNormalMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 6828:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(6828)) {
                    c.getSuperMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 6834:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(6834)) {
                    c.getPetMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 8167:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getGodwarsMysteryBox().canMysteryBox) || !(c.getRaresMysteryBox().canMysteryBox) || !(c.getPVPMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getPetMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else if (c.getItems().playerHasItem(8167)) {
                    c.getFoeMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 21347:
                c.boltTips = true;
                c.arrowTips = false;
                c.javelinHeads = false;
                c.sendMessage("Your Amethyst method is now Bolt Tips!");
                break;
            case 20724:
                DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(session)) {
                    if (session.getRules().contains(Rule.NO_DRINKS)) {
                        c.sendMessage("Using the imbued heart with 'No Drinks' option is forbidden.");
                        return;
                    }
                }
                if (System.currentTimeMillis() - c.lastHeart < 420000) {
                    c.sendMessage("You must wait 7 minutes between each use.");
                } else {
                    c.getPA().imbuedHeart();
                    c.lastHeart = System.currentTimeMillis();
                }
                break;
            case PvmCasket.PVM_CASKET: //Pvm Casket
                if (System.currentTimeMillis() - c.openCasketTimer > 350) {
                    if (c.getItems().playerHasItem(405)) {
                        c.getPvmCasket().roll(c);
                        c.openCasketTimer = System.currentTimeMillis();
                    }
                }
                break;
            case Items.DWARF_CANNON_SET:
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(Items.DWARF_CANNON_SET, 1)) {
                    c.getItems().deleteItem(Items.DWARF_CANNON_SET, 1);
                    c.getItems().addItem(Items.CANNON_BASE, 1);
                    c.getItems().addItem(Items.CANNON_STAND, 1);
                    c.getItems().addItem(Items.CANNON_BARRELS, 1);
                    c.getItems().addItem(Items.CANNON_FURNACE, 1);
                }
                break;
            case 11666://full void token
                if (c.getItems().freeSlots() < 6) {
                    c.sendMessage("You need at least 6 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(11666)) {
                    c.getItems().deleteItem(11666, 1);
                    c.getItems().addItem(Items.ELITE_VOID_ROBE, 1);
                    c.getItems().addItem(Items.ELITE_VOID_TOP, 1);
                    c.getItems().addItem(Items.VOID_KNIGHT_GLOVES, 1);
                    c.getItems().addItem(Items.VOID_RANGER_HELM, 1);
                    c.getItems().addItem(Items.VOID_MAGE_HELM, 1);
                    c.getItems().addItem(Items.VOID_MELEE_HELM, 1);
                }
                break;
            case 12873://guthan
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12873, 1)) {
                    c.getItems().addItem(4724, 1);
                    c.getItems().addItem(4726, 1);
                    c.getItems().addItem(4728, 1);
                    c.getItems().addItem(4730, 1);
                }
                c.getItems().deleteItem(12873, 1);
                break;
            case 12875://verac
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12875, 1)) {
                    c.getItems().addItem(4753, 1);
                    c.getItems().addItem(4755, 1);
                    c.getItems().addItem(4757, 1);
                    c.getItems().addItem(4759, 1);
                }
                c.getItems().deleteItem(12875, 1);
                break;
            case 12877://dharok
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12877, 1)) {
                    c.getItems().addItem(4716, 1);
                    c.getItems().addItem(4718, 1);
                    c.getItems().addItem(4720, 1);
                    c.getItems().addItem(4722, 1);
                }
                c.getItems().deleteItem(12877, 1);
                break;
            case 12879://torags
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12879, 1)) {
                    c.getItems().addItem(4745, 1);
                    c.getItems().addItem(4747, 1);
                    c.getItems().addItem(4749, 1);
                    c.getItems().addItem(4751, 1);
                }
                c.getItems().deleteItem(12879, 1);
                break;
            case 12881://ahrims
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12881, 1)) {
                    c.getItems().addItem(4708, 1);
                    c.getItems().addItem(4710, 1);
                    c.getItems().addItem(4712, 1);
                    c.getItems().addItem(4714, 1);
                }
                c.getItems().deleteItem(12881, 1);
                break;
            case 12883://karils
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12883, 1)) {
                    c.getItems().addItem(4732, 1);
                    c.getItems().addItem(4734, 1);
                    c.getItems().addItem(4736, 1);
                    c.getItems().addItem(4738, 1);
                }
                c.getItems().deleteItem(12883, 1);
                break;
            case 10006:
                Hunter.lay(c, new BirdSnare(c));
                break;
            case 10008:
                Hunter.lay(c, new BoxTrap(c));
                break;
            case 13249:
                if (Boundary.isIn(c, Boundary.OUTLAST_HUT)) {
                    c.sendMessage("Please leave the outlast hut area to teleport.");
                    return;
                }
                if (Boundary.isIn(c, Boundary.OUTLAST)) {
                    c.sendMessage("Please leave the outlast hut area to teleport.");
                    return;
                }
                if (Boundary.isIn(c, Boundary.WILDERNESS_PARAMETERS)) {
                    c.sendMessage("Please leave the wild.");
                    return;
                }
                if (!Boundary.isIn(c, Boundary.EDGEVILLE_PERIMETER)) {
                    c.sendMessage("Please use this in edgeville.");
                    return;
                }
                c.isWc = false;
                if (c.isOverloading) {
                    c.sendMessage("You cannot teleport while taking overload damage.");
                    return;
                }
                if (c.isFping()) {
                    /**
                     * Cannot do action while fping
                     */
                    c.sendMessage("You cannot teleport while fping.");

                    return;
                }

                if (c.stopPlayerSkill) {
                    SkillHandler.resetPlayerSkillVariables(c);
                    c.stopPlayerSkill = false;
                }
                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && (!c.getSlayer().getTask().get().getPrimaryName().equals("hellhound") && !c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")))) {
                    c.sendMessage("You must have an active cerberus or hellhound task to use this.");
                    return;
                }
                c.getItems().deleteItem(13249, 1);

                if (!c.getSlayer().isCerberusRoute()) {
                    c.sendMessage("You have bought Route into cerberus cave. please wait till you will be teleported.");
                    Cerberus.init(c);
                    c.cerbDelay = System.currentTimeMillis();
                    return;
                }

                if (Server.getEventHandler().isRunning(c, "cerb")) {
                    c.sendMessage("You're about to fight start the fight, please wait.");
                    return;
                }
                Cerberus.init(c);
                break;

            case 13226:
                c.getHerbSack().fillSack();
                break;

            case 12020:
                c.getGemBag().fillBag();
                break;

            case 5509:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 0);
                break;
            case 5510:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 1);
                break;
            case 5512:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 2);
                break;
            case 26784:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 3);
                break;


            /*case 952: //Spade
                int x = c.absX;
                int y = c.absY;
                if (Boundary.isIn(c, Barrows.GRAVEYARD)) {
                    c.getBarrows().digDown();
                }
                if (x == 3005 && y == 3376 || x == 2999 && y == 3375 || x == 2996 && y == 3377) {
                    if (!c.getRechargeItems().hasItem(13120)) {
                        c.sendMessage("You must have the elite falador shield to do this.");
                        return;
                    }
                    c.getPA().movePlayer(1760, 5163, 0);
                }
                break;*/
        }

        if (itemId == 2678) {
            c.getDH().sendDialogues(657, -1);
            return;
        }
        if (itemId == 8015 || itemId == 8014) {
            NonCombatSpellData.attemptDate(c, itemId);
        }
        if (itemId == 9553) {
            c.getPotions().eatChoc(itemSlot);
        }
        if (itemId == 12846) {
            c.getDH().sendDialogues(578, -1);
        }
        if (itemId == 12938) {
            c.getItems().deleteItem(12938, 1);
            c.getPA().startTeleport(2205, 3056, 0, "modern", false);
            return;
        }
        if (itemId == 4155) {
            if (!c.getSlayer().getTask().isPresent()) {
                c.sendMessage("You do not have a task, please talk with a slayer master!");
                return;
            }
            c.sendMessage("I currently have " + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + "'s to kill.");
            c.getPA().closeAllWindows();
        }
        if (itemId == 2839) {
            if (c.getSlayer().getUnlocks().contains(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
                c.sendMessage("You have already learned this recipe. You have no more use for this scroll.");
                return;
            }
            if (c.getItems().playerHasItem(2839)) {
                c.getSlayer().getUnlocks().add(SlayerUnlock.MALEVOLENT_MASQUERADE);
                c.sendMessage("You have learned the slayer helmet recipe. You can now assemble it");
                c.sendMessage("using a Black Mask, Facemask, Nose peg, Spiny helmet and Earmuffs.");
                c.getItems().deleteItem(2839, 1);
            }
        }
        if (itemId == 15098) {
            DiceHandler.rollDice(c);
        }
        if (itemId == 7509) {
            if (c.getPosition().inDuelArena() || Boundary.isIn(c, Boundary.DUEL_ARENA)) {
                c.sendMessage("You cannot do this here.");
                return;
            }
            if (c.getHealth().getStatus().isPoisoned() || c.getHealth().getStatus().isVenomed()) {
                c.sendMessage("You are effected by venom or poison, you should cure this first.");
                return;
            }
            if (c.getHealth().getCurrentHealth() <= 1) {
                c.sendMessage("I better not do that.");
                return;
            }
            c.forcedChat("Ow! I nearly broke a tooth!");
            c.startAnimation(829);
            // c.getHealth().reduce(1);
            c.appendDamage(1, Hitmark.HIT);
            c.getPA().sendSound(1018);
            return;
        }
        if (itemId == 10269) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10269, 1)) {
                c.getItems().addItem(995, 30000);
                c.getItems().deleteItem(10269, 1);
            }
        }
        if (itemId == 10271) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10271, 1)) {
                c.getItems().addItem(995, 10000);
                c.getItems().deleteItem(10271, 1);
            }
        }
        if (itemId == 10273) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10273, 1)) {
                c.getItems().addItem(995, 14000);
                c.getItems().deleteItem(10273, 1);
            }
        }
        if (itemId == 10275) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10275, 1)) {
                c.getItems().addItem(995, 18000);
                c.getItems().deleteItem(10275, 1);
            }
        }
        if (itemId == 10277) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10277, 1)) {
                c.getItems().addItem(995, 22000);
                c.getItems().deleteItem(10277, 1);
            }
        }
        if (itemId == 10279) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10279, 1)) {
                c.getItems().addItem(995, 26000);
                c.getItems().deleteItem(10279, 1);
            }
        }

        /*Coin Bags */
        if (itemId == 10832)
            if (c.getItems().playerHasItem(10832)) {
                c.getCoinBagSmall().open();
                return;
            }
        if (itemId == 10833)
            if (c.getItems().playerHasItem(10833)) {
                c.getCoinBagMedium().open();
                return;
            }
        if (itemId == 10834)
            if (c.getItems().playerHasItem(10834)) {
                c.getCoinBagLarge().open();
                return;
            }
        if (itemId == 10835)
            if (c.getItems().playerHasItem(10835)) {
                c.getCoinBagBuldging().open();
                return;
            }
        if (itemId == 11739)
            if (c.getItems().playerHasItem(11739)) {
                c.getVoteMysteryBox().roll(c);
                return;
            }

        if (itemId == 20703) //Daily Gear Box
            if (c.getItems().playerHasItem(20703)) {
                c.getDailyGearBox().open();
                return;
            }
        if (itemId == 20791) //Daily Skilling Box
            if (c.getItems().playerHasItem(20791)) {
                c.getDailySkillBox().open();
                return;
            }
		/*if (itemId == 7310) //Skill Casket
			if (c.getItems().playerHasItem(7310)) {
				c.getSkillCasket().open();
				return;
			}*/

        if (itemId == 2528) {
            c.inLamp = true;
            c.usingLamp = true;
            c.normalLamp = true;
            c.antiqueLamp = false;
            c.sendMessage("You rub the lamp...");
            c.getPA().showInterface(2808);
        }

    }
}