package io.xeros.content.bosses.hespori;

import io.xeros.content.QuestTab;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class BuchuBonus implements HesporiBonus {
    @Override
    public void activate(Player player) {
        Hespori.activeBuchuSeed = true;
        Hespori.BUCHU_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@BOOST@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@activated 1 hour of 2x boss points.");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        Hespori.activeBuchuSeed = false;
        Hespori.BUCHU_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public HesporiBonusPlant getPlant() {
        return HesporiBonusPlant.BUCHU;
    }
}
