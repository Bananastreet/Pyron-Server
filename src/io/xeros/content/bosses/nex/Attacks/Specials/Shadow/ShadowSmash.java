package io.xeros.content.bosses.nex.Attacks.Specials.Shadow;

import io.xeros.Server;
import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.model.Graphic;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class ShadowSmash {

    public ShadowSmash(NexNPC nex) {
        if (nex == null) {
            return;
        }
        nex.forceChat("Fear the shadow!");

        List<Position> positions = new ArrayList<>();
        nex.getInstance().getPlayers().forEach(p -> positions.add(p.getPosition()));

        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            int tick = 0;
            @Override
            public void execute(CycleEventContainer container) {
                tick++;
                switch (tick) {
                    case 0:
                    case 2:
                    case 4:
                        positions.forEach(p ->
                                nex.getInstance().getPlayers().forEach(i -> i.getPA().stillGfx(379, p.getX(), p.getY(), nex.getInstance().getHeight(), 1)));
                        break;

                    case 6:
                        container.stop();
                        break;
                }
            }

            @Override
            public void onStopped() {
                positions.forEach(p ->
                        nex.getInstance().getPlayers().forEach(i -> i.getPA().stillGfx(383, p.getX(), p.getY(), nex.getInstance().getHeight(), 2)));

                nex.getInstance().getPlayers().forEach(p -> {
                    if (positions.stream().anyMatch(i -> i.equals(p.getPosition()))) {
                        p.appendDamage(nex, Misc.random(30, 50), Hitmark.HIT);
                    }
                });
            }
        }, 1);


    }

}
