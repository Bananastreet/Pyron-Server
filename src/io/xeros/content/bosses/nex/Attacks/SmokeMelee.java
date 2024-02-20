package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.Animation;
import io.xeros.model.CombatType;

import java.util.function.Function;

public class SmokeMelee implements Function<NexNPC, NPCAutoAttack> {

    @Override
    public NPCAutoAttack apply(NexNPC nex) {
        int anim = 9186;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MELEE)
                .setMaxHit(30)
                .setMultiAttack(false)
                .setHitDelay(2)
                .setPoisonDamage(8)
                .setAttackDelay(6)
                .setAccuracyBonus(npcCombatAttack -> 15.0)
                .setDistanceRequiredForAttack(2)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.5d;
                    }
                })
                .createNPCAutoAttack();
    }

}
