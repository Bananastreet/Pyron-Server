package io.xeros.content.skills.skillrewards;

import io.xeros.content.skills.Skill;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;

import java.util.Random;

public class SeasonalTickets {

    public static final int ticketID = 619;
    private static final int CHANCE = 5;
    private static final int[] randomAmounts = {2, 5, 10, 15, 20, 25};

    public static void giveTickets(Skill skill, Player player) {
        if (Boundary.isIn(player, Boundary.HESPORI)) {
            return;
        }
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance > CHANCE) {
            return;
        }
        int amount = randomAmounts[random.nextInt(randomAmounts.length)];
        player.getItems().addItemUnderAnyCircumstance(ticketID, amount);
        player.sendMessage("You've received " + amount + "x seasonal tickets from " + skill.name().toLowerCase() + ".");

    }

}
