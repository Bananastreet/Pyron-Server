package io.xeros.content.battlepass;

import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Right;
import io.xeros.util.Misc;

public class BattlepassLevels {

    /*

    USAGE:  BattlepassLevels.addBattlepassExperience(c, XP_AMOUNT_HERE);
            You can randomise experience amounts between values by using Misc.random(VALUE1,VALUE2);
            Eg: BattlepassLevels.addBattlepassExperience(c, Misc.Random(69, 420);
            Everything else is handled already.
     */
    public static int calculateExperienceForLevel(int level) {
        return level * 1000;
    }

    public static void checkForLevelUp(Player player) {
        while (player.battlepassExperience >= calculateExperienceForLevel(player.battlepassLevel + 1)) {
            player.battlepassLevel += 1;
            player.sendMessage("Congratulations, you've just advanced a level on the battlepass!");
            BattlepassHandler.addToUnclaimed(player, player.battlepassLevel);
            BattlepassHandler.refreshInterface(player);
        }
    }

    public static void addBattlepassExperience(Player player, int amount) {
        boolean isDonator = player.getRights().isOrInherits(Right.SAPPHIRE_DONATOR);
        int chance = isDonator ? 2 : 1;
        if (Misc.random(1, 10) <= chance) {  //1 in 10 chance to apply experience now, 2 in 10 if donator
            player.battlepassExperience = player.battlepassExperience + amount;
            checkForLevelUp(player);
        }
    }
}
