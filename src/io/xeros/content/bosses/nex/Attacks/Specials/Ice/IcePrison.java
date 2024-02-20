package io.xeros.content.bosses.nex.Attacks.Specials.Ice;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IcePrison extends GlobalObject {

    public IcePrison(Position position) {
        super(42944, position, 0, 10);
    }

    public void sendSpawnId(NexNPC nex) {
        nex.getInstance().getPlayers().forEach(p -> p.getPA().object(this));
    }

    public void remove(NexNPC nex) {
        nex.getInstance().getPlayers().forEach(p -> p.getPA().object(withId(-1)));
    }
    static boolean removePrison = false;

    public static void start(Player target, NexNPC nex) {
        target.freezeTimer = 15;
        nex.forceChat("Die now, in a prison of ice!");
        CombatPrayer.resetPrayers(target);

        var playerX = target.getX();
        var playerY = target.getY();
        var playerH = target.getHeight();
        List<Position> positions = List.of(
                new Position(playerX - 1, playerY, playerH), new Position(playerX - 1, playerY + 1, playerH),
                new Position(playerX - 1, playerY - 1, playerH), new Position(playerX, playerY - 1, playerH),
                new Position(playerX, playerY + 1, playerH), new Position(playerX + 1, playerY, playerH),
                new Position(playerX + 1, playerY + 1, playerH), new Position(playerX + 1, playerY - 1, playerH));

        final int damageX = target.getX(), damageY = target.getY(), damageH = target.getHeight();

        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() == 4) {
                    positions.forEach(position -> {
                        IcePrison prison = new IcePrison(position);
                        prison.sendSpawnId(nex);
                        CycleEventHandler.getSingleton().addEvent(prison, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (removePrison) {
                                    prison.remove(nex);
                                    removePrison = false;
                                    container.stop();
                                }
                                if (container.getTotalTicks() == 15) {
                                    prison.remove(nex);
                                    container.stop();
                                }
                            }
                        }, 1);
                    });
                }

                if (container.getTotalTicks() < 19) {
                    for (Player player : nex.getInstance().getPlayers()) {
                        if (player.objectId == 42944) {
                            player.objectId = 0;
                            if (Misc.hasOneOutOf(5)) {
                                removePrison = true;
                                container.stop();
                            }
                        }
                    }
                }

                if (container.getTotalTicks() == 19) {
                    for (Player player : nex.getInstance().getPlayers()) {
                        if (removePrison) {
                            removePrison = false;
                            return;
                        }
                        if (player.getX() == damageX && player.getY() == damageY && player.getHeight() == damageH) {
                            var damage = player.prayerActive[17] ? Misc.random(50, 75) / 2 : Misc.random(50, 75);
                            player.appendDamage(damage, Hitmark.HIT);
                            player.sendMessage("You have been damaged by the ice prison!");
                            return;
                        }
                    }
                }
            }
        }, 1);
    }


}
