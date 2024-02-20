package io.xeros.content.bosses.nex;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.xeros.content.bosses.nex.Attacks.*;
import io.xeros.content.bosses.nex.Attacks.Specials.Blood.BloodSacrifice;
import io.xeros.content.bosses.nex.Attacks.Specials.Blood.BloodSiphon;
import io.xeros.content.bosses.nex.Attacks.Specials.Ice.Containment;
import io.xeros.content.bosses.nex.Attacks.Specials.Ice.IcePrison;
import io.xeros.content.bosses.nex.Attacks.Specials.Shadow.ShadowSmash;
import io.xeros.content.bosses.nex.Attacks.Specials.Smoke.Choke;
import io.xeros.content.bosses.nex.Attacks.Specials.Smoke.Drag;
import io.xeros.content.bosses.nex.Attacks.Specials.Wrath;
import io.xeros.content.bosses.nex.Minions.*;
import io.xeros.content.combat.Hitmark;
import io.xeros.content.combat.formula.MagicMaxHit;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.definitions.NpcDef;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.HealthStatus;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Position;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NexNPC extends NPC {

    public static Position spawnPosition = new Position(2924, 5202); //used for minions to face correctly
    enum Phases { SMOKE, SHADOW, BLOOD, ICE, ZAROS }
    private Phases phase;
    private void setPhase(Phases phase) {  this.phase = phase; }
    private Phases getPhase() { return this.phase; }
    public static boolean fumusAttackable, umbraAttackable, cruorAttackable, glaciesAttackable;
    private boolean spawnFinished;
    public boolean siphoning;
    private int specialTimer = 55;
    private final Nex instance;

    public NexNPC(int npcId, Position position, Nex instance) {
        super(npcId, position);
        getBehaviour().setRespawn(false);
        getBehaviour().setAggressive(false);
        getBehaviour().setRunnable(true);
        this.instance = instance;
        this.setPhase(Phases.SMOKE);
        instance.add(this);
        startSpawnAnim(this);
        instance.add(new Umbra(position));
        instance.add(new Glacies(position));
        instance.add(new Fumus(position));
        instance.add(new Cruor(position));
    }

    int switchTargetTicks;
    Player target;
    private void attack() {
        if (spawnFinished) {
            if (target == null) { target = getRandomPlayer(); }
            if (target.isDead()) { target = getRandomPlayer(); }
            try {
                if (!target.getInstance().equals(this.getInstance())) { target = getRandomPlayer(); }
            } catch (NullPointerException e) {
                target = getRandomPlayer();
            }
            if (!Boundary.isIn(target, Boundary.Nex)) { target = getRandomPlayer(); }
            switchTargetTicks++;
            if (switchTargetTicks >= 20) {
                switchTargetTicks = 0;
                target = getRandomPlayer();
            }
            if (target != null) {
                if (target.getPosition().getAbsDistance(asNPC().getPosition()) > 10) {
                    this.teleport(target.getPosition());
                    this.startAnimation(9182);
                }
                asNPC().facePlayer(target.getIndex());
                this.faceEntityUpdateRequired = true;
                this.setPlayerAttackingIndex(target.getIndex());
            }
        }

        switch (getPhase()) {
            case SMOKE:
                if (specialTimer <= 0) {
                    specialTimer = 55;
                        new Drag(getPlayerWithin6Tiles(), this);
                } else if (target != null && target.getPosition().getAbsDistance(asNPC().getPosition()) < 3) {
                    setNpcAutoAttacks(Lists.newArrayList(new SmokeMelee().apply(this)));
                } else {
                    setNpcAutoAttacks(Lists.newArrayList(new SmokeRush().apply(this)));
                }
                break;

            case SHADOW:
                if (specialTimer <= 0) {
                    specialTimer = 55;
                    new ShadowSmash(this);
                } else {
                    setNpcAutoAttacks(Lists.newArrayList(new ShadowShot().apply(this)));
                }
                break;

            case BLOOD:
                if (specialTimer <= 0) {
                    specialTimer = 55;
                    if (Misc.random(1) == 0) {
                        new BloodSiphon(this);
                    } else {
                        new BloodSacrifice(this, getRandomPlayer());
                    }
                } else {
                    setNpcAutoAttacks(Lists.newArrayList(new BloodBarrage().apply(this)));
                }
                break;

            case ICE:
                if (specialTimer <= 0) {
                    specialTimer = 55;
                    if (Misc.random(1) == 0) {
                        IcePrison.start(getRandomPlayer(), this);
                    } else {
                        Containment.start(this);
                    }
                } else {
                    setNpcAutoAttacks(Lists.newArrayList(new IceBarrage().apply(this)));
                }
                break;

            case ZAROS:
                setNpcAutoAttacks(Lists.newArrayList(new ZarosMage().apply(this)));
                break;
        }

    }

    boolean calledForMinion;
    boolean usedWrath = false;
    private void postHit() {
        var health = this.getHealth().getCurrentHealth();
        if (health <= 2720 && getPhase() == Phases.SMOKE) {
            if (!calledForMinion) {
                this.forceChat("Fumus, don't fail me!");
                sendMessage("Nex is invincible until you kill Fumus!");
                calledForMinion = true;
            }
            fumusAttackable = true;
            CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (!fumusAttackable) {
                        container.stop();
                    }
                }
                @Override
                public void onStopped() {
                    forceChat("Darken my shadow!");
                    calledForMinion = false;
                    setPhase(Phases.SHADOW);
                }
            }, 1);
            return;
        }

        if (health <= 2040 && getPhase() == Phases.SHADOW) {
            if (!calledForMinion) {
                this.forceChat("Umbra, don't fail me!");
                sendMessage("Nex is invincible until you kill Umbra!");
                calledForMinion = true;
            }
            umbraAttackable = true;
            CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (!umbraAttackable) {
                        container.stop();
                    }
                }
                @Override
                public void onStopped() {
                    forceChat("Flood my lungs with blood!");
                    calledForMinion = false;
                    setPhase(Phases.BLOOD);
                }
            }, 1);
            return;
        }

        if (health <= 1360 && getPhase() == Phases.BLOOD) {
            if (!calledForMinion) {
                this.forceChat("Cruor, don't fail me!");
                sendMessage("Nex is invincible until you kill Cruor!");
                calledForMinion = true;
            }
            cruorAttackable = true;
            CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (!cruorAttackable) {
                        container.stop();
                    }
                }
                @Override
                public void onStopped() {
                    forceChat("Infuse me with the power of ice!");
                    calledForMinion = false;
                    setPhase(Phases.ICE);

                    //kills any remaining blood reavers
                    for (int i = 0; i < getInstance().getNpcs().size(); i++) {
                        if (getInstance().getNpcs().get(i).getNpcId() == BloodReaver.npcId) {
                            getInstance().getNpcs().get(i).setDead(true);
                        }
                    }

                }
            }, 1);
            return;
        }

        if (health <= 680 && getPhase() == Phases.ICE) {
            if (!calledForMinion) {
                this.forceChat("Glacies, don't fail me!");
                sendMessage("Nex is invincible until you kill Glacies!");
                calledForMinion = true;
            }
            glaciesAttackable = true;
            CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (!glaciesAttackable) {
                        container.stop();
                    }
                }
                @Override
                public void onStopped() {
                    forceChat("NOW, THE POWER OF ZAROS!");
                    calledForMinion = false;
                    startAnimation(9179);
                    gfx100(2016);
                    setPhase(Phases.ZAROS);

                    getHealth().setCurrentHealth(health + 500);

                }
            }, 1);
        }

        if (health < 1 && !usedWrath) {
            new Wrath(this);
            usedWrath = true;
        }
    }

    private void sendMessage(String message) {
        getInstance().getPlayers().forEach(player -> player.sendMessage("@red@" + message));
    }

    private Player getLowestMageDefence() {
        Preconditions.checkState(!getInstance().getPlayers().isEmpty(), "No players!");
        List<Player> players = getInstance().getPlayers().stream().sorted(Comparator.comparingInt(MagicMaxHit::mageDefence)).collect(Collectors.toList());
        return players.get(0);
    }

    private Player getRandomPlayer() {
        Preconditions.checkState(!getInstance().getPlayers().isEmpty(), "No players!");
        List<Player> players = new ArrayList<>(getInstance().getPlayers());
        return players.get(Misc.random(players.size() - 1));
    }

    private Player getPlayerWithin6Tiles() {
        Preconditions.checkState(!getInstance().getPlayers().isEmpty(), "No players!");
        List<Player> players = getInstance().getPlayers().stream().filter(player -> player.getDistance(this.getX(), this.getY()) <= 6).collect(Collectors.toList());
        return players.isEmpty() ? null : players.get(Misc.random(players.size() - 1));
    }

    @Override
    public int modifyDamage(Player player, int damage) {
        if (this.getPhase() == Phases.BLOOD && siphoning) {
            this.appendHeal(damage, Hitmark.HEAL_PURPLE);
            return 0;
        }
        return damage;
    }

    @Override
    public boolean susceptibleTo (HealthStatus status){
        return false;
    }

    @Override
    public boolean hasBlockAnimation () {
        return false;
    }

    @Override
    public boolean canBeDamaged (Entity entity) {
        if (fumusAttackable || glaciesAttackable || cruorAttackable || umbraAttackable) {
            return false;
        }
        return !isDead() && !isInvisible();
    }

    @Override
    public boolean isAutoRetaliate () {
        return !isDead() && !isInvisible() && getBehaviour().isAggressive();
    }

    @Override
    public boolean canBeAttacked (Entity entity) {
        return spawnFinished;
    }

    @Override
    public void process() {
        checkInstance();
        postHit();
        attack();
        specialTimer--;
        processCombat();
    }

    private void checkInstance() {
        if (this.getInstance().getPlayers().isEmpty()) {
            if (!this.getInstance().isDisposed()) {
                this.getInstance().dispose();
            }
        }
    }

    private void processCombat() {
        super.process();
    }

    @Override
    public int getDeathAnimation() {
        return 9184;
    }

    @Override
    public void onDeath() {
        super.onDeath();

        for (Player player : instance.getPlayers()) {
            NexDrops.sendDrops(player, instance, this.getPosition());
            //player.moveTo(new Position(2904, 5206, 0));
        }

    }

    @Override
    public int getSize() {
        return NpcDef.forId(getNpcId()).getSize();
    }

    private void startSpawnAnim(NPC nex) {
        spawnFinished = false;
        CycleEventHandler.getSingleton().addEvent(nex, new CycleEvent() {
            int ticks = 0;
            @Override
            public void execute(CycleEventContainer container) {
                if (nex == null) {
                    return;
                }
                switch (ticks++) {
                    case 1:
                        nex.startAnimation(9182);
                        break;
                    case 3:
                        nex.forceChat("AT LAST!");
                        break;
                    case 7:
                        nex.facePosition(Fumus.fumusX, Fumus.fumusY);
                        nex.startAnimation(9189);
                        nex.forceChat("Fumus!");
                        break;
                    case 11:
                        nex.facePosition(Umbra.umbraX, Umbra.umbraY);
                        nex.startAnimation(9189);
                        nex.forceChat("Umbra!");
                        break;
                    case 15:
                        nex.facePosition(Cruor.cruorX, Cruor.cruorY);
                        nex.startAnimation(9189);
                        nex.forceChat("Cruor!");
                        break;
                    case 19:
                        nex.facePosition(Glacies.glaciesX, Glacies.glaciesY);
                        nex.startAnimation(9189);
                        nex.forceChat("Glacies!");
                        break;
                    case 23:
                        nex.forceChat("Fill my soul with smoke!");
                        break;
                    case 25:
                        container.stop();
                        break;
                }
            }
            @Override
            public void onStopped() {
                ticks = 0;
                spawnFinished = true;
                getBehaviour().setAggressive(true);
            }
        }, 1);
    }

}
