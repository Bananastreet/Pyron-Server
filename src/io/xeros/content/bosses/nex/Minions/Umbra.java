package io.xeros.content.bosses.nex.Minions;

import io.xeros.content.bosses.nex.NexNPC;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Position;

public class Umbra extends NPC {

    public static int umbraId = 11284, umbraX = 2937, umbraY = 5215;
    static boolean canAttackUmbra = false;
    public Umbra(Position position) {
        super(umbraId, new Position(umbraX, umbraY, position.getHeight()));
        this.facePosition(NexNPC.spawnPosition);
        this.revokeWalkingPrivilege = true;
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return NexNPC.umbraAttackable;
    }

    @Override
    public void onDeath() {
        NexNPC.umbraAttackable = false;
    }

}
