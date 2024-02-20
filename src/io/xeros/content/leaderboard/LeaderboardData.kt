package io.xeros.content.leaderboard

import io.xeros.content.leaderboard.impl.Boss
import io.xeros.content.leaderboard.impl.Boxes
import io.xeros.content.leaderboard.impl.Misc
import io.xeros.content.leaderboard.impl.Raid
import javax.swing.Box

enum class LeaderboardData(val verb : String,val content: List<ContentType>) {
    BOSSES("Kills",listOf(
        Boss.NEX, Boss.ZULRAH,Boss.MUTANT_TARN, Boss.MALEDICTUS, Boss.INADEQUACY, Boss.KRAKEN, Boss.SEREN, Boss.AVATAR_OF_DESTRUCTION,
        Boss.BRYOPHYTA, Boss.HUNLLEF, Boss.OBOR,
        Boss.DAGANNOTH_PRIME, Boss.DAGANNOTH_REX, Boss.DAGANNOTH_SUPREME,
        Boss.GIANT_MOLE, Boss.KALPHITE_QUEEN, Boss.LIZARDMAN_SHAMAN,
        Boss.SARACHNIS, Boss.THERMY,
        Boss.DEMONIC_GORILLAS, Boss.SARA_GWD, Boss.ZAMMY_GWD,
        Boss.BANDOS_GWD, Boss.ARMA_GWD, Boss.CORP, Boss.CERBERUS,
        Boss.ABYSSAL_SIRE, Boss.VORKATH, Boss.HYDRA, Boss.NIGHTMARE,
        Boss.BRASSICAN, Boss.KBD, Boss.VETION, Boss.CALLISTO,
        Boss.SCORPIA, Boss.VENENATIS, Boss.CHAOS_ELEMENTAL,
        Boss.CHAOS_FANATIC, Boss.CRAZY_ARCHEOLOGIST
    )),
    RAIDS("Runs",listOf(
        Raid.CHAMBER_OF_XERIC,Raid.THEATRE_OF_BLOOD
    )),
    MYSTERY_BOXES("Opened",listOf(
        Boxes.NORMAL,Boxes.SUPER,Boxes.ULTRA,Boxes.PVP, Boxes.Rares, Boxes.Godwars, Boxes.CHEST
    )),
    MISCELLANEOUS("Amount",listOf(
        Misc.DONATOR_TOKENS, Misc.EASY_CLUES, Misc.MEDIUM_CLUES,
        Misc.HARD_CLUES, Misc.MASTER_CLUES, Misc.TOURNAMENT_WINS, Misc.TOTAL_PRESTIGES, Misc.BOSS_CHALLENGES
    ))
}
