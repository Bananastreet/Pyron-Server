package io.xeros.content.bosses.ulfric;

import com.google.common.collect.Lists;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.Animation;
import io.xeros.model.CombatType;
import io.xeros.model.ProjectileBase;
import io.xeros.model.ProjectileBaseBuilder;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.util.Misc;

import java.util.function.Function;

public class UlfricNPC extends NPC {

    public UlfricNPC(int npcId, Position position, Player player) {
        super(npcId, position);
        setAttacks();
        this.spawnedBy = player.getIndex();
    }

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(30)
                //.setStartHeight(90)
                .setProjectileId(88)
                .createProjectileBase();
    }

    private void setAttacks() {
        setNpcAutoAttacks(Lists.newArrayList(
                new NPCAutoAttackBuilder()
                        //.setAnimation(new Animation(7770))
                        .setCombatType(CombatType.RANGE)
                        .setMaxHit(15)
                        .setHitDelay(2)
                        .setAttackDelay(6)
                        .setDistanceRequiredForAttack(24)
                        .setMultiAttack(false)
                        .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                            @Override
                            public Double apply(NPCCombatAttack npcCombatAttack) {
                                return 0.3;
                            }
                        })
                        .setProjectile(projectile())
                        .createNPCAutoAttack()

        ));
    }

    @Override
    public void process() {
        setAttacks();
        super.process();
    }

    @Override
    public boolean isFreezable() {
        return true;
    }
}
