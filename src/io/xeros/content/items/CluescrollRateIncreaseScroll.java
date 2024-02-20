package io.xeros.content.items;

import io.xeros.model.entity.player.Player;

import java.util.concurrent.TimeUnit;


public class CluescrollRateIncreaseScroll {

	private static final long TIME = TimeUnit.MINUTES.toMillis(30) / 600;

	private static final long TIMEDROPS = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMEDAMAGE = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMEFIFTYDAMAGE = TimeUnit.MINUTES.toMillis(20) / 600;

	private static final long TIMECOX = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMEDOUBLEDROPS = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMETOMEOFEXPERIENCE = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMETOMEOFPETS = TimeUnit.MINUTES.toMillis(60) / 600;

	private static final long TIMEDOUBLESEASONAL = TimeUnit.MINUTES.toMillis(60) / 600;

	public static void openScroll(Player player) {
		if (player.fasterCluesScroll) {
			player.sendMessage("You already have a bonus skill pet rate going.");
			return;
		}

		player.fasterCluesScroll = true;
		player.fasterCluesTicks = TIME;
	}

	public static void openSeasonalDrops(Player player) {
		if (player.seasonalScrollActive) {
			player.sendMessage("You already have a seasonal scroll active.");
			return;
		}

		player.seasonalScrollActive = true;
		player.seasonalScrollTicks = TIMEDOUBLESEASONAL;
	}

	public static void openScrollDrops(Player player) {
		if (player.dropScrollActive) {
			player.sendMessage("You already have a bonus drop rate scroll active.");
			return;
		}

		player.dropScrollActive = true;
		player.dropScrollTicks = TIMEDROPS;
	}

	public static void openScrollDamage(Player player) {
		if (player.damageScrollActive) {
			player.sendMessage("You already have a bonus damage scroll active.");
			return;
		}

		player.damageScrollActive = true;
		player.damageScrollTicks = TIMEDAMAGE;
	}

	public static void openScrollFiftyDamage(Player player) {
		if (player.fiftyDamageScrollActive) {
			player.sendMessage("You already have a bonus damage scroll active.");
			return;
		}

		player.fiftyDamageScrollActive = true;
		player.fiftyDamageScrollTicks = TIMEFIFTYDAMAGE;
	}

	public static void openTomeOfExperience(Player player) {
		if (player.tomeOfExperienceActive) {
			player.sendMessage("You already have a tome of experience active.");
			return;
		}

		player.tomeOfExperienceActive = true;
		player.tomeOfExperienceTicks = TIMETOMEOFEXPERIENCE;
	}

	public static void openTomeOfPets(Player player) {
		if (player.tomeOfPetsActive) {
			player.sendMessage("You already have a tome of pets active.");
			return;
		}

		player.tomeOfPetsActive = true;
		player.tomeOfPetsTicks = TIMETOMEOFPETS;
	}

	public static void openScrollCox(Player player) {
		if (player.coxScrollActive) {
			player.sendMessage("You already have a cox boost scroll active.");
			return;
		}

		player.coxScrollActive = true;
		player.coxScrollTicks = TIMECOX;
	}

	public static void openScrollDoubleDrops(Player player) {
		if (player.doubleDropScrollActive) {
			player.sendMessage("You already have a double drop chance scroll active.");
			return;
		}

		player.doubleDropScrollActive = true;
		player.doubleDropScrollTicks = TIMEDOUBLEDROPS;
	}
	

}
	
