package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.Animation;
import io.xeros.model.CombatType;
import io.xeros.model.ProjectileBase;
import io.xeros.model.ProjectileBaseBuilder;
import io.xeros.model.entity.npc.stats.NpcBonus;
import io.xeros.model.entity.npc.stats.NpcCombatSkill;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;

import java.util.function.Function;

public class ZarosMage implements Function<NexNPC, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(50)
                .setEndHeight(25)
                .setProjectileId(2007)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(NexNPC nex) {
        int anim = Misc.random(1) == 0 ? 9188 : 9189;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MAGE)
                .setMaxHit(33)
                .setMultiAttack(true)
                .setSelectPlayersForMultiAttack(NPCAutoAttack.nexMultiDistance())
                .setHitDelay(3)
                .setOnHit(attack -> {
                    if (attack.getCombatHit().missed())
                        return;

                    var target = attack.getVictim().asPlayer();
                    var canDrainPrayer = target.playerLevel[Player.playerPrayer] > 5;
                    if (canDrainPrayer) {
                        target.playerLevel[Player.playerPrayer] -= 5;
                        target.getPA().refreshSkill(Player.playerPrayer);
                    }

                    //Soulsplit heal
                    if (Misc.hasOneOutOf(3)) {
                        nex.appendHeal(attack.getCombatHit().getDamage() / 2, Hitmark.HEAL_PURPLE);
                    }

                    var chance = Misc.hasOneOutOf(10);
                    int[] playerStats = {Player.playerAttack, Player.playerStrength, Player.playerDefence, Player.playerRanged, Player.playerMagic};
                    NpcCombatSkill[] nexStats = {NpcCombatSkill.ATTACK, NpcCombatSkill.STRENGTH, NpcCombatSkill.DEFENCE, NpcCombatSkill.RANGE, NpcCombatSkill.MAGIC};
                    if (chance) {
                        var drainAmount = Misc.random(2, 5);
                        //drain players stats
                        for (int skill : playerStats) {
                            target.playerLevel[skill] -= drainAmount;
                            if (target.playerLevel[skill] < 1) {
                                target.playerLevel[skill] = 1;
                            }
                            target.getPA().refreshSkill(skill);
                        }
                        //increase nex stats
                        for (NpcCombatSkill skill : nexStats) {
                            nex.getCombatDefinition().setLevel(skill, nex.getCombatDefinition().getLevel(skill) + drainAmount);
                        }
                    }
                })
                .setProjectile(projectile())
                .setAttackDelay(6)
                .setDistanceRequiredForAttack(10)
                .setAccuracyBonus(npcCombatAttack -> 15.0)
                .setMaxHitBonus(npcCombatAttack -> 1.0)
                .setPrayerProtectionPercentage(npcCombatAttack -> 1.0)
                .createNPCAutoAttack();
    }

}
