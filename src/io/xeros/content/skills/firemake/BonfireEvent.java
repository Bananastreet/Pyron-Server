package io.xeros.content.skills.firemake;

import com.google.common.base.Stopwatch;
import io.xeros.Server;
import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.achievement_diary.impl.LumbridgeDraynorDiaryEntry;
import io.xeros.model.Animation;
import io.xeros.model.cycleevent.Event;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.items.ItemAssistant;
import io.xeros.util.Misc;

import java.util.concurrent.TimeUnit;

public class BonfireEvent  {

    Player player;
    public BonfireEvent(Player player) { this.player = player; }

    private static final Animation anim = new Animation(897);
    private final Stopwatch lastAction = Stopwatch.createStarted();

    public void start(LogData log, final int amount, int objectX, int objectY) {
        var logName = ItemAssistant.getItemName(log.getlogId());
        player.getPA().stopSkilling();
        if (player.playerLevel[11] < log.getlevelRequirement()) {
            player.sendMessage("You need a firemaking level of at least " + log.getlevelRequirement() + " to light the "
                    + logName + ".");
            return;
        }
        if (!player.getItems().playerHasItem(log.getlogId())) {
            return;
        }
        if (lastAction.elapsed(TimeUnit.MILLISECONDS) < 1800) {
            return;
        }
        handleAddingLog(log);
        player.startAnimation(anim);
        lastAction.reset();
        lastAction.start();
        Server.getEventHandler().submit(new Event<Player>("skilling", player, 5) {
            int remaining = amount - 1;
            @Override
            public void execute() {
                if (player.isDisconnected() || player.getSession() == null) {
                    super.stop();
                    return;
                }
                if (!player.getItems().playerHasItem(log.getlogId())) {
                    super.stop();
                    player.sendMessage("You have run out of " + logName + ".");
                    return;
                }
                if (remaining <= 0) {
                    super.stop();
                    return;
                }
                if (!Server.getGlobalObjects().exists(5249, objectX, objectY, player.getHeight())) {
                    super.stop();
                    return;
                }
                remaining--;
                player.facePosition(player.objectX, player.objectY);
                handleAddingLog(log);
                player.startAnimation(anim);
                lastAction.reset();
                lastAction.start();
            }
        });

    }

    private int getPyromancerPieces() {
        int pieces = 0;
        for (int i = 0; i < Firemaking.pyromancerOutfit.length; i++) {
            if (player.getItems().isWearingItem(Firemaking.pyromancerOutfit[i])) {
                pieces++;
            }
        }
        return pieces;
    }

    private void handleAddingLog(LogData log) {
        if (log.getlogId() == 1521) {
            player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.BURN_OAK);
        }

        Achievements.increase(player, AchievementType.FIRE, 1);
        player.getPA().sendSound(2596);
        player.getItems().deleteItem2(log.getlogId(), 1);
        player.sendMessage("You add the " + ItemAssistant.getItemName(log.getlogId()).toLowerCase() + " to the fire.");

        double experience = log.getExperience() + log.getExperience() / 10 * player.getBonfire().getPyromancerPieces();
        player.getPA().addSkillXPMultiplied((int) experience, 11, true);

        if (Misc.hasOneOutOf(2000)) {
            if (player.getItems().getItemCount(20693, false) > 0 || player.petSummonId == 20693) {
                return;
            }
            player.getItems().addItemUnderAnyCircumstance(20693, 1);
            PlayerHandler.dropMessage(player.getDisplayNameFormatted() + " received a Phoenix pet!");
            player.getCollectionLog().handleDrop(player, 5, 20693, 1);
        }
    }

}
