package io.xeros.content.bosses.nex.Attacks.Specials.Smoke;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class Choke {

    public Choke(Player target, NexNPC nex) {
        ChokeAttack(target);
        nex.forceChat("Let the virus flow through you!");
        nex.startAnimation(9188);
   }

    void ChokeAttack(Player target) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!Boundary.isIn(target, Boundary.Nex)) {
                    target.getAttributes().setBoolean("hasNexVirus", false);
                    target.getAttributes().setBoolean("hasHadNexVirus", false);
                }
                if (!Boundary.isIn(target, Boundary.Nex)) {
                    target.getAttributes().remove("nexVirusTimer");
                    target.getAttributes().remove("hasNexVirus");
                    target.getAttributes().remove("hasHadNexVirus");
                    container.stop();
                }
                if (target.getAttributes().getBoolean("hasNexVirus") && target.getAttributes().getInt("nexVirusTimer") <= 0) {
                    target.getAttributes().remove("hasNexVirus");
                    target.getAttributes().remove("hasHadNexVirus");
                    container.stop();
                }

                if (!target.getAttributes().contains("hasNexVirus") && Boundary.isIn(target, Boundary.Nex)) {
                    target.forcedChat("*Cough*");
                    target.getAttributes().setInt("nexVirusTimer", 100);
                    target.getAttributes().setBoolean("hasNexVirus", true);
                    target.getAttributes().setBoolean("hasHadNexVirus", true);
                    target.getAttributes().setLong("TimeSinceVirus", (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
                }
                if (target.getAttributes().getInt("nexVirusTimer") > 0) {
                    target.forcedChat("*Cough*");
                }
                target.getAttributes().setInt("nexVirusTimer", target.getAttributes().getInt("nexVirusTimer") - 10);
                var canDrainPrayer = target.playerLevel[Player.playerPrayer] > 1;
                if (canDrainPrayer) {
                    target.playerLevel[Player.playerPrayer] -= 2;
                    target.getPA().refreshSkill(Player.playerPrayer);
                }
                for (Player player : PlayerHandler.getPlayers()) {
                    if (player != target && !player.getAttributes().getBoolean("hasNexVirus") && player.getPosition().withinDistance(target.getPosition(), 2)) {
                        if (!player.getAttributes().getBoolean("hasHadNexVirus") && player.getAttributes().getLong("TimeSinceVirus") <= System.currentTimeMillis()) {
                            ChokeAttack(player);
                        }
                    }
                }
            }
            @Override
            public void onStopped() {
                target.getAttributes().remove("nexVirusTimer");
                target.getAttributes().remove("hasNexVirus");
                target.getAttributes().remove("hasHadNexVirus");
                target.getAttributes().remove("TimeSinceVirus");
            }
        }, 10);
    }

}
