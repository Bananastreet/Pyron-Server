package io.xeros.content.perks;

import io.xeros.model.PermanentAttributes.PermanentAttributeKey;
import io.xeros.model.entity.player.Player;

public enum Perks {
    /* buttonId will always start at 148145 in each category, and increment 4 for each
    additional perk in that category. The permanent attribute key needs to be declared in Player.java.
    The current max is 20 perks per category. This can be raised client sided in PerkInterface.java. */

    /**
     * Combat Perks
     */
    ABYSSAL_WARRIOR("Abyssal Warrior", PerkCategories.COMBAT, 5, 148145, Player.ABYSSAL_WARRIOR,
            "When using an Abyssal whip or\\n\\ntentacle, +2 damage will be added to\\n\\nevery hit above 0."),

    DEMONIC_DEFILER("Demonic Defiler", PerkCategories.COMBAT, 5, 148149, Player.DEMONIC_DEFILER,
            "Damage done to demonic gorillas will\\n\\nbe 5 higher each hit\\n"),

    SNAKE_CHARMER("Snake Charmer", PerkCategories.COMBAT, 7, 148153, Player.SNAKE_CHARMER,
            "Damage done to Zulrah will be 5 higher\\n\\neach hit\\n"),

    WILD_PVMER("Wild Pvmer", PerkCategories.COMBAT, 15, 148157, Player.WILD_PVMER,
            "Damage done to npcs while in the wild\\n\\nwill be 20% higher\\n"),

    COX_CRUSHER("Cox Crusher", PerkCategories.COMBAT, 15, 148161, Player.COX_CRUSHER,
            "Damage done to npcs while in \\n\\nChambers of Xeric will be 20% higher\\n"),

    NEXUS_WARRIOR("Pyron Warrior", PerkCategories.COMBAT, 12, 148165, Player.NEXUS_WARRIOR,
            "Damage done to npcs with melee will \\n\\nbe an additional 10% higher globally.\\n"),

    NEXUS_ARCHER("Pyron Archer", PerkCategories.COMBAT, 12, 148169, Player.NEXUS_ARCHER,
            "Damage done to npcs with range will \\n\\nbe an additional 10% higher globally.\\n"),

    NEXUS_MAGICIAN("Pyron Magician", PerkCategories.COMBAT, 12, 148173, Player.NEXUS_MAGICIAN,
            "Damage done to npcs with magic will \\n\\nbe an additional 10% higher globally.\\n"),

    CRYSTAL_RANGER("Crystal Ranger", PerkCategories.COMBAT, 12, 148177, Player.CRYSTAL_RANGER,
            "Any bow of faerdhinen will have 10% \\n\\nmore damage if full crystal is\\n\\nequipped (any or mixed tier).\\n\\n"),

    CORRUPT_RANGER("Corrupt Ranger", PerkCategories.COMBAT, 12, 148181, Player.CORRUPT_RANGER,
            "Any bow of faerdhinen will have 5 \\n\\nadded to its max hit when any\\n\\ncombination of full crystal\\n\\n is worn."),

    THEATRE_PERFORMER("Theatre Performer", PerkCategories.COMBAT, 15, 148185, Player.THEATRE_PERFORMER,
            "Damage done to npcs while in \\n\\nTheatre of Blood will be 20% higher\\n"),

    BARROWS_BASHER("Barrows Basher", PerkCategories.COMBAT, 2, 148189, Player.BARROWS_BASHER,
            "Damage done to barrows brothers \\n\\nwill be 20% higher\\n"),

    NASTY_NEX("Nasty Nex", PerkCategories.COMBAT, 15, 148193, Player.NASTY_NEX,
            "Damage done while at Nex will be 20%\\n\\nhigher, including minions\\n"),

    SEREN_SLAYER("Seren Slayer", PerkCategories.COMBAT, 15, 148197, Player.SEREN_SLAYER,
            "Damage done to seren will be \\n\\n15% higher.\\n"),

    ARCANE_MAGE("Arcane Mage", PerkCategories.COMBAT, 5, 148201, Player.ARCANE_MAGE,
            "Magic max hit will be 5 higher \\n\\nfor all magic attacks.\\n"),

    BULWARK_BUILD("Bulwark build", PerkCategories.COMBAT, 20, 148205, Player.BULWARK_BUILD,
            "50% chance damage outside of the \\n\\nwild is reduced by 40%.\\n"),

    TORVA_WARRIOR("Torva Warrior", PerkCategories.COMBAT, 20, 148209, Player.TORVA_WARRIOR,
            "Damage while wearing any tier(s) set \\n\\nof torva increased 15%.\\n"),

