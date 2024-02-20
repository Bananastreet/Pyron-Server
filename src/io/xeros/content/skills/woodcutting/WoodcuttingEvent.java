package io.xeros.content.skills.woodcutting;

import java.util.Optional;

import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.content.SkillcapePerks;
import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.achievement_diary.impl.DesertDiaryEntry;
import io.xeros.content.achievement_diary.impl.FaladorDiaryEntry;
import io.xeros.content.achievement_diary.impl.FremennikDiaryEntry;
import io.xeros.content.achievement_diary.impl.KandarinDiaryEntry;
import io.xeros.content.achievement_diary.impl.LumbridgeDraynorDiaryEntry;
import io.xeros.content.achievement_diary.impl.VarrockDiaryEntry;
import io.xeros.content.achievement_diary.impl.WildernessDiaryEntry;
import io.xeros.content.bosses.hespori.Hespori;
import io.xeros.content.event.eventcalendar.EventChallenge;
import io.xeros.content.skills.Skill;
import io.xeros.content.skills.firemake.Firemaking;
import io.xeros.model.Items;
import io.xeros.model.collisionmap.WorldObject;
import io.xeros.model.cycleevent.Event;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Position;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;

public class WoodcuttingEvent extends Event<Player> {
	private static boolean woodcuttingTree;
	private final Tree tree;
	private final Hatchet hatchet;
	private final int objectId;
	private final int x;
	private final int y;
	private int chops;
	
	private final int[] lumberjackOutfit = { 10933, 10939, 10940, 10941 };

	public WoodcuttingEvent(Player player, Tree tree, Hatchet hatchet, int objectId, int x, int y) {
		super("skilling", player, 1);
		this.tree = tree;
		this.hatchet = hatchet;
		this.objectId = objectId;
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		double osrsExperience;
		double experience;
		int pieces = 0;
		pieces=handleOutfit(pieces);
		osrsExperience = tree.getExperience() + tree.getExperience() / 10 * pieces;
		if (canChop()) return;
		if (Misc.random(300) == 0 && attachment.getInterfaceEvent().isExecutable()) {
			attachment.getInterfaceEvent().execute();
			super.stop();
			return;
		}
		chops++;
		int chopChance = 1 + (int) (tree.getChopsRequired() * hatchet.getChopSpeed());
		if (Boundary.isIn(attachment, Boundary.WOODCUTTING_GUILD_BOUNDARY)){
			chopChance *= 1.5;
		}
		if (tree.equals(Tree.HESPORI)) {
			int randomTele = 1;
			if (attachment.getItems().playerHasItem(Hespori.KEY)) {
				attachment.moveTo(new Position(3072 + randomTele, 3505 + randomTele));
				Hespori.deleteEventItems(attachment);
				return;
			}
			int randomTele2 = Misc.random(2);
			attachment.canLeaveHespori = true;
			attachment.moveTo(new Position(3072 + randomTele2, 3505 + randomTele2, 0));
			//attachment.getPA().teleport(3072 + randomTele2, 3505 + randomTele2, 0, "modern",false);
			attachment.getItems().addItem(tree.getWood(), 1);
			if ((Configuration.DOUBLE_DROPS_TIMER > 0 || Configuration.DOUBLE_DROPS) && Misc.random(2) == 1) {
				attachment.getItems().addItem(tree.getWood(), 1);
			}
			attachment.getPA().addSkillXPMultiplied((int)osrsExperience, Skill.WOODCUTTING.getId(), true);
			handleRewards();
			Hespori.deleteEventItems(attachment);
			if (!attachment.getMode().isOsrs() && !attachment.getMode().is5x()) {
				attachment.getPA().addSkillXP(60000 , 19, true);
			} else {
				attachment.getPA().addSkillXP(3300 , 19, true);
			}
			super.stop();
			return;
		}
		if (Misc.random(chopChance) == 0 || chops >= tree.getChopsRequired()) {
			chops = 0;
			int random = Misc.random(4);
			attachment.getPA().addSkillXPMultiplied((int) osrsExperience, Skill.WOODCUTTING.getId(), true);
			Achievements.increase(attachment, AchievementType.WOODCUT, 1);

			handleDiary(tree);
			handleWildernessRewards();
			handleRewards();
			if (attachment.hasPerk(Player.PYRO_LUMBERJACK) && Misc.hasOneOutOf(2) && !Boundary.HESPORI.in(attachment)) {
				Firemaking.lightFire(attachment, tree.getWood(), "infernal_axe");
				return;
			}
			if ((attachment.getItems().isWearingItem(13241) || attachment.getItems().playerHasItem(13241)) && random == 2) {
				if (!Boundary.HESPORI.in(attachment)) {
					Firemaking.lightFire(attachment, tree.getWood(), "infernal_axe");
					return;
				}
			}
			if ((SkillcapePerks.WOODCUTTING.isWearing(attachment) || SkillcapePerks.isWearingMaxCape(attachment)) && attachment.getItems().freeSlots() < 2) {
				attachment.sendMessage("You have run out of free inventory space.");
				super.stop();
				return;
			}
			attachment.getItems().addItem(tree.getWood(), SkillcapePerks.WOODCUTTING.isWearing(attachment) || SkillcapePerks.isWearingMaxCape(attachment) ? 2 : 1);
		}
		if (super.getElapsedTicks() % 4 == 0) {
			attachment.startAnimation(hatchet.getAnimation());
		}
	}

	private int handleOutfit(int pieces) {

		for (int aLumberjackOutfit : lumberjackOutfit) {
			if (attachment.getItems().isWearingItem(aLumberjackOutfit)) {
				pieces+=2;
			}
		}
		return pieces;
	}

