package io.xeros.content.bosses.maledictus;

import com.google.common.collect.Lists;
import io.xeros.Server;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.model.*;
import io.xeros.model.cycleevent.*;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.*;
import io.xeros.util.Misc;

import java.util.List;
import java.util.Optional;

public class MaledictusNPC extends NPC {

    public static void checkRequirements(Player player) {

    }

    public MaledictusNPC(int npcId, Position position, Player player) {
        super(npcId, position);
        setAttacks();
        this.spawnedBy = player.getIndex();
    }

    private void setAttacks() {
        setNpcAutoAttacks(Lists.newArrayList(
                new NPCAutoAttackBuilder()
                        //.setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                        .setCombatType(CombatType.MAGE)
                        .setDistanceRequiredForAttack(10)
                        .setHitDelay(1)
                        .setAnimation(new Animation(9279))
                        .setOnHit(attack ->  {
                            //if (attack.getCombatHit().missed())
                            if (Misc.random(2) != 1)
                                return;
                            var target = attack.getVictim().asPlayer();
                            int randomAttack = Misc.random(2);
                            if (randomAttack == 1) {
                                CombatPrayer.resetPrayers(target);
                                target.sendMessage("");
                                target.sendMessage("@red@Maledictus has disabled your prayers.");
                                this.forceChat("There is no protection from me...");
                            } else {
                                target.sendMessage("");
                                target.sendMessage("@red@Maledictus is going to charge...");
                                attackOne(target, this);
                                this.forceChat("My hooves will trample you...");
                            }
                        })
                        .setMaxHit(35)
                        .setAttackDelay(5)
                        .createNPCAutoAttack()

        ));
    }

    private void attackOne(Player victim, MaledictusNPC tarn) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!victim.prayerActive[CombatPrayer.PROTECT_FROM_MELEE]) {
                    victim.appendDamage(Misc.random(35, 65), Hitmark.HEAL_PURPLE);
                    tarn.appendHeal(100, Hitmark.HEAL_PURPLE);
                    tarn.startAnimation(9277);
                } else {
                    victim.appendDamage(Misc.random(15, 25), Hitmark.HEAL_PURPLE);
                }
                container.stop();
            }
        }, 5);
    }
    private void undergroundFire(Player victim, MaledictusNPC tarn) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!victim.prayerActive[CombatPrayer.PROTECT_FROM_MELEE]) {
                    victim.appendDamage(Misc.random(45, 85), Hitmark.HIT);
                    victim.gfx100(498);
                    tarn.appendHeal(250, Hitmark.HEAL_PURPLE);
                } else {
                    victim.gfx100(498);
                    victim.appendDamage(Misc.random(15, 25), Hitmark.HIT);
                }
                container.stop();
            }
        }, 5);
    }

    private void smokescreen(Player victim, MaledictusNPC tarn) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!victim.prayerActive[CombatPrayer.PROTECT_FROM_RANGED]) {
                    victim.appendDamage(Misc.random(45, 85), Hitmark.HIT);
                    victim.gfx100(974);
                    tarn.appendHeal(250, Hitmark.HEAL_PURPLE);
                } else {
                    victim.gfx100(974);
                    victim.appendDamage(Misc.random(15, 25), Hitmark.HIT);
                }
                container.stop();
            }
        }, 5);
    }



    @Override
    public void process() {
        setAttacks();
        super.process();
    }

    @Override
    public boolean isFreezable() {
        return false;
    }


    //7210 = on sucessful hit with special attack on PLAYER
    //gfx 60
    //7183 = special attack anim on NPC
    //1148 = climbing down rocks



    /**
     *
     *
     *
     */

}
