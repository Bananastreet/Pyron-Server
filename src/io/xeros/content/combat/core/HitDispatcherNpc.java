package io.xeros.content.combat.core;

import io.xeros.Configuration;
import io.xeros.content.bosses.Hunllef;
import io.xeros.content.bosses.Skotizo;
import io.xeros.content.combat.Damage;
import io.xeros.content.combat.effects.damageeffect.impl.amuletofthedamned.impl.AhrimEffect;
import io.xeros.content.combat.effects.damageeffect.impl.amuletofthedamned.impl.KarilEffect;
import io.xeros.content.combat.effects.damageeffect.impl.amuletofthedamned.impl.VeracsEffect;
import io.xeros.content.combat.magic.CombatSpellData;
import io.xeros.content.combat.weapon.WeaponInterface;
import io.xeros.content.items.PvpWeapons;
import io.xeros.content.questing.hftd.DagannothMother;
import io.xeros.content.skills.Skill;
import io.xeros.content.skills.mining.Pickaxe;
import io.xeros.model.CombatType;
import io.xeros.model.Items;
import io.xeros.model.Npcs;
import io.xeros.model.Spell;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.Entity;
import io.xeros.model.entity.npc.NPC;
import io.xeros.model.entity.npc.NPCHandler;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.EquipmentSet;
import io.xeros.util.Misc;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class HitDispatcherNpc extends HitDispatcher {

    public HitDispatcherNpc(Player attacker, Entity defender) {
        super(attacker, defender);
    }

    @Override
    public void beforeDamageCalculated(CombatType type) {
        NPC npc = (NPC) defender;
        attacker.getBossTimers().track(npc);
        StyleWarning.styleWarning(attacker, defender, type);
        int npcId = npc.getNpcId();
        if (type == CombatType.MELEE) {

            if (PvpWeapons.activateEffect(attacker, Items.VIGGORAS_CHAINMACE))
                PvpWeapons.degradeWeaponAfterCombat(attacker, false);

        } else if (type == CombatType.RANGE) {
            // Craws bow
            if (PvpWeapons.activateEffect(attacker, Items.CRAWS_BOW))
                PvpWeapons.degradeWeaponAfterCombat(attacker, false);

        } else if (type == CombatType.MAGE) {
            if (attacker.getSpellId() > -1) {
                int spellId = CombatSpellData.MAGIC_SPELLS[attacker.getSpellId()][0];
                int shield = attacker.playerEquipment[Player.playerShield];
                int staff = attacker.playerEquipment[Player.playerWeapon];
                Spell spell = Spell.forId(spellId);

                if (PvpWeapons.activateEffect(attacker, Items.THAMMARONS_SCEPTRE))
                    PvpWeapons.degradeWeaponAfterCombat(attacker, true);

            }
        }

        if (npcId == Npcs.CORPOREAL_BEAST) {
            WeaponInterface weaponInterface = attacker.getCombatConfigs().getWeaponData().getWeaponInterface();
            if (!(type == CombatType.MELEE && (weaponInterface == WeaponInterface.SPEAR || weaponInterface == WeaponInterface.HALBERD))) {
                maximumDamage /= 2;
                attacker.sendMessage("@red@Corporeal beast can only be effectively damaged with spears or halberds!",
                        TimeUnit.MINUTES.toMillis(10));
            }
        }

        // Demonic gorilla
        if (type == CombatType.MELEE && npcId == Npcs.DEMONIC_GORILLA
                || type == CombatType.RANGE && npcId == Npcs.DEMONIC_GORILLA_2
                || type == CombatType.MAGE && npcId == Npcs.DEMONIC_GORILLA_3) {
            maximumDamage = 0;
            maximumAccuracy = 0;
        }

        switch (npcId) {

            /**
             * Air based spells only
             */
            case 6373:
            case DagannothMother.AIR_PHASE:
                if (!CombatSpellData.airSpells(attacker)) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;

            /**
             * Water based spells only
             */
            case DagannothMother.WATER_PHASE:
            case 6375:
                if (!CombatSpellData.waterSpells(attacker)) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;

            /**
             * Fire based spells only
             */
            case DagannothMother.FIRE_PHASE:
            case 6376:
                if (!CombatSpellData.fireSpells(attacker)) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;

            /**
             * Earth based spells only
             */
            case DagannothMother.EARTH_PHASE:
            case 6378:
                if (!CombatSpellData.earthSpells(attacker)) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;

            /**
             * Melee only
             */
            case DagannothMother.MELEE_PHASE:
            case 6374:
                if (!attacker.usingMelee) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;

            /**
             * Range only
             */
            case DagannothMother.RANGE_PHASE:
            case 6377:
                if (!attacker.usingBow && !attacker.usingOtherRangeWeapons && !attacker.usingBallista) {
                    maximumDamage = 0;
                    maximumAccuracy = 0;
                }
                break;
        }

        if (npcId == Hunllef.MELEE_PROTECT && type == CombatType.MELEE
                || npcId == Hunllef.MAGE_PROTECT && type == CombatType.MAGE
                || npcId == Hunllef.RANGED_PROTECT && type == CombatType.RANGE) {
            maximumDamage = 0;
            maximumAccuracy = 0;
        }
    }

    /**
     * Anything that blocks damage totally should go in the method above and set
     * {@link HitDispatcher#maximumDamage} to zero.
     */
    @Override
    public void afterDamageCalculated(CombatType type, boolean successfulHit) {
        NPC defender = this.defender.asNPC();

        if (successfulHit) {
            if (AhrimEffect.INSTANCE.canUseEffect(attacker)) {
                AhrimEffect.INSTANCE.useEffect(attacker, defender, null);
            } else if (KarilEffect.INSTANCE.canUseEffect(attacker)) {
                KarilEffect.INSTANCE.useEffect(attacker, defender, new Damage(damage));
            } else if (VeracsEffect.INSTANCE.canUseEffect(attacker)) {
                VeracsEffect.INSTANCE.useEffect(attacker, null, null);
            }
        }

        if (ItemDef.forId(attacker.getItems().getWeapon()).getName().toLowerCase().contains("t3")) {
            damage *= 1.05;
        }

        if (ItemDef.forId(attacker.getItems().getWeapon()).getName().toLowerCase().contains("t4")) {
            damage *= 1.07;
        }

        if (ItemDef.forId(attacker.getItems().getWeapon()).getName().toLowerCase().contains("t5")) {
            damage *= 1.09;
        }

        if (defender.getNpcId() == Npcs.DEMONIC_GORILLA || defender.getNpcId() == Npcs.DEMONIC_GORILLA_2 || defender.getNpcId() == Npcs.DEMONIC_GORILLA_3) {
                if (attacker.hasPerk(Player.DEMONIC_DEFILER)) {
                    damage += 5;
                }
        }

        if (defender.getNpcId() == Npcs.ZULRAH || defender.getNpcId() == Npcs.ZULRAH_2 || defender.getNpcId() == Npcs.ZULRAH_3) {
            if (attacker.hasPerk(Player.SNAKE_CHARMER)) {
                damage += 5;
            }
        }

        if (Boundary.isIn(attacker, Boundary.REV_CAVE) && attacker.getItems().isWearingItem(Items.AMULET_OF_AVARICE)) {
            maximumAccuracy *= 1.2;
            damage *= 1.2;
        }

        if (defender.getNpcId() == 1673 || defender.getNpcId() == 1677 || defender.getNpcId() == 1672 || defender.getNpcId() == 1676 || defender.getNpcId() == 1675 || defender.getNpcId() == 1674) {
            if (attacker.hasPerk(Player.BARROWS_BASHER)) {
                damage *= 1.2;
            }
        }

        if (Boundary.isIn(attacker, Boundary.RAIDS) && attacker.hasPerk(Player.COX_CRUSHER)) {
            damage *= 1.2;
        }

        if (attacker.damageScrollActive) {
            damage *= 1.1;
        }

        if (attacker.getItems().isWearingItem(22949) && defender.getName().toLowerCase().contains("seasonal")) {
            damage *= 1.3;
        }

        if (attacker.getItems().isWearingItem(22950)) {
            if (Misc.hasOneOutOf(4)) {
                int gainAmount = Misc.random(2, 4);
                if (gainAmount + attacker.playerLevel[5] < 99) {
                    attacker.playerLevel[5] += gainAmount;
                    attacker.getPA().refreshSkill(5);
                }
            }
        }

        if (attacker.fiftyDamageScrollActive) {
            damage *= 1.5;
        }

        if (Boundary.isIn(attacker, Boundary.SEREN_ROOM) && attacker.hasPerk(Player.SEREN_SLAYER)) {
            damage *= 1.2;
        }

        if (Boundary.isIn(attacker, Boundary.Nex) && attacker.hasPerk(Player.NASTY_NEX)) {
            damage *= 1.2;
        }

        if (Boundary.isIn(attacker, Boundary.TARN_AREA) && attacker.hasPerk(Player.TARNISHED_DAMAGE)) {
            damage *= 1.1;
        }

        if (defender.getNpcId() == 7414 && attacker.getItems().isWearingItem(22946)) {
            damage *= 1.2;
        }

        if (Boundary.isIn(attacker, Boundary.INADEQUACY) && attacker.hasPerk(Player.ADEQUATE_DAMAGE)) {
            damage *= 1.1;
        }

        if (Boundary.isIn(attacker, Boundary.TOB_VERZIK) && attacker.hasPerk(Player.THEATRE_PERFORMER) || Boundary.isIn(attacker, Boundary.TOB) && attacker.hasPerk(Player.THEATRE_PERFORMER)) {
            damage *= 1.2;
        }

        if (attacker.getPosition().inWild() && attacker.hasPerk(Player.WILD_PVMER)) {
            damage *= 1.2;
        }

        if (Boundary.isIn(attacker, Boundary.CORPOREAL_BEAST_LAIR) && attacker.getItems().isWearingItem(29075)) {
            damage *= 1.5;
        }

        if (type == CombatType.MELEE && attacker.hasPerk(Player.NEXUS_WARRIOR)) {
            if (damage > 10) {
                damage *= 1.1;
            } else {
                damage += 1;
            }
        }

        if (type == CombatType.MELEE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "melee" && attacker.trinketOfTricksBoost == "positive") {
            damage *= 1.15;
        }

        if (type == CombatType.MELEE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "melee" && attacker.trinketOfTricksBoost == "negative") {
            damage *= 0.9;
        }

        if (type == CombatType.RANGE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "range" && attacker.trinketOfTricksBoost == "positive") {
            damage *= 1.15;
        }

        if (type == CombatType.RANGE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "range" && attacker.trinketOfTricksBoost == "negative") {
            damage *= 0.9;
        }

        if (type == CombatType.MAGE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "magic" && attacker.trinketOfTricksBoost == "positive") {
            damage *= 1.15;
        }

        if (type == CombatType.MAGE && attacker.trinketOfTricksActive && attacker.trinketOfTricksType == "magic" && attacker.trinketOfTricksBoost == "negative") {
            damage *= 0.9;
        }

        if (type == CombatType.RANGE && attacker.hasPerk(Player.NEXUS_ARCHER)) {
            if (damage > 10) {
                damage *= 1.1;
            } else {
                damage += 1;
            }
        }

        if (type == CombatType.MAGE && attacker.hasPerk(Player.NEXUS_MAGICIAN)) {
            if (damage > 10) {
                damage *= 1.1;
            } else {
                damage += 1;
            }
        }

        if (attacker.petSummonId == 12650 || attacker.petSummonId == 12649 || attacker.petSummonId == 12651 || attacker.petSummonId == 12652) {
            if (Boundary.isIn(attacker, Boundary.GODWARS_OTHER_ROOMS)) { //godwars pets
               damage *= 1.1;
            }
        }

        if (attacker.hasFollower && attacker.petSummonId == 30020 && Boundary.isIn(attacker, Boundary.CRYSTAL_CAVE_AREA)) {
            damage *= 1.15;
        }

        if (attacker.petSummonId == 12644 || attacker.petSummonId == 12645 || attacker.petSummonId == 12643) { //dks pets
            if (Boundary.isIn(attacker, Boundary.DAGANNOTH_KINGS)) {
                damage *= 1.1;
            }
        }

        if (attacker.npcAttackingIndex > 0) { //kbd pet
            NPC npc = NPCHandler.npcs[attacker.npcAttackingIndex];
            if (attacker.petSummonId == 12653) {
                if (Misc.linearSearch(Configuration.DRAG_IDS, npc.getNpcId()) != -1) {
                    damage *= 1.2;
                }
            }
        }

        if (attacker.petSummonId == 13247) { //hellpuppy pet
            if (Boundary.isIn(attacker, Boundary.CERBERUS_BOSSROOMS)) {
                damage *= 1.2;
            }
        }

        if (attacker.petSummonId == 22473) { //lil' zik pet
            if (Boundary.isIn(attacker, Boundary.TOB)) {
                damage *= 1.2;
            }
        }

        if (attacker.petSummonId == 26348) { //nexling pet
            if (Boundary.isIn(attacker, Boundary.Nex)) {
                damage *= 1.15;
            }
        }



        switch (type) {
            case MELEE:
                if (defender.getNpcId() == 6600) {
                    Pickaxe pickaxe = Pickaxe.getBestPickaxe(attacker);
                    if (pickaxe != null && attacker.getItems().isWearingItem(pickaxe.getItemId())) {
                        damage += Misc.random(pickaxe.getPriority() * 4);
                    }
                }

                if (attacker.petSummonId == 13178) { //callisto pet
                    if (Boundary.isIn(attacker, Boundary.WILDERNESS)) {
                        damage *= 1.1;
                    }
                }

                switch (defender.getNpcId()) {
                    case 7930:
                    case 7931:
                    case 7932:
                    case 7933:
                    case 7934:
                    case 7935:
                    case 7936:
                    case 7937:
                    case 7938:
                    case 7939:
                    case 7940:
                        if (attacker.playerEquipment[Player.playerHands] == 21816 && attacker.braceletEtherCount <= 0) {
                            attacker.getItems().equipItem(21817, 1, 9);
                        }
                        break;
                    case 7413://combat dummy
                        this.maximumAccuracy = 100.0;
                        damage = maximumDamage;
                        break;


                    case 2266:
                    case 2267:
                        damage = 0;
                        break;

                    case 965:
                        if (!EquipmentSet.VERAC.isWearingBarrows(attacker)) {
                            damage = 0;
                        }
                        break;
                }

                if (defender.getNpcId() == 5666) {
                    damage = damage / 4;
                    if (damage < 0) {
                        damage = 0;
                    }
                }
                break;
            case RANGE:
                if (attacker.petSummonId == 12921 || attacker.petSummonId == 12939 || attacker.petSummonId == 12940) { //zulrah pet
                    if (Boundary.isIn(attacker, Boundary.ZULRAH)) {
                        damage *= 1.15;
                    }
                }
                switch (defender.getNpcId()) {
                    case 7413://combat dummy
                        damage = maximumDamage;
                        maximumAccuracy = 100.0;
                        break;

                    case 2265:
                    case 2267:
                        damage = 0;
                        break;
                    case 8781:
                        int health1 = defender.getHealth().getCurrentHealth();
                        if (health1 > 500 && health1 < 600) {
                            defender.forceChat("YOU'LL NEVER TAKE ME DOWN!!");
                            defender.gfx0(1028);
                            defender.gfx0(1009);
                            attacker.attacking.reset();
                        }
                        break;
                    case 5890:
                        if (!attacker.getSlayer().getTask().isPresent()) {
                            return;
                        }
                        if (!attacker.getSlayer().getTask().isPresent() && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal demon") && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal sire")) {
                            attacker.sendMessage("The sire Fdoes not seem interested.");
                            attacker.attacking.reset();
                            return;
                        }
                        int health = defender.getHealth().getCurrentHealth();
                        if (health > 329) {
                            if (!CombatSpellData.shadowSpells(attacker)) {
                                //attacker.sendMessage("This would not be effective, I should try shadow spells.");
                                attacker.attacking.reset();
                            }
                        }
                        break;

                }
                break;
            case MAGE:
                if (attacker.petSummonId == 13179) { //vetion pet
                    if (Boundary.isIn(attacker, Boundary.WILDERNESS)) {
                        damage *= 1.2;
                    }
                }
                if (attacker.petSummonId == 11995) { //pet chaos elemental
                    damage *= 1.05;
                }
                if (attacker.petSummonId == 11995) { //pet scorpia
                    damage *= 1.08;
                }
                switch (defender.getNpcId()) {
                    case 7413://combat dummy
                        damage = maximumDamage;
                        maximumAccuracy = 100.0;
                        break;

                    case 5890:
                        if (!attacker.getSlayer().getTask().isPresent()) {
                            return;
                        }
                        if (!attacker.getSlayer().getTask().isPresent() && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal demon") && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal sire")) {
                            attacker.sendMessage("The sire does not seem interested.");
                            attacker.attacking.reset();
                            return;
                        }
                        int health = defender.getHealth().getCurrentHealth();
                        if (health > 329) {
                            if (!CombatSpellData.shadowSpells(attacker)) {
                               // attacker.sendMessage("This would not be effective, I should try shadow spells.");
                                attacker.attacking.reset();
                            }
                        }
                        break;
                    case 2265:
                    case 2266:
                        damage = 0;
                        break;
                }
                break;
            default:
                break;
        }

        if (defender.getNpcId() == Skotizo.SKOTIZO_ID) {
            damage = attacker.getSkotizo().calculateSkotizoHit(attacker, damage);
        }

        if (!defender.canBeDamaged(attacker)) {
            damage = 0;
            if (damage2 > 0) {
                damage2 = 0;
            }
        }
    }

}
