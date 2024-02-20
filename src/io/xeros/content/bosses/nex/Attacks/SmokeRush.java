package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.*;

import java.util.function.Function;

public class SmokeRush implements Function<NexNPC, NPCAutoAttack> {

    private static final Graphic endGraphic = new Graphic(387, Graphic.GraphicHeight.MIDDLE);

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(50)
                .setEndHeight(25)
                .setProjectileId(1997)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(NexNPC nex) {
        int anim = 9181;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MAGE)
                .setMaxHit(33)
                .setMultiAttack(true)
                .setSelectPlayersForMultiAttack(NPCAutoAttack.nexMultiDistance())
                .setHitDelay(4)
                .setPoisonDamage(8)
                .setProjectile(projectile())
                .setEndGraphic(endGraphic)
                .setAttackDelay(6)
                .setAccuracyBonus(npcCombatAttack -> 15.0)
                .setDistanceRequiredForAttack(10)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.2d;
                    }
                })
                .createNPCAutoAttack();
    }

}
