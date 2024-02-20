package io.xeros.content.combat.specials.impl;

import io.xeros.content.combat.Damage;
import io.xeros.content.combat.core.HitDispatcher;
import io.xeros.content.combat.specials.Special;
import io.xeros.content.combat.specials.Specials;
import io.xeros.content.skills.Skill;
import io.xeros.model.CombatType;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Player;

public class AncientMace extends Special {

	int pReduction;
	double npcReduction;

	public AncientMace() {
		super(6.0, 100.0, 4.0, new int[] { 11061 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx0(1052);
		player.startAnimation(6147);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		if (!target.isPlayer()) {
			player.sendMessage("Your ancient mace deals "+ damage.getAmount() + " damage.");
		} else {
			player.sendMessage("You can not use this special attack in pvp.");
		}
		if (damage.getAmount() > 0) {
			pReduction = 3;
			npcReduction = .3;
		} else if (damage.getAmount() == 0) {
			pReduction = 20;
			npcReduction = .05;
		}
		if (target instanceof Player) {
			Player playerTarget = ((Player) target);
			if (playerTarget.playerLevel[1] > 0) {
				playerTarget.playerLevel[1] -= ((Player) target).playerLevel[1] / pReduction;
				playerTarget.getPA().refreshSkill(1);
			}
		} else {
			NPC npc = ((NPC) target);
			if (player.debugMessage) {
				player.sendMessage("Ancient Mace, npc defence before: " + npc.getDefence());
			}
			npc.lowerDefence(npcReduction);
			if (player.debugMessage) {
				player.sendMessage("Ancient Mace, npc defence after: " + npc.getDefence());
			}
		}
	}

}
