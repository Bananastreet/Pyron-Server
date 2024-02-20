package io.xeros.content.tiersystem

data class TierData(val tierLevel: Int, val itemID : Int, val tokensNeeded : Int, val replacmentItem : Int)

enum class UpgradeableItems(val maxTier : Int = 5, val teirs : List<TierData>,val info : String = "") {
    SCYTHE_OF_VITUR(teirs = listOf(
        TierData(itemID = 22325, tierLevel = 0, tokensNeeded = 15, replacmentItem = 29070),
        TierData(itemID = 29070, tierLevel = 1, tokensNeeded = 20, replacmentItem = 29071),
        TierData(itemID = 29071, tierLevel = 2, tokensNeeded = 25, replacmentItem = 29072),
        TierData(itemID = 29072, tierLevel = 3, tokensNeeded = 30, replacmentItem = 29073)
    ), info = "T2 Cost: 15\\n T3 Cost: 20\\n T4 Cost: 25\\n T5 Cost: 30"),
    GHRAZI_RAPIER(teirs = listOf(
        TierData(itemID = 22324, tierLevel = 0, tokensNeeded = 7, replacmentItem = 3692),
        TierData(itemID = 3692, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29013),
        TierData(itemID = 29013, tierLevel = 2, tokensNeeded = 12, replacmentItem = 29014),
        TierData(itemID = 29014, tierLevel = 3, tokensNeeded = 15, replacmentItem = 29015)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 12\\n T5 Cost: 15"),
    BOW_OF_FAERDHINEN(teirs = listOf(
        TierData(itemID = 25865, tierLevel = 0, tokensNeeded = 15, replacmentItem = 25867),
        TierData(itemID = 25867, tierLevel = 1, tokensNeeded = 20, replacmentItem = 25896),
        TierData(itemID = 25896, tierLevel = 2, tokensNeeded = 25, replacmentItem = 25888),
        TierData(itemID = 25888, tierLevel = 3, tokensNeeded = 30, replacmentItem = 25886)
    ), info = "T2 Cost: 15\\n T3 Cost: 20\\n T4 Cost: 25\\n T5 Cost: 30"),
    TWISTED_BOW(teirs = listOf(
        TierData(itemID = 20997, tierLevel = 0, tokensNeeded = 15, replacmentItem = 3694),
        TierData(itemID = 3694, tierLevel = 1, tokensNeeded = 25, replacmentItem = 3714),
        TierData(itemID = 3714, tierLevel = 2, tokensNeeded = 30, replacmentItem = 3715),
        TierData(itemID = 3715, tierLevel = 3, tokensNeeded = 35, replacmentItem = 3716)
    ), info = "T2 Cost: 15\\n T3 Cost: 25\\n T4 Cost: 30\\n T5 Cost: 35"),
    SANGUINESTI_STAFF(teirs = listOf(
        TierData(itemID = 22323, tierLevel = 0, tokensNeeded = 15, replacmentItem = 3696),
        TierData(itemID = 3696, tierLevel = 1, tokensNeeded = 18, replacmentItem = 29043),
        TierData(itemID = 29043, tierLevel = 2, tokensNeeded = 22, replacmentItem = 29044),
        TierData(itemID = 29044, tierLevel = 3, tokensNeeded = 25, replacmentItem = 29045)
    ), info = "T2 Cost: 15\\n T3 Cost: 18\\n T4 Cost: 22\\n T5 Cost: 25"),
    ELYSIAN_SPIRIT_SHIELD(teirs = listOf(
        TierData(itemID = 12817, tierLevel = 0, tokensNeeded = 5, replacmentItem = 29035),
        TierData(itemID = 29035, tierLevel = 1, tokensNeeded = 10, replacmentItem = 3711),
        TierData(itemID = 3711, tierLevel = 2, tokensNeeded = 15, replacmentItem = 3712),
        TierData(itemID = 3712, tierLevel = 3, tokensNeeded = 20, replacmentItem = 3713)
    ), info = "T2 Cost: 5\\n T3 Cost: 10\\n T4 Cost: 15\\n T5 Cost: 20"),
    ARCANE_SPIRIT_SHIELD(teirs = listOf(
        TierData(itemID = 12825, tierLevel = 0, tokensNeeded = 4, replacmentItem = 3723),
        TierData(itemID = 3723, tierLevel = 1, tokensNeeded = 8, replacmentItem = 3724),
        TierData(itemID = 3724, tierLevel = 2, tokensNeeded = 13, replacmentItem = 3725),
        TierData(itemID = 3725, tierLevel = 3, tokensNeeded = 20, replacmentItem = 3726)
    ), info = "T2 Cost: 4\\n T3 Cost: 8\\n T4 Cost: 13\\n T5 Cost: 20"),
    AVERNIC_DEFENDER(teirs = listOf(
        TierData(itemID = 22322, tierLevel = 0, tokensNeeded = 6, replacmentItem = 29038),
        TierData(itemID = 29038, tierLevel = 1, tokensNeeded = 8, replacmentItem = 29039),
        TierData(itemID = 29039, tierLevel = 2, tokensNeeded = 10, replacmentItem = 29040),
        TierData(itemID = 29040, tierLevel = 3, tokensNeeded = 12, replacmentItem = 29041)
    ), info = "T2 Cost: 6\\n T3 Cost: 8\\n T4 Cost: 10\\n T5 Cost: 12"),
    INFERNAL_CAPE(teirs = listOf(
        TierData(itemID = 21295, tierLevel = 0, tokensNeeded = 7, replacmentItem = 29016),
        TierData(itemID = 29016, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29017),
        TierData(itemID = 29017, tierLevel = 2, tokensNeeded = 12, replacmentItem = 29018),
        TierData(itemID = 29018, tierLevel = 3, tokensNeeded = 15, replacmentItem = 29019)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 12\\n T5 Cost: 15"),
    NECKLACE_OF_ZENITH(teirs = listOf(
        TierData(itemID = 23240, tierLevel = 0, tokensNeeded = 5, replacmentItem = 29036),
        TierData(itemID = 29036, tierLevel = 1, tokensNeeded = 12, replacmentItem = 29037),
        TierData(itemID = 29037, tierLevel = 2, tokensNeeded = 18, replacmentItem = 6200),
        TierData(itemID = 6200, tierLevel = 3, tokensNeeded = 27, replacmentItem = 6201)
    ), info = "T2 Cost: 5\\n T3 Cost: 12\\n T4 Cost: 18\\n T5 Cost: 27"),
    RING_OF_ZENITH(teirs = listOf(
        TierData(itemID = 25541, tierLevel = 0, tokensNeeded = 7, replacmentItem = 2951),
        TierData(itemID = 2951, tierLevel = 1, tokensNeeded = 18, replacmentItem = 2952),
        TierData(itemID = 2952, tierLevel = 2, tokensNeeded = 30, replacmentItem = 2953),
        TierData(itemID = 2953, tierLevel = 3, tokensNeeded = 40, replacmentItem = 2954)
    ), info = "T2 Cost: 7\\n T3 Cost: 18\\n T4 Cost: 30\\n T5 Cost: 40"),
    BOOTS_OF_ZENITH(teirs = listOf(
        TierData(itemID = 3693, tierLevel = 0, tokensNeeded = 7, replacmentItem = 3735),
        TierData(itemID = 3735, tierLevel = 1, tokensNeeded = 18, replacmentItem = 3736),
        TierData(itemID = 3736, tierLevel = 2, tokensNeeded = 30, replacmentItem = 3737),
        TierData(itemID = 3737, tierLevel = 3, tokensNeeded = 40, replacmentItem = 3738)
    ), info = "T2 Cost: 7\\n T3 Cost: 18\\n T4 Cost: 30\\n T5 Cost: 40"),
    ZENITH_GLOVES(teirs = listOf(
        TierData(itemID = 3717, tierLevel = 0, tokensNeeded = 5, replacmentItem = 3718),
        TierData(itemID = 3718, tierLevel = 1, tokensNeeded = 12, replacmentItem = 3719),
        TierData(itemID = 3719, tierLevel = 2, tokensNeeded = 20, replacmentItem = 3720),
        TierData(itemID = 3720, tierLevel = 3, tokensNeeded = 30, replacmentItem = 3721)
    ), info = "T2 Cost: 5\\n T3 Cost: 12\\n T4 Cost: 20\\n T5 Cost: 30"),
    AVAS_ASSEMBLER(teirs = listOf(
        TierData(itemID = 22109, tierLevel = 0, tokensNeeded = 7, replacmentItem = 29021),
        TierData(itemID = 29021, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29022),
        TierData(itemID = 29022, tierLevel = 2, tokensNeeded = 12, replacmentItem = 29023),
        TierData(itemID = 29023, tierLevel = 3, tokensNeeded = 15, replacmentItem = 29024)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 12\\n T5 Cost: 15"),
    ARMADYL_HELM(teirs = listOf(
        TierData(itemID = 11826, tierLevel = 0, tokensNeeded = 5, replacmentItem = 3869),
        TierData(itemID = 3869, tierLevel = 1, tokensNeeded = 7, replacmentItem = 3872),
        TierData(itemID = 3872, tierLevel = 2, tokensNeeded = 11, replacmentItem = 3875),
        TierData(itemID = 3875, tierLevel = 3, tokensNeeded = 15, replacmentItem = 3878)
    ), info = "T2 Cost: 5\\n T3 Cost: 7\\n T4 Cost: 11\\n T5 Cost: 15"),
    ARMADYL_CHESTPLATE(teirs = listOf(
        TierData(itemID = 11828, tierLevel = 0, tokensNeeded = 5, replacmentItem = 3870),
        TierData(itemID = 3870, tierLevel = 1, tokensNeeded = 10, replacmentItem = 3873),
        TierData(itemID = 3873, tierLevel = 2, tokensNeeded = 17, replacmentItem = 3876),
        TierData(itemID = 3876, tierLevel = 3, tokensNeeded = 23, replacmentItem = 3879)
    ), info = "T2 Cost: 5\\n T3 Cost: 10\\n T4 Cost: 17\\n T5 Cost: 23"),
    ARMADYL_CHAINSKIRT(teirs = listOf(
        TierData(itemID = 11830, tierLevel = 0, tokensNeeded = 5, replacmentItem = 3871),
        TierData(itemID = 3871, tierLevel = 1, tokensNeeded = 10, replacmentItem = 3874),
        TierData(itemID = 3874, tierLevel = 2, tokensNeeded = 15, replacmentItem = 3877),
        TierData(itemID = 3877, tierLevel = 3, tokensNeeded = 20, replacmentItem = 3880)
    ), info = "T2 Cost: 5\\n T3 Cost: 10\\n T4 Cost: 15\\n T5 Cost: 20"),
    ELITE_VOID_TOP(teirs = listOf(
        TierData(itemID = 13072, tierLevel = 0, tokensNeeded = 4, replacmentItem = 29027),
        TierData(itemID = 29027, tierLevel = 1, tokensNeeded = 6, replacmentItem = 29029),
        TierData(itemID = 29029, tierLevel = 2, tokensNeeded = 8, replacmentItem = 29031),
        TierData(itemID = 29031, tierLevel = 3, tokensNeeded = 10, replacmentItem = 29033)
    ), info = "T2 Cost: 4\\n T3 Cost: 6\\n T4 Cost: 8\\n T5 Cost: 10"),
    ELITE_VOID_ROBE(teirs = listOf(
        TierData(itemID = 13073, tierLevel = 0, tokensNeeded = 4, replacmentItem = 29026),
        TierData(itemID = 29026, tierLevel = 1, tokensNeeded = 6, replacmentItem = 29028),
        TierData(itemID = 29028, tierLevel = 2, tokensNeeded = 8, replacmentItem = 29030),
        TierData(itemID = 29030, tierLevel = 3, tokensNeeded = 10, replacmentItem = 29032)
    ), info = "T2 Cost: 4\\n T3 Cost: 6\\n T4 Cost: 8\\n T5 Cost: 10"),
    TORVA_HELM(teirs = listOf(
        TierData(itemID = 26382, tierLevel = 0, tokensNeeded = 5, replacmentItem = 29057),
        TierData(itemID = 29057, tierLevel = 1, tokensNeeded = 7, replacmentItem = 29060),
        TierData(itemID = 29060, tierLevel = 2, tokensNeeded = 9, replacmentItem = 29063),
        TierData(itemID = 29063, tierLevel = 3, tokensNeeded = 11, replacmentItem = 29066)
    ), info = "T2 Cost: 5\\n T3 Cost: 7\\n T4 Cost: 9\\n T5 Cost: 11"),
    TORVA_PLATEBODY(teirs = listOf(
        TierData(itemID = 26384, tierLevel = 0, tokensNeeded = 7, replacmentItem = 29058),
        TierData(itemID = 29058, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29061),
        TierData(itemID = 29061, tierLevel = 2, tokensNeeded = 11, replacmentItem = 29064),
        TierData(itemID = 29064, tierLevel = 3, tokensNeeded = 14, replacmentItem = 29067)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 11\\n T5 Cost: 14"),
    TORVA_PLATELEGS(teirs = listOf(
        TierData(itemID = 26386, tierLevel = 0, tokensNeeded = 7, replacmentItem = 29059),
        TierData(itemID = 29059, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29062),
        TierData(itemID = 29062, tierLevel = 2, tokensNeeded = 11, replacmentItem = 29065),
        TierData(itemID = 29065, tierLevel = 3, tokensNeeded = 14, replacmentItem = 29068)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 11\\n T5 Cost: 14"),
    CRYSTAL_HELM(teirs = listOf(
        TierData(itemID = 23971, tierLevel = 0, tokensNeeded = 7, replacmentItem = 23842),
        TierData(itemID = 23842, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29047),
        TierData(itemID = 29047, tierLevel = 2, tokensNeeded = 11, replacmentItem = 29050),
        TierData(itemID = 29050, tierLevel = 3, tokensNeeded = 13, replacmentItem = 29053)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 11\\n T5 Cost: 13"),
    CRYSTAL_BODY(teirs = listOf(
        TierData(itemID = 23975, tierLevel = 0, tokensNeeded = 7, replacmentItem = 23845),
        TierData(itemID = 23845, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29048),
        TierData(itemID = 29048, tierLevel = 2, tokensNeeded = 11, replacmentItem = 29051),
        TierData(itemID = 29051, tierLevel = 3, tokensNeeded = 13, replacmentItem = 29054)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 11\\n T5 Cost: 13"),
    CRYSTAL_LEGS(teirs = listOf(
        TierData(itemID = 23979, tierLevel = 0, tokensNeeded = 7, replacmentItem = 23848),
        TierData(itemID = 23848, tierLevel = 1, tokensNeeded = 9, replacmentItem = 29049),
        TierData(itemID = 29049, tierLevel = 2, tokensNeeded = 11, replacmentItem = 29052),
        TierData(itemID = 29052, tierLevel = 3, tokensNeeded = 13, replacmentItem = 29055)
    ), info = "T2 Cost: 7\\n T3 Cost: 9\\n T4 Cost: 11\\n T5 Cost: 13"),
    ANCESTRAL_HAT(teirs = listOf(
        TierData(itemID = 21018, tierLevel = 0, tokensNeeded = 5, replacmentItem = 29000),
        TierData(itemID = 29000, tierLevel = 1, tokensNeeded = 5, replacmentItem = 29003),
        TierData(itemID = 29003, tierLevel = 2, tokensNeeded = 5, replacmentItem = 29006),
        TierData(itemID = 29006, tierLevel = 3, tokensNeeded = 5, replacmentItem = 29009),
    ), info = "T2 Cost: 5\\n T3 Cost: 5\\n T4 Cost: 5\\n T5 Cost: 5"),
    ANCESTRAL_ROBE_TOP(teirs = listOf(
            TierData(itemID = 21021, tierLevel = 0, tokensNeeded = 6, replacmentItem = 29001),
            TierData(itemID = 29001, tierLevel = 1, tokensNeeded = 7, replacmentItem = 29004),
            TierData(itemID = 29004, tierLevel = 2, tokensNeeded = 8, replacmentItem = 29007),
            TierData(itemID = 29007, tierLevel = 3, tokensNeeded = 9, replacmentItem = 29010),
    ), info = "T2 Cost: 6\\n T3 Cost: 7\\n T4 Cost: 8\\n T5 Cost: 9"),
    ANCESTRAL_ROBE_BOTTOM(teirs = listOf(
            TierData(itemID = 21024, tierLevel = 0, tokensNeeded = 6, replacmentItem = 29002),
            TierData(itemID = 29002, tierLevel = 1, tokensNeeded = 7, replacmentItem = 29005),
            TierData(itemID = 29005, tierLevel = 2, tokensNeeded = 8, replacmentItem = 29008),
            TierData(itemID = 29008, tierLevel = 3, tokensNeeded = 9, replacmentItem = 29011),
    ), info = "T2 Cost: 6\\n T3 Cost: 7\\n T4 Cost: 8\\n T5 Cost: 9")
    ;

    companion object {
        fun getValidItems(): MutableMap<Int, TierData> {
            val itemsValid : MutableMap<Int,TierData> = emptyMap<Int, TierData>().toMutableMap()
            values().forEach {
                it.teirs.forEach { data ->
                    itemsValid[data.itemID] = data
                }
            }
            return itemsValid;
        }
    }

}