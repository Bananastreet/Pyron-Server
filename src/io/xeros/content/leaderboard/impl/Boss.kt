package io.xeros.content.leaderboard.impl

import io.xeros.content.leaderboard.ContentType
import io.xeros.content.leaderboard.LeaderboardData

enum class Boss(val kcName : String = "") : ContentType {
    NEX("nex"),
    ZULRAH("zulrah"),
    MUTANT_TARN("mutant tarn"),
    MALEDICTUS("maledictus"),
    INADEQUACY("the inadequacy"),
    KRAKEN("kraken"),
    SEREN("fragment of seren"),
    AVATAR_OF_DESTRUCTION("avatar of destruction"),
    BRYOPHYTA("bryophyta"),
    HUNLLEF("hunllef"),
    OBOR("obor"),
    DAGANNOTH_PRIME("dagannoth prime"),
    DAGANNOTH_REX("dagannoth rex"),
    DAGANNOTH_SUPREME("dagannoth supreme"),
    GIANT_MOLE("giant mole"),
    KALPHITE_QUEEN("kalphite queen"),
    LIZARDMAN_SHAMAN("lizardman shaman"),
    SARACHNIS("sarachnis"),
    THERMY("thermonuclear smoke devil"),
    DEMONIC_GORILLAS("demonic gorilla"),
    SARA_GWD("commander zilyana"),
    ZAMMY_GWD("k'ril tsutsaroth"),
    BANDOS_GWD("general graardor"),
    ARMA_GWD("kree'arra"),
    CORP("corporeal beast"),
    CERBERUS("cerberus"),
    ABYSSAL_SIRE("abyssal sire"),
    VORKATH("vorkath"),
    HYDRA("alchemical hydra"),
    NIGHTMARE("the nightmare"),
    BRASSICAN("brassican"),
    KBD("king black dragon"),
    VETION("vetion"),
    CALLISTO("callisto"),
    SCORPIA("scorpia"),
    VENENATIS("venenatis"),
    CHAOS_ELEMENTAL("chaos elemental"),
    CHAOS_FANATIC("chaos fanatic"),
    CRAZY_ARCHEOLOGIST("crazy archeologist");

    companion object {

        fun getBoss(identifier : String) : Boss? {
            return Boss.values().find { it.kcName == identifier }
        }
    }

}
