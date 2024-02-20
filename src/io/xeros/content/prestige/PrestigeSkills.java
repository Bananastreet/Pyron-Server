package io.xeros.content.prestige;

import io.xeros.Configuration;
import io.xeros.content.leaderboard.LeaderboardData;
import io.xeros.content.leaderboard.LeaderboardSerialisation;
import io.xeros.content.leaderboard.impl.Boxes;
import io.xeros.content.leaderboard.impl.Misc;
import io.xeros.content.skills.Skill;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.mode.ModeType;

public class PrestigeSkills {

	public Player player;
	
	public PrestigeSkills(Player player) {
		this.player = player;
	}
	
	public final int MAX_PRESTIGE = 10;
	
	public int points = 1; // This is the base prestige points given
	
	public void openPrestige() { // Refreshes all text lines before showing the interface - Looks better
		for (int j = 0; j < 22; j++) {
			player.getPA().sendFrame126(""+player.prestigeLevel[j]+"", 37400 + j); // Update Current Prestige on interface
		}
		registerClick(0);
		player.getPA().showInterface(37300);
	}
	
	public void openShop() {
		player.sendMessage("@blu@ You have "+player.getPrestigePoints()+" prestige points.");
		player.getShops().openShop(120);
	}
	
	public void registerClick(int i) {
		player.prestigeNumber = i;
		player.currentPrestigeLevel = player.prestigeLevel[player.prestigeNumber];
		player.canPrestige = player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber]) == 99; //Update global canPrestige boolean
		String canPrestige = ((player.canPrestige == true) ? "@gre@Yes" : "@red@No"); // String version for interface Yes or No
		player.getPA().sendFrame126(Skill.forId(player.prestigeNumber).toString(), 37307); // Update Skill Name
		player.getPA().sendFrame126("Current Prestige: @whi@"+player.currentPrestigeLevel, 37308); // Update Current Prestige in box
		player.getPA().sendFrame126("Reward: @whi@"+prestigeRewards(player.prestigeNumber)+" Points", 37309); // Update Reward
		player.getPA().sendFrame126("Can Prestige: "+ canPrestige, 37390); // Update If you can prestige
	}
	
	public void prestige() {
		if (player.currentPrestigeLevel == MAX_PRESTIGE) { // Change to prestige master
			player.sendMessage("You are the max prestige level in this skill!");
			return;
		}
		if (player.getItems().isWearingItems()) { // Change to prestige master
			player.getDH().sendNpcChat1("You must remove your equipment before prestiging a skill", 2989, "Ak-Haranu");
			return;
		}
		player.canPrestige = player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber]) == 99; //Update global canPrestige boolean
		if (player.canPrestige) { // If the skill is 99
			if (player.VERIFICATION == 0) { // Verification Check
				player.sendMessage("@red@Please click prestige again to confirm prestiging of the "+ Configuration.SKILL_NAME[player.prestigeNumber]+" skill.");
				player.VERIFICATION++;
				return;
			}
			player.VERIFICATION = 0;
			if (player.prestigeNumber != 3) { // If not Hitpoints
				player.playerLevel[player.prestigeNumber] = 1;
				player.playerXP[player.prestigeNumber] = player.getPA().getXPForLevel(1);
				player.getPA().setSkillLevel(player.prestigeNumber, 1, player.getPA().getXPForLevel(1));
				player.getPA().refreshSkill(player.prestigeNumber); // Refresh skills
			} else { // Hitpoints should be 10
				player.playerLevel[player.prestigeNumber] = 10;
				player.playerXP[player.prestigeNumber] = player.getPA().getXPForLevel(10) + 1;
				player.getPA().setSkillLevel(player.prestigeNumber, 10, player.getPA().getXPForLevel(10));
				player.getPA().refreshSkill(player.prestigeNumber); // Refresh skills
			}
			if (player.prestigeNumber == 18) {
				player.getSlayer().removeCurrentTask();
				player.sendMessage("Your slayer task has been reset upon prestiging. ");
			}
			if (player.prestigeNumber <= 6) {
				player.combatLevel = player.calculateCombatLevel();
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
			}
			player.prestigePoints+= prestigeRewards(player.prestigeNumber);
			player.prestigeLevel[player.prestigeNumber] += 1;
			registerClick(player.prestigeNumber);
			player.totalPrestiges++;
			if (Configuration.leaderboardEnabled) {
				LeaderboardSerialisation.INSTANCE.updateCount(player, LeaderboardData.MISCELLANEOUS, player.totalPrestiges, Misc.TOTAL_PRESTIGES);
			}
			if (player.prestigeNumber > 6)
			PlayerHandler.dropMessage(player.getDisplayNameFormatted() +" has advanced "+ Configuration.SKILL_NAME[player.prestigeNumber] + " to level "+ player.prestigeLevel[player.prestigeNumber] + " Prestige.");
			player.getPA().sendFrame126(""+player.prestigeLevel[player.prestigeNumber]+"", 37400 + player.prestigeNumber); // Update Current Prestige on interface

		} else {
			player.sendMessage("You cannot prestige "+ Configuration.SKILL_NAME[player.prestigeNumber]+" you need to gain "+ (99 -  player.getPA().getLevelForXP(player.playerXP[player.prestigeNumber])) +" more "+ Configuration.SKILL_NAME[player.prestigeNumber]+" levels.");
		}
	}

	public int prestigeRewards(int skill) {
		int reward = 1;
		switch (skill) {
			case 0: //attack
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 1: //defense
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 2: //strength
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 3: //hitpoints
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 4: //ranged
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 5: //prayer
				reward += (4 + player.currentPrestigeLevel);
				break;
			case 6: //magic
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 7: //cooking
				reward += (2 + player.currentPrestigeLevel);
				break;
			case 8: //woodcutting
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 9: //fletching
				reward += (3 + player.currentPrestigeLevel);
				break;
			case 10: //fishing
				reward += (3 + player.currentPrestigeLevel);
				break;
			case 11: //firemaking
				reward += (4 + player.currentPrestigeLevel);
				break;
			case 12: //crafting
				reward += (4 + player.currentPrestigeLevel);
				break;
			case 13: //smithing
				reward += (3 + player.currentPrestigeLevel);
				break;
			case 14: //mining
				reward += (1 + player.currentPrestigeLevel);
				break;
			case 15: //herblore
				reward += (6 + player.currentPrestigeLevel);
				break;
			case 16: //agility
				reward += (8 + player.currentPrestigeLevel);
				break;
			case 17: //thieving
				reward += (3 + player.currentPrestigeLevel);
				break;
			case 18: //slayer
				reward += (15 + player.currentPrestigeLevel);
				break;
			case 19: //farming
				reward += (9 + player.currentPrestigeLevel);
				break;
			case 20: //runecrafting
				reward += (4 + player.currentPrestigeLevel);
				break;
			case 21: //hunter
				reward += (7 + player.currentPrestigeLevel);
				break;
		}
		if (player.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN || player.getMode().getType() == ModeType.ROGUE || player.getMode().getType() == ModeType.ROGUE_IRONMAN) {
			reward *= 3;
		}
		return reward; //default
	}
	
	public boolean prestigeClicking(int id) {
		if (id != 146015)
			player.VERIFICATION = 0;
		switch (id) {
			case 145191:
				registerClick(0);
			return true;
			case 145192:
				registerClick(1);
			return true;
			case 145193:
				registerClick(2);
			return true;
			case 145194:
				registerClick(3);
			return true;
			case 145195:
				registerClick(4);
			return true;
			case 145196:
				registerClick(5);
			return true;
			case 145197:
				registerClick(6);
			return true;
			case 145198:
				registerClick(7);
			return true;
			case 145199:
				registerClick(8);
			return true;
			case 145200:
				registerClick(9);
			return true;
			case 145201:
				registerClick(10);
			return true;
			case 145202:
				registerClick(11);
			return true;
			case 145203:
				registerClick(12);
			return true;
			case 145204:
				registerClick(13);
			return true;
			case 145205:
				registerClick(14);
			return true;
			case 145206:
				registerClick(15);
			return true;
			case 145207:
				registerClick(16);
			return true;
			case 145208:
				registerClick(17);
			return true;
			case 145209:
				registerClick(18);
			return true;
			case 145210:
				registerClick(19);
			return true;
			case 145211:
				registerClick(20);
			return true;
			case 145212:
				registerClick(21);
			return true;
			case 146015:
				prestige();
			return true;
			case 145182:
				player.getPA().closeAllWindows();
			return true;
		}
		return false;
	}
}