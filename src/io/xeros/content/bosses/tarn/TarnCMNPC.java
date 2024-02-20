package io.xeros.content.bosses.tarn;

import com.google.common.collect.Lists;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.content.combat.npc.NPCAutoAttackBuilder;
import io.xeros.model.*;
import io.xeros.model.cycleevent.*;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.*;
import io.xeros.util.Misc;

public class TarnCMNPC extends NPC {

    public static void checkRequirements(Player player) {

    }

    public TarnCMNPC(int npcId, Position position, Player player) {
        super(npcId, position);
        setAttacks();
        this.spawnedBy = player.getIndex();
    }

    private void setAttacks() {
        setNpcAutoAttacks(Lists.newArrayList(
                new NPCAutoAttackBuilder()
                        //.setSelectAutoAttack(attack -> Misc.trueRand(1) == 0)
                        .setCombatType(CombatType.MELEE)
                        .setDistanceRequiredForAttack(10)
                        .setHitDelay(1)
                        .setAnimation(new Animation(5617))
                        .setOnHit(attack ->  {
                            //if (attack.getCombatHit().missed())
                            if (Misc.random(2) != 1)
                                return;
                            var target = attack.getVictim().asPlayer();
                            int randomAttack = Misc.random(3);
                            if (randomAttack == 1) {
                                target.gfx100(601);
                                target.sendMessage("");
                                target.sendMessage("@red@Tarn is preparing to use a special attack: green burst");
                                this.forceChat("GREEN BURST");
                                greenBurst(target, this);
                            } else if (randomAttack == 2) {
                                target.gfx100(601);
                                target.sendMessage("");
                                target.sendMessage("@red@Tarn is preparing to use a special attack: underground fire");
                                this.forceChat("UNDERGROUND FIRE");
                                undergroundFire(target, this);
                            } else {
                                target.gfx100(601);
                                target.sendMessage("");
                                target.sendMessage("@red@Tarn is preparing to use a special attack: smokescreen");
                                this.forceChat("SMOKESCREEN");
                                smokescreen(target, this);
                            }
                        })
                        .setMaxHit(35)
                        .setAttackDelay(6)
                        .createNPCAutoAttack()

        ));
    }

    private void greenBurst(Player victim, TarnCMNPC tarn) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!victim.prayerActive[CombatPrayer.PROTECT_FROM_MAGIC]) {
                    victim.appendDamage(Misc.random(45, 85), Hitmark.HIT);
                    victim.gfx100(266);
                    tarn.appendHeal(250, Hitmark.HEAL_PURPLE);
                } else {
                    victim.gfx100(266);
                    victim.appendDamage(Misc.random(15, 25), Hitmark.HIT);
                }
                container.stop();
            }
        }, 5);
    }
    private void undergroundFire(Player victim, TarnCMNPC tarn) {
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

    private void smokescreen(Player victim, TarnCMNPC tarn) {
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
