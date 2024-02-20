package io.xeros.content.bosses.nex.Attacks.Specials.Ice;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Position;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;

import java.util.List;

public class Containment extends GlobalObject {

    public Containment(Position position) {
        super(42943, position, 0, 10);
    }

    public void sendSpawnId(NexNPC nex) {
        nex.getInstance().getPlayers().forEach(p -> p.getPA().object(this));
    }

    public void remove(NexNPC nex) {
        nex.getInstance().getPlayers().forEach(p -> p.getPA().object(withId(-1)));
    }

    public static void start(NexNPC nex) {
        nex.forceChat("Contain this!");

        var nexX = nex.getX();
        var nexY = nex.getY();
        var nexH = nex.getHeight();
        List<Position> positions = List.of(
                new Position(nexX, nexY + 1, nexH), new Position(nexX, nexY, nexH), new Position(nexX, nexY + 2, nexH),
                new Position(nexX + 1, nexY, nexH), new Position(nexX + 1, nexY + 2, nexH),
                new Position(nexX + 2, nexY + 1, nexH), new Position(nexX + 2, nexY, nexH), new Position(nexX + 2, nexY + 2, nexH));

        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() == 4) {
                    positions.forEach(position -> {
                        Containment containment = new Containment(position);
                        containment.sendSpawnId(nex);

                        nex.getInstance().getPlayers().forEach(player -> {
                            var onIcicle = positions.stream().anyMatch(pos -> pos.equals(player.getPosition()));
                            if (onIcicle) {
                                player.appendDamage(Misc.random(30, 60), Hitmark.HIT);
                            }
                        });
                        CycleEventHandler.getSingleton().addEvent(containment, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (container.getTotalTicks() == 8) {
                                    containment.remove(nex);
                                }
                            }
                        }, 1);
                    });
                }
            }
        }, 1);
    }

}
