package io.xeros.content.bosses.ulfric;

import io.xeros.content.bosses.tarn.TarnNPC;
import io.xeros.content.instances.InstanceConfiguration;
import io.xeros.content.instances.InstancedArea;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;

public class ulfricInstance extends InstancedArea {

    public ulfricInstance() {
        super(InstanceConfiguration.CLOSE_ON_EMPTY, Boundary.ULFRIC_AREA);
    }

    public void begin(Player player) {

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

            int tick;

            @Override
            public void execute(CycleEventContainer container) {

                if (player == null || player.isDead() || !player.isOnline()) {
                    container.stop();
                    return;
                }
                if (tick == 4) {
                    enter(player);
                    container.stop();
                }
                tick++;
            }

        }, 1);
    }

    public void enter(Player player) {
        try {
            Position pos = new Position(3840, 9074, getHeight());
            player.moveTo(pos);
            player.facePosition(pos.getX(), pos.getY() - 1);
            add(player);
            NPC npc = new UlfricNPC(4500, new Position(3840, 9079, getHeight()), player);
            add(npc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDispose() {
        getPlayers().stream().forEach(plr -> {
            remove(plr);
        });
    }

}