	private boolean canChop() {

		if (attachment == null || attachment.isDisconnected() || attachment.getSession() == null) {
			super.stop();
			return true;
		}
		if (!attachment.getItems().playerHasItem(hatchet.getItemId()) && !attachment.getItems().isWearingItem(hatchet.getItemId())) {
			attachment.sendMessage("Your axe has disappeared.");
			super.stop();
			return true;
		}
		if (attachment.playerLevel[Player.playerWoodcutting] < hatchet.getLevelRequired()) {
			attachment.sendMessage("You no longer have the level required to operate this hatchet.");
			super.stop();
			return true;
		}
		if (attachment.getItems().freeSlots() == 0) {
			attachment.sendMessage("You have run out of free inventory space.");
			super.stop();
			return true;
		}
		return false;
	}

	private void handleWildernessRewards() {

		if (Boundary.isIn(attachment, Boundary.RESOURCE_AREA)) {
			if (Misc.random(20) == 5) {
				int randomAmount = 1;
				attachment.sendMessage("You received " + randomAmount + " pkp while woodcutting!");
				attachment.getItems().addItem(2996, randomAmount);
			}
		}
	}

	private void handleDiary(Tree tree) {
		switch (tree) {
			case MAGIC:
				attachment.getEventCalendar().progress(EventChallenge.CUT_DOWN_X_MAGIC_LOGS, 2);
				if (Boundary.isIn(attachment, Boundary.AL_KHARID_BOUNDARY)) {
					attachment.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.CHOP_MAGIC_AL);
				}
				if (Boundary.isIn(attachment, Boundary.RESOURCE_AREA_BOUNDARY)) {
					attachment.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.MAGIC_LOG_WILD);
				}
				if (Boundary.isIn(attachment, Boundary.SEERS_BOUNDARY)) {
					attachment.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CUT_MAGIC_SEERS);
				}
				break;
			case MAPLE:
				break;
			case NORMAL:
				break;
			case OAK:
				if (Boundary.isIn(attachment, Boundary.LUMRIDGE_BOUNDARY) || Boundary.isIn(attachment, Boundary.DRAYNOR_BOUNDARY)) {
					attachment.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.CHOP_OAK);
				}
				if (Boundary.isIn(attachment, Boundary.RELLEKKA_BOUNDARY)) {
					attachment.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.CHOP_OAK_FREM);
				}
				break;
			case WILLOW:
				if (Boundary.isIn(attachment, Boundary.FALADOR_BOUNDARY)) {
					attachment.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.CHOP_WILLOW);
				}
				if (Boundary.isIn(attachment, Boundary.DRAYNOR_BOUNDARY)) {
					attachment.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.CHOP_WILLOW_DRAY);
				}
				break;
			case YEW:
				if (Boundary.isIn(attachment, Boundary.FALADOR_BOUNDARY)) {
					attachment.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.CHOP_YEW);
				}
				if (Boundary.isIn(attachment, Boundary.VARROCK_BOUNDARY)) {
					attachment.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.YEWS_AND_BURN);
				}
				break;
			case TEAK:
				if (Boundary.isIn(attachment, Boundary.DESERT_BOUNDARY)) {
					attachment.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.CHOP_TEAK);
				}
				break;
			default:
				break;

		}
	}

	private void handleRewards() {
		int dropRate = 10;
		int clueAmount = 1;
		if (attachment.fasterCluesScroll) {
			dropRate = dropRate*2;
		}
		if (Hespori.activeGolparSeed) {
			clueAmount = 2;
		}
		if (!Boundary.isIn(attachment, Boundary.HESPORI)) {
			if (Misc.random(tree.getPetChance() / dropRate) == 10) {
				switch (Misc.random(2)) {
					case 0:
						attachment.getItems().addItemUnderAnyCircumstance(19712, clueAmount);
						break;
					case 1:
						attachment.getItems().addItemUnderAnyCircumstance(19714, clueAmount);
						break;
					case 2:
						attachment.getItems().addItemUnderAnyCircumstance(19716, clueAmount);
						break;
				}
				attachment.sendMessage("@blu@You appear to see a clue nest fall from the tree, and pick it up.");
			}
		}

		if (Misc.random(500) == 1 && !Boundary.isIn(attachment, Boundary.HESPORI)) {
			attachment.getItems().addItemUnderAnyCircumstance(lumberjackOutfit[Misc.random(lumberjackOutfit.length - 1)], 1);
			attachment.sendMessage("You notice a lumberjack piece falling from the tree and pick it up.");
		}

		if (Misc.random(175) == 1 && !Boundary.isIn(attachment, Boundary.HESPORI)) {
			var possibleNests = new int[] { Items.BIRD_NEST, Items.BIRD_NEST_2, Items.BIRD_NEST_3, Items.BIRD_NEST_4,
					Items.BIRD_NEST_5, Items.BIRD_NEST_6 };
			var randomNest = possibleNests[Misc.random(possibleNests.length - 1)];
			attachment.getItems().addItemUnderAnyCircumstance(randomNest, 1);
			attachment.sendMessage("You notice a bird's nest falling from the tree and pick it up.");
		}

		int petRate = attachment.skillingPetRateScroll ? (int) (tree.getPetChance() * .75) : tree.getPetChance();
		if (!Boundary.isIn(attachment, Boundary.HESPORI) && Misc.random(petRate) == 2 && attachment.getItems().getItemCount(13322, false) == 0 && attachment.petSummonId != 13322) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + attachment.getDisplayName() + "</col> chopped down a tree for the <col=CC0000>Beaver</col> pet!");
			attachment.getItems().addItemUnderAnyCircumstance(13322, 1);
			attachment.getCollectionLog().handleDrop(attachment, 5, 13322, 1);
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (attachment != null) {
			attachment.startAnimation(65535);
		}
	}

}