    ANCESTRAL_MAGE("Ancestral Mage", PerkCategories.COMBAT, 15, 148213, Player.ANCESTRAL_MAGE,
            "Damage while wearing any tier(s) set \\n\\nof ancestral increased 15%.\\n"),

    CANNON_CAPACITY("Cannon Capacity", PerkCategories.COMBAT, 15, 148217, Player.CANNON_CAPACITY,
            "Doubles your current cannon capacity."),

    SPECIAL_SURPRISE("Special Surprise", PerkCategories.COMBAT, 20, 148221, Player.SPECIAL_SURPRISE,
            "Special attack restores 2x faster."),

    TARNISHED_DAMAGE("Tarnished Damage", PerkCategories.COMBAT, 15, 148225, Player.TARNISHED_DAMAGE,
            "10% more damage dealt to Tarn"),

    ADEQUATE_DAMAGE("Adequate Damage", PerkCategories.COMBAT, 15, 148229, Player.ADEQUATE_DAMAGE,
            "10% more damage dealt to \\n\\nthe inadequacy."),


    /**
     * Boost Perks
     */
    RING_OF_WEALTHIER("Ring of Wealthier", PerkCategories.BOOST, 15, 148145, Player.RING_OF_WEALTHIER,
            "When purchased, the row(i) passive\\n\\nwill increase to 10%.\\n\\n\\n@red@The row(i) must be equipped for this to\\n@red@be active."),

    GRIND_HATER("Grind Hater", PerkCategories.BOOST, 15, 148149, Player.GRIND_HATER,
            "Experience gained is 10% higher, \\n\\nstacks with other boosts.\\n\\n\\n"),

    TOURNY_BOOSTER("Tourny Booster", PerkCategories.BOOST, 5, 148153, Player.TOURNY_BOOSTER,
            "You will receive 2 extra points from\\n\\nevery tournament.\\n\\n\\n"),

    SLAYER_BOOSTER("Slayer Booster", PerkCategories.BOOST, 5, 148157, Player.SLAYER_BOOSTER,
            "You will receive 5 extra points from\\n\\nevery slayer task.\\n\\n\\n"),

    BLOOD_STEALER("Blood Stealer", PerkCategories.BOOST, 7, 148161, Player.BLOOD_STEALER,
            "Blood shards will be 1 in 1500 from\\n\\npickpocketing vyres, rather than \\n\\n1 in 2500.\\n"),

    COX_BOOSTER("Cox Booster", PerkCategories.BOOST, 15, 148165, Player.COX_BOOSTER,
            "Increases your odds of receiving a \\n\\nrare key from CoX by 2 each raid. \\n\\n\\n"),

    PEST_CONTROLLER("Pest Controller", PerkCategories.BOOST, 5, 148169, Player.PEST_CONTROLLER,
            "You will receive 3 more points each \\n\\ngame of pest control. \\n\\n\\n"),

    UPGRADED_RAID("Upgraded Raid", PerkCategories.BOOST, 15, 148173, Player.UPGRADED_RAID,
            "Odds of receiving an upgrade token \\n\\nfrom ToB or CoX will be higher. \\n\\n\\n"),

    PERKY_JERKY("Perky Jerky", PerkCategories.BOOST, 15, 148177, Player.PERKY_JERKY,
            "Odds of receiving an perk tickets \\n\\nas a drop will be 2x higher. \\n\\n\\n"),

    PET_CEMETARY("Pet Cemetary", PerkCategories.BOOST, 15, 148181, Player.PET_CEMETARY,
            "Odds of receiving any pets will be \\n\\ntwice as high. \\n\\n\\n"),

    BOSS_BOOST("Boss Boost", PerkCategories.BOOST, 15, 148185, Player.BOSS_BOOST,
            "Receive 2x boss points from kills."),

    EXCHANGE_MASTER("Exchange Master", PerkCategories.BOOST, 25, 148189, Player.EXCHANGE_MASTER,
            "Receive 10% more tickets when \\n\\nselling to exchange store."),

    BOSS_CHALLENGER("Boss Challenger", PerkCategories.BOOST, 25, 148193, Player.BOSS_CHALLENGER,
            "20% chance of receiving two  \\n\\nboxes after a boss challenge."),


    /**
     * Other Perks
     */
    CHEST_HUNTER("Chest Hunter", PerkCategories.OTHER, 10, 148145, Player.CHEST_HUNTER,
            "Provides 10% higher chance of\\n\\nreceiving a chest key."),

    RAW_COLLECTOR("Raw Collector", PerkCategories.OTHER, 4, 148149, Player.RAW_COLLECTOR,
            "Receive 3x fish while fishing, stacks\\n\\nwith fishing skillcape for 6x."),

