package io.xeros.content.bosses.hespori;

import java.util.concurrent.TimeUnit;

import io.xeros.content.QuestTab;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

public class KronosBonus implements HesporiBonus {
    @Override
    public void activate(Player player) {
        Hespori.activeKronosSeed = true;
        Hespori.KRONOS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@BOOST@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@activated 1 hour of double CoX keys + TOB rares.");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        Hespori.activeKronosSeed = false;
        Hespori.KRONOS_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public HesporiBonusPlant getPlant() {
        return HesporiBonusPlant.KRONOS;
    }
}
