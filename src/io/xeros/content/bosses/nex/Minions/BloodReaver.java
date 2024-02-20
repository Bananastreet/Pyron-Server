package io.xeros.content.bosses.nex.Minions;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class BloodReaver extends NPC {

    public static int npcId = 11293;

    public BloodReaver(Position position) {
        super(npcId, position);
        this.getBehaviour().setAggressive(true);
        this.getBehaviour().setRespawn(false);
        this.randomWalk = true;
    }

    int switchTargetTicks;
    Player target;
    private void attack() {
        if (target == null) { target = getRandomPlayer(); }
        switchTargetTicks++;
        if (switchTargetTicks >= 20) {
            switchTargetTicks = 0;
            target = getRandomPlayer();
        }
        asNPC().facePlayer(target.getIndex());
        this.faceEntityUpdateRequired = true;
        this.setPlayerAttackingIndex(target.getIndex());

        setNpcAutoAttacks(Lists.newArrayList(new ReaverAttack().apply(this)));
    }

    private Player getRandomPlayer() {
        Preconditions.checkState(!getInstance().getPlayers().isEmpty(), "No players!");
        List<Player> players = new ArrayList<>(getInstance().getPlayers());
        return players.get(Misc.random(players.size() - 1));
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return true;
    }

    @Override
    public void onDeath() {
        getInstance().remove(this);
    }

    @Override
    public int getDeathAnimation() {
        return 9192;
    }

    @Override
    public void process() {
        attack();
        processCombat();
    }

    private void processCombat() {
        super.process();
    }

}