    MASTER_CHEF("Master Chef", PerkCategories.OTHER, 3, 148153, Player.MASTER_CHEF,
            "Fish will never burn when cooking"),

    MIGHTY_MINER("Mighty Miner", PerkCategories.OTHER, 4, 148157, Player.MIGHTY_MINER,
            "50% chance of mining rewards being\\n\\ndoubled.\\n"),

    CRYSTAL_COLLECTOR("Crystal Collector", PerkCategories.OTHER, 4, 148161, Player.CRYSTAL_COLLECTOR,
            "Crystal key drops will go to your \\n\\ninventory and be doubled.\\n"),

    COIN_COLLECTOR("Coin Collector", PerkCategories.OTHER, 10, 148165, Player.COIN_COLLECTOR,
            "Coin bag drops will go to your \\n\\ninventory and be doubled.\\n"),

    RESOURCE_COLLECTOR("Resource Collector", PerkCategories.OTHER, 5, 148169, Player.RESOURCE_COLLECTOR,
            "Resource box drops will go to your \\n\\ninventory and be doubled.\\n"),

    HUNLLEF_DOUBLER("Hunllef Doubler", PerkCategories.OTHER, 15, 148173, Player.HUNLLEF_DOUBLER,
            "Gives a 25% chance of getting 2 keys\\n\\nafter defeating Hunllef."),

    PYRO_LUMBERJACK("Pyro Lumberjack", PerkCategories.OTHER, 5, 148177, Player.PYRO_LUMBERJACK,
            "Gives a 50% chance of incinerating \\n\\nlogs when chopped, providing\\n\\nfiremaking experience."),

    MASTER_SMITHER("Master Smither", PerkCategories.OTHER, 5, 148181, Player.MASTER_SMITHER,
            "Gives a 50% chance of saving coal \\n\\nwhen smithing, and 100% chance of\\n\\nsmelting iron bars."),

    POCKET_CRUSHER("Pocket Crusher", PerkCategories.OTHER, 8, 148185, Player.POCKET_CRUSHER,
            "Acts as a bonecrusher, instantly\\n\\nburying bones dropped by npcs for\\n\\nprayer experience."),

    HERB_STACKER("Herb Stacker", PerkCategories.OTHER, 5, 148189, Player.HERB_STACKER,
            "When harvesting herbs, they will\\n\\nautomatically be noted."),

    OVERLOADED("Overloaded", PerkCategories.OTHER, 10, 148193, Player.OVERLOADED,
            "When drinking an overload, no damage\\n\\nwill be taken."),

    LONG_OVERLOADED("Long Overloaded", PerkCategories.OTHER, 13, 148197, Player.LONG_OVERLOADED,
            "Overload boost lasts twice as long."),

    PRO_SALESMAN("Pro Salesman", PerkCategories.OTHER, 13, 148201, Player.PRO_SALESMAN,
            "Items sold to the sell me anything \\n\\nstore will give 30% more."),

    THE_ACCUMULATOR("The Accumulator", PerkCategories.OTHER, 7, 148205, Player.THE_ACCUMULATOR,
            "Range arrows will have an 80% \\n\\nchance of being saved."),

    BANK_HOARDER("Bank Hoarder", PerkCategories.OTHER, 15, 148209, Player.BANK_HOARDER,
            "500 Additional Bank Spaces."),

    GOT_THE_BAG("Got The Bag", PerkCategories.OTHER, 15, 148213, Player.GOT_THE_BAG,
            "Looting bag can be used anywhere."),

    MEGA_RESTORE("Mega Restore", PerkCategories.OTHER, 10, 148217, Player.MEGA_RESTORE,
            "Restoring potions restore 10 more."),

    SEASONAL_PICKUP("Seasonal Pickup", PerkCategories.OTHER, 10, 148221, Player.SEASONAL_PICKUP,
            "Seasonal ticket drops automatically\\n\\ngo to inventory or bank.");



    private final String perkName;
    private final PerkCategories category;
    private final int price;
    private final int buttonId;
    private final PermanentAttributeKey<?> key;
    private final String description;

    Perks(String perkName, PerkCategories category, int price, int buttonId, PermanentAttributeKey<?> key, String description) {
        this.perkName = perkName;
        this.category = category;
        this.price = price;
        this.buttonId = buttonId;
        this.key = key;
        this.description = description;
    }

    public String getPerkName() { return perkName; }
    public PerkCategories getCategory() { return category; }
    public int getPrice() { return price; }
    public int getButtonId() { return buttonId; }
    public PermanentAttributeKey<?> getKey() { return key; }
    public String getDescription() { return description; }

}


