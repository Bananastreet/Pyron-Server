package io.xeros.content.bosses.nex.Minions;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Position;

public class Cruor extends NPC {

    public static int cruorId = 11285, cruorX = 2937, cruorY = 5191;
    static boolean canAttackCruor = false;
    public Cruor(Position position) {
        super(cruorId, new Position(cruorX, cruorY, position.getHeight()));
        this.facePosition(NexNPC.spawnPosition);
        this.revokeWalkingPrivilege = true;
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return NexNPC.cruorAttackable;
    }

    @Override
    public void onDeath() {
        NexNPC.cruorAttackable = false;
    }

}
