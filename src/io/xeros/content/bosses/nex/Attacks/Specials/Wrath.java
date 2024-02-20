package io.xeros.content.bosses.nex.Attacks.Specials;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.model.Area;
import io.xeros.model.SquareArea;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.util.Misc;

import java.util.Arrays;

public class Wrath {

    public Wrath(NexNPC nex) {
        nex.forceChat("Taste my wrath!");

        Area[] areas = { new SquareArea(nex.getX() - 2, nex.getY() + 2, nex.getX() + 2, nex.getY() - 2) };
        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() >= 4) {
                    gfxPositions(nex);
                    nex.getInstance().getPlayers().forEach(player -> {
                        Arrays.stream(positions).forEach(position -> {
                            player.getPA().stillGfx(2013, position.getX(), position.getY(), position.getHeight(), 1);
                        });
                    });
                }
                if (container.getTotalTicks() >= 5) {
                    for (Player player : nex.getInstance().getPlayers()) {
                        if (Arrays.stream(areas).anyMatch(area -> area.inside(player))) {
                            player.appendDamage(Misc.random(20, 50), Hitmark.HIT);
                        }
                    }
                    container.stop();
                }
            }
        }, 1);
    }

    Position[] positions = new Position[25];
    private void gfxPositions(NexNPC nex) {
        var count = 0;
        for (int x = nex.getX() - 1; x <= nex.getX() + 3; x++) {
            for (int y = nex.getY() - 1; y <= nex.getY() + 3; y++) {
                positions[count] = new Position(x, y, nex.getInstance().getHeight());
                count++;
            }
        }
    }

}
