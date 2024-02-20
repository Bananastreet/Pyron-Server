package io.xeros.content.commands.owner;

import io.xeros.content.QuestTab;
import io.xeros.content.bosses.hespori.Hespori;
import io.xeros.content.commands.Command;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class doublelarrans extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        Hespori.activeKeldaSeed = true;
        Hespori.KELDA_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.newsMessage("1 hour of double larrans keys is now active.");
        QuestTab.updateAllQuestTabs();
    }
}
