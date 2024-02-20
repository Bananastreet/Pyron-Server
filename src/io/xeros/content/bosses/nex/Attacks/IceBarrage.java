package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.Animation;
import io.xeros.model.CombatType;
import io.xeros.model.ProjectileBase;
import io.xeros.model.ProjectileBaseBuilder;
import io.xeros.model.entity.player.ClientGameTimer;
import io.xeros.model.entity.player.Player;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class IceBarrage implements Function<NexNPC, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(50)
                .setEndHeight(25)
                .setProjectileId(2006)
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
                .setIgnoreProjectileClipping(true)
                .setOnHit(attack -> {
                    if (attack.getCombatHit().missed()) { return; }
                    var target = attack.getVictim().asPlayer();

                    var canDrainPrayer = target.playerLevel[Player.playerPrayer] > attack.getCombatHit().getDamage() / 2;
                    if (canDrainPrayer) {
                        target.playerLevel[Player.playerPrayer] -= attack.getCombatHit().getDamage() / 2;
                        target.getPA().refreshSkill(Player.playerPrayer);
                    }

                    if (!target.prayerActive[16]) {
                        target.freezeTimer = 12;
                        target.sendMessage("You have been frozen!");
                        target.getPA().sendGameTimer(ClientGameTimer.FREEZE, TimeUnit.MILLISECONDS, 600 * 12);
                        target.gfx0(2005);
                    }

                })
                .setProjectile(projectile())
                .setAccuracyBonus(npcCombatAttack -> 15.0)
                .setAttackDelay(6)
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
