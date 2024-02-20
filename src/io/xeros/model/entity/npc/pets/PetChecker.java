package io.xeros.model.entity.npc.pets;

import io.xeros.model.entity.player.Player;

public class PetChecker {

    public static boolean shadowWarrior(Player player) {
        if (player.petSummonId == 30015) return true;
        return false;
    }

    public static boolean darkShadowWarrior(Player player) {
        if (player.petSummonId == 30115) return true;
        return false;
    }

    public static boolean shadowArcher(Player player) {
        if (player.petSummonId == 30016) return true;
        return false;
    }

    public static boolean darkShadowArcher(Player player) {
        if (player.petSummonId == 30116) return true;
        return false;
    }

    public static boolean shadowWizard(Player player) {
        if (player.petSummonId == 30017) return true;
        return false;
    }

    public static boolean darkShadowWizard(Player player) {
        if (player.petSummonId == 30117) return true;
        return false;
    }

}
