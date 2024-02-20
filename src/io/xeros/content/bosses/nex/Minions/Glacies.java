package io.xeros.content.bosses.nex.Minions;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Position;

public class Glacies extends NPC {

    public static int glaciesId = 11286, glaciesX = 2913, glaciesY = 5191;
    static boolean canAttackGlacies = false;
    public Glacies(Position position) {
        super(glaciesId, new Position(glaciesX, glaciesY, position.getHeight()));
        this.facePosition(NexNPC.spawnPosition);
        this.revokeWalkingPrivilege = true;
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return NexNPC.glaciesAttackable;
    }

    @Override
    public void onDeath() {
        NexNPC.glaciesAttackable = false;
    }

}
