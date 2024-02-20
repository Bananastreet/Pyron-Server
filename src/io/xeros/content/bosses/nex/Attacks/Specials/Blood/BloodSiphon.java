package io.xeros.content.bosses.nex.Attacks.Specials.Blood;

import io.xeros.content.bosses.nex.Minions.BloodReaver;
import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.content.combat.Hitmark;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Position;

public class BloodSiphon {
    public BloodSiphon(NexNPC nex) {
        nex.forceChat("A siphon will solve this!");
        nex.startAnimation(9183);
        nex.siphoning = true;
        wipePreviousAndHeal(nex);
        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() == 2) {
                    nex.getInstance().add(new BloodReaver(new Position(2937, 5206, nex.getInstance().getHeight())));
                    nex.getInstance().add(new BloodReaver(new Position(2935, 5196, nex.getInstance().getHeight())));
                }
                if (container.getTotalTicks() == 8) {
                    nex.siphoning = false;
                    container.stop();
                }
            }
        }, 1);
    }

    private void wipePreviousAndHeal(NexNPC nex) {
        for (int i = 0; i < nex.getInstance().getNpcs().size(); i++) {
            if (nex.getInstance().getNpcs().get(i).getNpcId() == BloodReaver.npcId) {
                nex.appendHeal(nex.getInstance().getNpcs().get(i).getHealth().getCurrentHealth(), Hitmark.HEAL_PURPLE);
                nex.getInstance().getNpcs().get(i).setDead(true);
            }
        }
    }

}
