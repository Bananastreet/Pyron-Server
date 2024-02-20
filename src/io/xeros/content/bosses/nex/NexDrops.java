package io.xeros.content.bosses.nex;

import io.xeros.Server;
import io.xeros.content.combat.death.NPCDeath;
import io.xeros.content.instances.InstancedArea;
import io.xeros.content.wogw.Wogw;
import io.xeros.content.worldevent.events.BossSpotlight;
import io.xeros.model.Items;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.npc.pets.PetHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Position;
import io.xeros.model.entity.player.Right;
import io.xeros.model.items.GameItem;
import io.xeros.util.Misc;

import java.util.concurrent.TimeUnit;

public class NexDrops {

    public static void sendDrops(Player player, InstancedArea instance, Position position) {
        if (player.getAttributes().getInt("nex_damage") < 50) {
            return;
        }

        PetHandler.roll(player, PetHandler.Pets.NEX);
        handleCommonDrops(player, position);
        handleUpgradeTokenDrop(player, position);

        if (Misc.hasOneOutOf(getModifier(player, instance))) {
            handleRareDrops(player, position);
        }

        player.sendMessage("You had a drop rate of 1/" + getModifier(player, instance) + " for this kill. Damage done: " + player.getAttributes().getInt("nex_damage") + ".");
        player.getAttributes().setInt("nex_damage", 0);
    }

    private static void handleUpgradeTokenDrop(Player player, Position position) {
        if (Misc.hasOneOutOf(35)) {
            var drop = new GameItem(23497, 1);
            Server.itemHandler.createGroundItem(player, drop, position, Misc.toCycles(3, TimeUnit.MINUTES));
            PlayerHandler.executeGlobalMessage("@pur@" + player.getDisplayNameFormatted() + " received an upgrade token from Nex.");
        }
    }

    private static void handleCommonDrops(Player player, Position position) {
        var commonDrops = new int[][] { {Items.BLOOD_RUNE, 84, 325}, {Items.DEATH_RUNE, 85, 170}, {Items.SOUL_RUNE, 86, 227},
                {Items.DRAGON_BOLTS_UNF, 12, 90}, {Items.CANNONBALL, 42, 298}, {Items.AIR_RUNE, 123, 1365}, {Items.FIRE_RUNE, 210, 1655},
                {Items.WATER_RUNE, 193, 1599}, {Items.ONYX_BOLTS_E, 11, 29}, {Items.AIR_ORB_NOTED, 6, 20}, {Items.UNCUT_RUBY_NOTED, 3, 26},
                {Items.UNCUT_DIAMOND_NOTED, 3, 17}, {Items.WINE_OF_ZAMORAK_NOTED, 4, 14}, {Items.COAL_NOTED, 23, 95},
                {Items.RUNITE_ORE_NOTED, 2, 28}, {26231, 1, 20}, {19835, 1, 1}, {Items.COINS, 250000, 500000}, {Items.OVERLOAD_4, 1, 3},
                {Items.PRAYER_POTION4_NOTED, 5, 10}, {Items.SARADOMIN_BREW4_NOTED, 5, 10}, {26388, 6, 39}};

        var randomItem = Misc.random(commonDrops.length - 1);
        var item = new GameItem(commonDrops[randomItem][0], Misc.random(commonDrops[randomItem][1], commonDrops[randomItem][2]));
        Server.itemHandler.createGroundItem(player, item, position, Misc.toCycles(3, TimeUnit.MINUTES));
    }

    private static void handleRareDrops(Player player, Position position) {
        var possibleRares = new int[] { 26376, 26378, 26380, 26370, 26372, 26235 };
        var item = new GameItem(possibleRares[Misc.random(possibleRares.length - 1)], 1);
        Server.itemHandler.createGroundItem(player, item, position, Misc.toCycles(3, TimeUnit.MINUTES));
        NPCDeath.announce(player, item, 11278);
    }

    private static double getPercentageDecrease(Player player) {
        var percentageDecrease = 1.0;

        //Game mode (rogue), this is += because it's a negative modifier
        percentageDecrease += player.getMode().getDropModifier();

        //Ring of wealth
        if (player.getItems().isWearingItem(2572)) {
            percentageDecrease -= .03;
        } else if (player.getItems().isWearingItem(12785) ||  player.getItems().isWearingItem(25541)) {
            percentageDecrease -= player.hasPerk(Player.RING_OF_WEALTHIER) ? .10 : .08;
        }

        //Donator ranks
        if (player.getRights().contains(Right.ONYX_DONATOR)) {
            percentageDecrease -= 0.08;
        } else if (player.getRights().contains(Right.DIAMOND_DONATOR)) {
            percentageDecrease -= 0.06;
        } else if (player.getRights().contains(Right.RUBY_DONATOR)) {
            percentageDecrease -= 0.05;
        } else if (player.getRights().contains(Right.EMERALD_DONATOR)) {
            percentageDecrease -= 0.04;
        } else if (player.getRights().contains(Right.SAPPHIRE_DONATOR)) {
            percentageDecrease -= 0.03;
        }

        //Boss spotlight
        if (BossSpotlight.isSpotlightNpc(11278)) {
            percentageDecrease -= 0.05;
        }

        //Sigils
        if (player.getItems().playerHasItem(26017) || player.getItems().playerHasItem(26146)) {
            percentageDecrease -= .03;
        }

        //Well of Goodwill 20%
        if (Wogw._20_PERCENT_DROP_RATE_TIMER > 0) {
            percentageDecrease -= 0.1;
        }

        return percentageDecrease;
    }

    private static int getModifier(Player player, InstancedArea instance) {
        var modifier = 400;

        //Drop percentage decrease, if a player has all boosts (including well and spotlight) this will make the modifier 204
        modifier *= getPercentageDecrease(player);

        //increase base modifier by 20 for teams over 7 players, lower for teams under or equal to 7
        final var teamSize = instance.getPlayers().size();
        final var soloKill = instance.getPlayers().size() == 1;
        if (teamSize > 7) {
            modifier += (teamSize - 7) * 20;
        } else if (soloKill) {
            modifier -= 100;
        } else {
            modifier -= 30;
        }

        //reduce modifier based on damage done, highest damaging player gets -100
        final var damageDone = player.getAttributes().getInt("nex_damage");
        if (getHighestDamage(instance) == player) {
            modifier -= 100;
        } else if (damageDone > 1500) {
            modifier -= 75;
        } else if (damageDone > 1000) {
            modifier -= 50;
        } else if (damageDone > 500) {
            modifier -= 25;
        } else if (damageDone > 250) {
            modifier -= 20;
        } else if (damageDone > 100) {
            modifier -= 10;
        }

        //fail-safe, if solo, limit to 65, otherwise limit to 100
        if (modifier < 65 && soloKill) {
            modifier = 65;
        } else if (modifier < 100) {
            modifier = 100;
        }

        modifier *= 0.7;

        return modifier;
    }

    private static Player getHighestDamage(InstancedArea instance) {
        var highestDamage = 0;
        Player highestPlayer = null;
        for (Player player : instance.getPlayers()) {
            var damage = player.getAttributes().getInt("nex_damage");
            if (damage > highestDamage) {
                highestDamage = damage;
                highestPlayer = player;
            }
        }
        return highestPlayer;
    }

}
