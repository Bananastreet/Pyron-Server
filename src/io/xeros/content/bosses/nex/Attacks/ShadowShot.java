package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.*;
import io.xeros.model.entity.player.Player;

import java.util.function.Function;

public class ShadowShot implements Function<NexNPC, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(50)
                .setEndHeight(25)
                .setProjectileId(1999)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(NexNPC nex) {
        int anim = 9180;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.RANGE)
                .setMaxHit(30)
                .setMultiAttack(true)
                .setSelectPlayersForMultiAttack(NPCAutoAttack.nexMultiDistance())
                .setHitDelay(4)
                .setOnHit(attack -> {
                    if (attack.getCombatHit().missed())
                        return;
                    var canDrainPrayer = attack.getVictim().asPlayer().playerLevel[Player.playerPrayer] > 1;
                    if (canDrainPrayer) {
                        attack.getVictim().asPlayer().playerLevel[Player.playerPrayer] -= 2;
                        attack.getVictim().asPlayer().getPA().refreshSkill(Player.playerPrayer);
                    }
                })
                .setProjectile(projectile())
                .setAttackDelay(6)
                .setDistanceRequiredForAttack(10)
                .setMaxHitBonus(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        if (npcCombatAttack.getVictim().isPlayer()) {
                            Player player = npcCombatAttack.getVictim().asPlayer();
                            if (player.getPosition().getAbsDistance(nex.asNPC().getPosition()) < 5) {
                                return 1.0d;
                            }
                        }
                        return 0d;
                    }
                })
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.5d;
                    }
                })

                .createNPCAutoAttack();
    }

}
