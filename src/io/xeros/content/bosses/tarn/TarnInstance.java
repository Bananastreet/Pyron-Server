package io.xeros.content.bosses.tarn;

import io.xeros.content.instances.*;
import io.xeros.model.Npcs;
import io.xeros.model.collisionmap.WorldObject;
import io.xeros.model.cycleevent.*;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.*;

/**
 * @author Bubly
 */

public class TarnInstance extends InstancedArea {


    public TarnInstance() {
        super(InstanceConfiguration.CLOSE_ON_EMPTY, Boundary.TARN_AREA);
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
            Position pos = new Position(2530, 9294, getHeight());
            player.moveTo(pos);
            player.facePosition(pos.getX(), pos.getY() - 1);
            add(player);
            NPC npc = new TarnNPC(6477, new Position(2518, 9295, getHeight()), player);
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
