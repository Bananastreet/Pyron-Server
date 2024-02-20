package io.xeros.content.teleportation.inter;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import io.xeros.Configuration;
import io.xeros.content.achievement_diary.impl.*;
import io.xeros.content.bosses.maledictus.MaledictusInstance;
import io.xeros.content.bosses.obor.OborInstance;
import io.xeros.content.bosses.tarn.TarnInstance;
import io.xeros.content.bosses.ulfric.ulfricInstance;
import io.xeros.model.entity.player.Position;
import io.xeros.model.entity.player.Right;
import io.xeros.model.entity.player.mode.ModeType;
import io.xeros.model.items.ItemAssistant;

public class TeleportInterfaceConstants {

    public static final List<Integer> TELEPORT_BUTTON_ID_LIST;
    public static final List<Integer> TELEPORT_MESSAGE_ID_LIST;

    private static final int TELEPORT_LIST_LENGTH = 40;
    private static final int TELEPORT_LIST_START_ID = 44_745;
    private static final int TELEPORT_LIST_ID_INTERVAL = 4;

    /**
     * This requires editing of the client (Interfaces#teleportInterface#teleportAmount) if changed.
     */
    public static final int TELEPORT_LIST_COUNT = 40;

    static {
        List<Integer> buttonList = Lists.newArrayList();
        for (int buttonId = 44_742; buttonId <= (44_742 + (TELEPORT_LIST_COUNT * 4)); buttonId += 4)
            buttonList.add(buttonId);
        TELEPORT_BUTTON_ID_LIST = Collections.unmodifiableList(Lists.newArrayList(buttonList));

        List<Integer> messageIdList = Lists.newArrayList();
        for (int index = 0; index < TELEPORT_LIST_LENGTH; index++) {
            messageIdList.add(TELEPORT_LIST_START_ID + (index * TELEPORT_LIST_ID_INTERVAL));
        }
        TELEPORT_MESSAGE_ID_LIST = Collections.unmodifiableList(Lists.newArrayList(messageIdList));
    }

    public static final int[] TAB_SELECTION_BUTTON_IDS = {39_706, 39_711, 39_716, 39_721, 39_726, 39_731, 39_736};

    public static final int INTERFACE_ID = 39_700;
    public static final int INTERFACE_SCROLLABLE_ID = 44_741;

    private static final TeleportContainer MONSTERS = new TeleportContainer(Lists.newArrayList(
            new TeleportButton("Catacombs", plr -> {
                if (plr.getPA().canTeleport()) {
                    plr.getPA().movePlayer(1664, 10050, 0);
                    plr.getPA().showInterface(33900);
                    plr.sendMessage("@cr10@ Use @pur@::level2@bla@ to be sent to @pur@next catacombs level.");
                }
            }),
            new TeleportButtonStandard("Corrupted Dungeon", new Position(2861, 4954, 0)),
            new TeleportButtonStandard("Rock Crabs", new Position(2673, 3710, 0)),
            new TeleportButtonStandard("Jelly Room", new Position(1330, 9967, 0)),
            new TeleportButtonStandard("Cows", new Position(3260, 3272, 0)),
            new TeleportButtonStandard("Desert Bandits", new Position(3176, 2987, 0)),
            new TeleportButtonStandard("Desert Bandits #2", new Position(3490, 3090, 0)),
            new TeleportButtonStandard("Bob's Island", new Position(2524, 4775, 0)),
            new TeleportButtonStandard("Jormungand's Prison", new Position(2471, 10420, 0)),
            new TeleportButtonStandard("Forthos Dungeon", new Position(1800, 9948, 0)),
            new TeleportButtonStandard("Elf Warriors", new Position(2897, 2725, 0)),
            new TeleportButtonStandard("Dagannoths", new Position(2442, 10147, 0)),
            new TeleportButtonStandard("Mithril Dragons", new Position(1740, 5342, 0)),
            new TeleportButtonStandard("Slayer Tower", new Position(3428, 3538, 0)),
            new TeleportButtonStandard("Fremennik Slayer Dungeon", new Position(2807, 10002, 0)),
            new TeleportButtonStandard("Taverly Dungeon", new Position(2883, 9800, 0)),
            new TeleportButtonStandard("Stronghold Cave", new Position(2452, 9820, 0)),
            new TeleportButtonStandard("Mount Karuulm", new Position(1311, 3795, 0)),
            new TeleportButtonStandard("Smoke Devils", new Position(2404, 9415, 0)),
            new TeleportButtonStandard("Asgarnian Ice Dungeon", new Position(3053, 9578, 0)),
            new TeleportButtonStandard("Brimhaven Dungeon", new Position(2709, 9476, 0)),
            new TeleportButtonStandard("Lithkren Vault", new Position(1567, 5074, 0)),
           // new TeleportButtonStandard("Crystal Cavern", new Position(3272, 6052, 0)),
            new TeleportButtonStandard("Death Plateau", new Position(2867, 3594, 0)),
            new TeleportButtonStandard("Brine Rat Cavern", new Position(2722, 10133, 0))
            ));

