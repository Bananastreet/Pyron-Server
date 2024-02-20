package io.xeros.content.bosses.nex.Attacks.Specials.Blood;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;

public class BloodSacrifice {

    public BloodSacrifice(NexNPC nex, Player target) {
        nex.forceChat("I demand a blood sacrifice!");
        target.gfx0(2015);
        target.sendMessage("@red@Nex has marked you for a blood sacrifice! RUN!");
        nex.setPlayerAttackingIndex(target.getIndex());
        nex.teleport(target.getPosition());
        nex.startAnimation(9182);

        CycleEventHandler.getSingleton().addEvent(target, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() >= 7) {
                    if (target.getPosition().getAbsDistance(nex.asNPC().getPosition()) < 8) {
                        //if they aren't more than 7 tiles away, hit for damage and heal nex
                        var damage = Misc.random(20, 50);
                        target.appendDamage(damage, Hitmark.HIT);
                        nex.appendHeal(damage, Hitmark.HEAL_PURPLE);

                        //drain 1/3 of prayer
                        var currentPrayer = target.playerLevel[Player.playerPrayer];
                        target.playerLevel[Player.playerPrayer] -= currentPrayer / 3;
                        target.getPA().refreshSkill(Player.playerPrayer);

                        //hit nearby players & drain prayer if they are within 2 tiles of marked player
                        nex.getInstance().getPlayers().forEach(player -> {
                            if (player != null && player.getPosition().withinDistance(target.getPosition(), 2)) {
                                var randomHit = Misc.random(5, 12);
                                player.appendDamage(randomHit, Hitmark.HIT);
                                player.playerLevel[Player.playerPrayer] -= currentPrayer / 3;
                                target.getPA().refreshSkill(Player.playerPrayer);
                                nex.appendHeal(randomHit, Hitmark.HEAL_PURPLE);
                            }
                        });
                    }
                    container.stop();
                }
            }
        }, 1);
    }

}
