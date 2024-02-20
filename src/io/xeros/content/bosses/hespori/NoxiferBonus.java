package io.xeros.content.bosses.hespori;

import io.xeros.content.QuestTab;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class NoxiferBonus implements HesporiBonus {
    @Override
    public void activate(Player player) {
        Hespori.activeNoxiferSeed = true;
        Hespori.NOXIFER_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@BOOST@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@activated 1 hour of 2x slayer points.");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        Hespori.activeNoxiferSeed = false;
        Hespori.NOXIFER_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public HesporiBonusPlant getPlant() {
        return HesporiBonusPlant.NOXIFER;
    }
}