    private static final TeleportContainer SKILLING = new TeleportContainer(Lists.newArrayList(
            new TeleportButtonStandard("Slayer Masters", new Position(3083, 3499, 0)),
            new TeleportButtonStandard("Mount Karuulm (Slayer)", new Position(1311, 3795, 0)),
            new TeleportButtonStandard("Skilling Island", new Position(1688, 4707, 0)),
            new TeleportButton("Agility", plr -> plr.getDH().sendDialogues(14401, -1)),
            new TeleportButtonStandard("Hunter", new Position(3560, 4010, 0)),
            new TeleportButtonStandard("Essence Mine", new Position(2900, 4820, 0)),
            new TeleportButtonStandard("Farming", new Position(3053, 3301, 0)),
            new TeleportButtonStandard("Puro-Puro", new Position(2594, 4320, 0)),
            new TeleportButtonStandard("ZMI Altar", new Position(2453, 3231, 0))
            ));

    private static final TeleportContainer CITIES = new TeleportContainer(Lists.newArrayList(
            /*new TeleportButton("@cr26@Ironman & Rogue", plr -> {
                if (plr.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN || plr.getMode().getType() == ModeType.ROGUE_IRONMAN || plr.getMode().getType() == ModeType.ROGUE
                || plr.getMode().getType() == ModeType.IRON_MAN || plr.getMode().getType() == ModeType.HC_IRON_MAN || plr.getMode().getType() == ModeType.ULTIMATE_IRON_MAN
                || plr.getMode().getType() == ModeType.GROUP_IRONMAN || plr.getMode().getType() == ModeType.ULTIMATE_IRON_MAN) {
                    plr.getPA().startTeleport(3810, 3297, 0, "modern", false);
                } else {
                    plr.sendMessage("Only ironmen and rogue accounts can use this teleport.");
                }
            }),*/
            new TeleportButtonStandard("Varrock", new Position(3210, 3424, 0)),
            new TeleportButtonStandard("Darkmeyer", new Position(3592, 3359, 0)),
            new TeleportButtonStandard("Yanille", new Position(2606, 3093, 0)),
            new TeleportButtonStandard("Edgeville", new Position(3093, 3493, 0)),
            new TeleportButton("Lumbridge", plr -> {
                plr.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.LUMBRIDGE_TELEPORT);
                plr.getPA().startTeleport(3222, 3218, 0, "modern", false);
            }),
            new TeleportButton("Ardougne", plr -> {
                plr.getPA().startTeleport(2662, 3305, 0, "modern", false);
                plr.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);
            }),

