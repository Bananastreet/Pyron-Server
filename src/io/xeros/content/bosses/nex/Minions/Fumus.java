package io.xeros.content.bosses.nex.Minions;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Position;

public class Fumus extends NPC {

    public static int fumusId = 11283, fumusX = 2913, fumusY = 5215;
    static boolean canAttackFumus = false;
    public Fumus(Position position) {
        super(fumusId, new Position(fumusX, fumusY, position.getHeight()));
        this.facePosition(NexNPC.spawnPosition);
        this.revokeWalkingPrivilege = true;
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return NexNPC.fumusAttackable;
    }

    @Override
    public void onDeath() {
        NexNPC.fumusAttackable = false;
    }

}
