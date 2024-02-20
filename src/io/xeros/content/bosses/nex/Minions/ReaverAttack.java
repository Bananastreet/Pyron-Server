package io.xeros.content.bosses.nex.Minions;

import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.Animation;
import io.xeros.model.CombatType;
import io.xeros.model.ProjectileBase;
import io.xeros.model.ProjectileBaseBuilder;

import java.util.function.Function;

public class ReaverAttack implements Function<BloodReaver, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(40)
                .setEndHeight(25)
                .setProjectileId(374)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(BloodReaver reaver) {
        int anim = 9194;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MAGE)
                .setMaxHit(20)
                .setMultiAttack(false)
                .setHitDelay(4)
                .setOnHit(attack -> {
                    if (attack.getCombatHit().missed()) { return; }
                    var target = attack.getVictim().asPlayer();

                    target.gfx0(373);

                    //heals since using blood rush
                    reaver.appendHeal(attack.getCombatHit().getDamage() / 4, Hitmark.HEAL_PURPLE);
                })
                .setProjectile(projectile())
                .setAttackDelay(6)
                .setDistanceRequiredForAttack(10)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.5d;
                    }
                })

                .createNPCAutoAttack();
    }

}
