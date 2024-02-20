package io.xeros.content.worldevent.events;

import io.xeros.model.Npcs;
import io.xeros.model.entity.player.PlayerHandler;

import java.util.Arrays;
import java.util.List;

public class BossSpotlight {

    public static boolean active;
    public static Bosses selectedBoss;

    public enum Bosses {
        ZULRAH("Zulrah is", new int[] { Npcs.ZULRAH, Npcs.ZULRAH_2, Npcs.ZULRAH_3 }),
        GWD("Godwars Dungeon is", new int[] { Npcs.GENERAL_GRAARDOR, Npcs.COMMANDER_ZILYANA, Npcs.KRIL_TSUTSAROTH, Npcs.KREEARRA }),

        DAGANNOTH_KINGS("Dagannoth Kings are", new int[] { Npcs.DAGANNOTH_REX, Npcs.DAGANNOTH_SUPREME, Npcs.DAGANNOTH_PRIME }),

        KRAKEN("Kraken is", new int[] { Npcs.KRAKEN }),

        MUTANT_TARN("Mutant tarn is", new int[] { 6477 }),

        AVATAR("Avatar is", new int[] { 10532 }),

        CORPOREAL_BEAST("Corporeal Beast is", new int[] { Npcs.CORPOREAL_BEAST }),

        VORKATH("Vorkath is", new int[] { Npcs.VORKATH, Npcs.VORKATH_2, Npcs.VORKATH_3 }),

        KING_BLACK_DRAGON("King Black Dragon is", new int[] { Npcs.KING_BLACK_DRAGON }),

        WILDERNESS_BOSSES("Wilderness Bosses are", new int[] { Npcs.CRAZY_ARCHAEOLOGIST, Npcs.CHAOS_FANATIC, Npcs.CHAOS_ELEMENTAL,
                Npcs.VENENATIS_2, Npcs.SCORPIA, Npcs.CALLISTO_2, Npcs.VETION, Npcs.VETION_REBORN, Npcs.BRASSICAN_MAGE }),

        THE_NIGHTMARE("The Nightmare is", new int[] { Npcs.THE_NIGHTMARE }),

        NEX("Nex is", new int[] { 11278 }),

        ALCHEMICAL_HYDRA("Alchemical Hydra is", new int[] { Npcs.ALCHEMICAL_HYDRA, Npcs.ALCHEMICAL_HYDRA_2, Npcs.ALCHEMICAL_HYDRA_3,
                Npcs.ALCHEMICAL_HYDRA_4, Npcs.ALCHEMICAL_HYDRA_5, Npcs.ALCHEMICAL_HYDRA_6, Npcs.ALCHEMICAL_HYDRA_7, Npcs.ALCHEMICAL_HYDRA_8 }),

        CERBERUS("Cerberus is", new int[] { Npcs.CERBERUS }),

        ABYSSAL_SIRE("Abyssal Sire is", new int[] { Npcs.ABYSSAL_SIRE }),

        SEREN("Seren is", new int[] { Npcs.FRAGMENT_OF_SEREN_2 });

        private final String npcName;
        private final int[] npcId;

        Bosses(String npcName, int[] npcId) {
            this.npcName = npcName;
            this.npcId = npcId;
        }

        public String getNpcName() { return npcName; }
        public int[] getNpcId() { return npcId; }
    }

    public static void start() {
        active = true;

        List<Bosses> bosses = Arrays.asList(Bosses.values());
        selectedBoss = bosses.get((int) (Math.random() * bosses.size()));

        PlayerHandler.newsMessage(selectedBoss.getNpcName() + " now on spotlight, kill for 5% boosted drop rate!");
    }

    public static boolean isSpotlightNpc(int npcId) {
        if (!active || selectedBoss == null) {
            return false;
        }
        return Arrays.stream(selectedBoss.getNpcId()).anyMatch(i -> i == npcId);
    }

    public static void end() {
        PlayerHandler.newsMessage(selectedBoss.getNpcName() + " no longer on spotlight.");
        active = false;
        selectedBoss = null;
    }

}
