package io.xeros.content.bosses.nex.Attacks.Specials.Smoke;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.melee.CombatPrayer;
import io.xeros.model.entity.player.Player;

public class Drag {

    public Drag(Player target, NexNPC nex) {
        if (target == null || nex == null) {
            return;
        }
        if (target.getDistance(nex.getX(), nex.getY()) < 7) {
            nex.startAnimation(9188);
            target.moveTo(nex.getPosition());
            target.sendMessage("Nex drags you in, disabling your prayers!");
            CombatPrayer.resetPrayers(target);
            target.prayerId = -1;
        }
    }

}
