package io.xeros.content.bosses.nex.Attacks;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.npc.NPCAutoAttack;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.content.combat.npc.NPCCombatAttack;
import io.xeros.model.*;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;

import java.util.function.Function;

public class BloodBarrage implements Function<NexNPC, NPCAutoAttack> {

    private static final Graphic endGraphic = new Graphic(2003, Graphic.GraphicHeight.LOW);

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(55)
                .setStartHeight(50)
                .setEndHeight(25)
                .setProjectileId(2002)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(NexNPC nex) {
        int anim = 9181;
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MAGE)
                .setMaxHit(33)
                .setMultiAttack(false)
                .setHitDelay(4)
                .setOnHit(attack -> {
                    if (attack.getCombatHit().missed()) { return; }
                    var target = attack.getVictim().asPlayer();
                    var canDrainPrayer = target.playerLevel[Player.playerPrayer] > 1;
                    if (canDrainPrayer) {
                        target.playerLevel[Player.playerPrayer] -= 2;
                        target.getPA().refreshSkill(Player.playerPrayer);
                    }

                    target.gfx0(2003);

                    //heals since using blood barrage
                    nex.appendHeal(attack.getCombatHit().getDamage() / 4, Hitmark.HEAL_PURPLE);

                    //handle 3x3 area damage
                    var playerPosition = target.getPosition();
                    nex.getInstance().getPlayers().forEach(player -> {
                        if (player == attack.getVictim().asPlayer()) { return; }
                        if (player != null && player.getPosition().withinDistance(playerPosition, 3)) {
                            var randomHit = Misc.random(5, 33);
                            player.gfx0(2003);
                            player.appendDamage(randomHit, Hitmark.HIT);
                            nex.appendHeal(randomHit / 4, Hitmark.HEAL_PURPLE);
                        }
                    });
                })
                .setProjectile(projectile())
                .setAccuracyBonus(npcCombatAttack -> 15.0)
                .setAttackDelay(6)
                .setDistanceRequiredForAttack(10)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.3d;
                    }
                })

                .createNPCAutoAttack();
    }

}