            new TeleportButtonStandard("Neitiznot", new Position(2321, 3804, 0)),
            new TeleportButtonStandard("Karamja", new Position(2948, 3147, 0)),
            new TeleportButton("Falador", plr -> {
                plr.getPA().startTeleport(2964, 3378, 0, "modern", false);
                plr.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_TO_FALADOR);
            }),
            new TeleportButtonStandard("Taverley", new Position(2928, 3451, 0)),
            new TeleportButton("Camelot", plr -> {
                plr.getPA().startTeleport(2757, 3478, 0, "modern", false);
                plr.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
            }),
            new TeleportButtonStandard("Catherby", new Position(2804, 3432, 0)),
            new TeleportButtonStandard("Al Kharid", new Position(3293, 3179, 0)),
            new TeleportButtonStandard("Draynor", new Position(3105, 3249, 0)),
            new TeleportButtonStandard("Kebos Lowlands", new Position(1310, 3618, 0))
    ));

    private static final TeleportContainer WILDERNESS = new TeleportContainer(Lists.newArrayList(

            new TeleportButtonStandard("Mage Bank @yel@(Safe)", new Position(2539, 4716, 0)),
            new TeleportButtonStandard("West Dragons @red@(10)", new Position(2976, 3591, 0), true),
            new TeleportButtonStandard("Dark Castle @red@(15)", new Position(3020, 3632, 0), true),
            new TeleportButtonStandard("Wilderness Slayer Cave @red@(18)", new Position(3384, 10055, 0), true),
            new TeleportButtonStandard("Elder Chaos Druids @red@(16)", new Position(3232, 3642, 0), true),
            new TeleportButtonStandard("Hill Giants (Multi) @red@(18)", new Position(3304, 3657, 0), true),
            new TeleportButtonStandard("Black Chinchompa @red@(32)", new Position(3137, 3767, 0), true),
            new TeleportButtonStandard("Revenant Cave @red@(40)", new Position(3127, 3835, 0), true),
            new TeleportButtonStandard("Lava Maze @red@(43)", new Position(3025, 3836, 0), true),
            new TeleportButtonStandard("Wildy God Wars Dungeon @red@(28)", new Position(3021, 3738, 0), true),
            new TeleportButtonStandard("Wilderness Agility Course @red@(52)", new Position(3003, 3934, 0), true),
            new TeleportButtonStandard("Resource Area @red@(54)", new Position(3184, 3947, 0), true)

    ));

    private static final TeleportContainer BOSSES = new TeleportContainer(Lists.newArrayList(
           // new TeleportButtonStandard("Nex", new Position(2904, 5203, 0)),
            new TeleportButton("Nex", plr -> {
                if (Configuration.nexEnabled) {
                    plr.getPA().startTeleport(2904, 5203, 0, "modern", false);
                } else {
                    plr.sendMessage("Nex is currently disabled.");
                }
            }),
            new TeleportButtonStandard("Seren", new Position(3167, 5357, 0)),
            new TeleportButtonStandard("Avatar of Destruction", new Position(2971, 4574, 0)),
            new TeleportButtonStandard("Barrelchest", new Position(2903, 3612, 0)),
            new TeleportButtonStandard("The Inadequacy", new Position(1824, 5144, 2)),
            new TeleportButton("Mutant Tarn", plr -> {
                    plr.getPA().closeAllWindows();
                    plr.sendMessage("Preparing your mutant tarn instance...");
                    new TarnInstance().begin(plr);
            }),
            new TeleportButton("Maledictus", plr -> {
                plr.getPA().closeAllWindows();
                plr.sendMessage("Preparing your maledictus instance...");
                new MaledictusInstance().begin(plr);
            }),
            /*new TeleportButton("Ulfric", plr -> {
                    plr.getPA().closeAllWindows();
                    plr.sendMessage("Preparing your ulfric instance...");
                    new ulfricInstance().begin(plr);
            }),*/
            new TeleportButtonStandard("Bryophyta", new Position(3174, 9898, 0)),
            new TeleportButtonStandard("Hunllef", new Position(3030, 6121, 1)),
            new TeleportButtonStandard("Obor", new Position(3097, 9833, 0)),
            new TeleportButtonStandard("Dagannoth Kings", new Position(1913, 4367, 0)),
            new TeleportButtonStandard("Giant Mole", new Position(2993, 3376, 0)),
            new TeleportButtonStandard("Kalphite Queen", new Position(3510, 9496, 2)),
            new TeleportButtonStandard("Lizardman Shaman", new Position(1558, 3696, 0)),
            new TeleportButtonStandard("Sarachnis", new Position(1842, 9926, 0)),
            new TeleportButtonStandard("Grotesque Guardians", new Position(3428, 3541, 2)),
            new TeleportButtonStandard("Thermonuclear Smoke Devil", new Position(2404, 9415, 0)),
            new TeleportButtonStandard("Kraken", new Position(2280, 10016, 0)),
            new TeleportButtonStandard("Demonic Gorillas", new Position(2124, 5660, 0)),
            new TeleportButton("Godwars", plr -> plr.getDH().sendDialogues(4487, -1)),
            new TeleportButtonStandard("Corporeal Beast", new Position(2964, 4382, 2)),
            new TeleportButtonStandard("Zulrah", new Position(2203, 3056, 0)),
            new TeleportButtonStandard("Cerberus", new Position(1310, 1248, 0)),
            new TeleportButtonStandard("Abyssal Sire", new Position(3038, 4767, 0)),
            new TeleportButtonStandard("Vorkath", new Position(2272, 4050, 0)),
            new TeleportButtonStandard("Alchemical hydra", new Position(1354, 10259, 0)),
            new TeleportButtonStandard("The Nightmare", new Position(3808, 9755, 1)),
            new TeleportButtonStandard("Brassicans Cave @red@(42 Wild)", new Position(3113, 10251, 0), true),
            new TeleportButtonStandard("King Black Dragon @red@(42 Wild)", new Position(3005, 3849, 0), true),
            new TeleportButtonStandard("Vet'ion @red@(40 Wild)", new Position(3200, 3794, 0), true),
            new TeleportButtonStandard("Callisto @red@(43 Wild)", new Position(3325, 3845, 0), true),
            new TeleportButtonStandard("Scorpia @red@(54 Wild)", new Position(3233, 3945, 0), true),
            new TeleportButtonStandard("Venenatis @red@(28 Wild)", new Position(3345, 3754, 0), true),
            new TeleportButtonStandard("Chaos Elemental @red@(50 Wild)", new Position(3285, 3925, 0), true),
            new TeleportButtonStandard("Chaos Fanatic @red@(41 Wild)", new Position(2978, 3833, 0), true),
            new TeleportButtonStandard("Crazy Archaeologist @red@(23 Wild)", new Position(2984, 3713, 0), true)
    ));

    private static final TeleportContainer MINIGAMES = new TeleportContainer(Lists.newArrayList(
            new TeleportButtonStandard("Theatre of Blood", new Position(3671, 3219, 0)),
            new TeleportButtonStandard("Chambers of Xeric", new Position(3033, 6067, 0)),
            new TeleportButtonStandard("Fight Caves", new Position(2444, 5179, 0)),
            new TeleportButtonStandard("The Inferno", new Position(2437, 5126, 0)),
            new TeleportButtonStandard("Barrows", new Position(3565, 3316, 0)),
            new TeleportButtonStandard("Duel Arena", new Position(3366, 3266, 0)),
            new TeleportButtonStandard("Warriors Guild", new Position(2874, 3546, 0)),
            new TeleportButton("Pest Control", plr -> {
                plr.getPA().startTeleport(2660, 2648, 0, "modern", false);
                plr.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PEST_CONTROL_TELEPORT);
            }),
           // new TeleportButtonStandard("Clan Wars", new Position(3387, 3158, 0)),
            new TeleportButtonStandard("Outlast", new Position(3112, 3508, 0)),
            new TeleportButtonStandard("Mage Arena", new Position(2541, 4716, 0)),
            new TeleportButtonStandard("Learning The Ropes [Quest]", new Position(3806, 3543, 0)),
            new TeleportButtonStandard("Horror From The Deep [Quest]", new Position(2508, 3641, 0)),
            new TeleportButtonStandard("Monkey Madness [Quest]", new Position(2466, 3490, 0))
    ));
    private static final TeleportContainer DONATOR = new TeleportContainer(Lists.newArrayList(

            new TeleportButton("@cr4@Donator Zone", plr -> {
                if (plr.tokensSpent >= 50 || plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(3809, 2844, 0, "modern", false);
                } else {
                    plr.sendMessage("You need to be a sapphire donator to teleport here.");
                }
            }),
            new TeleportButton("@cr6@Emerald Zone", plr -> {
                if (plr.tokensSpent >= 100 || plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(2846, 5089, 0, "modern", false);
                } else {
                    plr.sendMessage("You need a donator status of emerald to tele here.");
                }
            }),
            new TeleportButton("@cr8@Ruby Zone", plr -> {
                if (plr.tokensSpent >= 250 || plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(1247, 2707, 0, "modern", false);
                } else {
                    plr.sendMessage("You need a donator status of ruby to tele here.");
                }
            }),
            new TeleportButton("@cr16@Diamond Zone", plr -> {
                if (plr.tokensSpent >= 500|| plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(3810, 3297, 0, "modern", false);
                } else {
                    plr.sendMessage("You need a donator status of diamond to tele here.");
                }
            }),
            new TeleportButton("@cr17@Onyx Zone", plr -> {
                if (plr.tokensSpent >= 1000 || plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(2337, 9800, 0, "modern", false);
                } else {
                    plr.sendMessage("You need a donator status of onyx to tele here.");
                }
            }),
            new TeleportButton("@cr18@Zenyte Zone", plr -> {
                if (plr.tokensSpent >= 1500 || plr.getDisplayName().equalsIgnoreCase("haku")) {
                    plr.getPA().startTeleport(1692, 4258, 0, "modern", false);
                } else {
                    plr.sendMessage("You need a donator status of zenyte to tele here.");
                }
            })
            /*new TeleportButton("@cr26@Ironman & Rogue", plr -> {
                if (plr.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN || plr.getMode().getType() == ModeType.ROGUE_IRONMAN || plr.getMode().getType() == ModeType.ROGUE
                || plr.getMode().getType() == ModeType.IRON_MAN || plr.getMode().getType() == ModeType.HC_IRON_MAN || plr.getMode().getType() == ModeType.ULTIMATE_IRON_MAN
                || plr.getMode().getType() == ModeType.GROUP_IRONMAN || plr.getMode().getType() == ModeType.ULTIMATE_IRON_MAN) {
                    plr.getPA().startTeleport(3810, 3297, 0, "modern", false);
                } else {
                    plr.sendMessage("Only ironmen and rogue accounts can use this teleport.");
                }
            }),*/
    ));

    public static final List<TeleportContainer> TELEPORT_CONTAINER_LIST = Collections.unmodifiableList(Lists.newArrayList(
        MONSTERS, BOSSES, WILDERNESS, SKILLING, MINIGAMES, CITIES, DONATOR
    ));

}