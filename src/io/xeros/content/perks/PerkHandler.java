package io.xeros.content.perks;

import io.xeros.Configuration;
import io.xeros.content.dialogue.DialogueBuilder;
import io.xeros.content.dialogue.DialogueOption;
import io.xeros.model.Items;
import io.xeros.model.PermanentAttributes.PermanentAttributeKey;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.sql.DiscordWebhook;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.IOException;

/**
 * @author Jack, created on 05/10/2023
 * @see <a href="https://www.rune-server.ee/members/jack/">Rune-Server Profile</a>
 */
public class PerkHandler {

    public static int getRefundAmount(int cost) {
        return (int) Math.round(cost * .7);
    }

    private final Player player;

    public PerkHandler(Player player) {
        this.player = player;
    }

    public void displayInterface() {
        handleDrawingComponents();
        player.perk = "";
        player.getPA().sendFrame126("Select a Perk", 38021);
        player.getPA().sendFrame126("Select a perk from the left for more\\ninformation!", 38031);
        player.getPA().sendFrame126("", 38024); //price
        var tokensInInventory = player.getItems().getItemAmount(Items.DONATOR_TOKEN);
        player.getPA().sendFrame126("" + tokensInInventory, 38019); //tokens in inventory
        player.getPA().sendFrame126("Purchase", 42529); //purchase button
        player.getPA().showInterface(38000);
    }

    private void handleDrawingComponents() {
        //shows all background components and wipes all names for perks
        for (int i = 0; i < 30; i++) {
            player.getPA().sendInterfaceHidden(38033 + (i * 4), false);
            player.getPA().sendFrame126("", 38036 + (i * 4));
        }

        //displays the perk names on interface
        var position = 0;
        for (int i = 0; i < Perks.values().length; i++) {
            if (Perks.values()[i].getCategory() == player.getPerkCategory()) {
                var hasPerk = player.hasPerk(Perks.values()[i].getKey());
                var perkName = hasPerk ? "@gre@" + Perks.values()[i].getPerkName() : Perks.values()[i].getPerkName();
                position++;
                player.getPA().sendFrame126(perkName, 38036 + ((position - 1) * 4));
            }
        }

        //hides all components that are not being used
        for (int i = 30; i > position - 1; i--) {
            player.getPA().sendInterfaceHidden(38033 + (i * 4), true);
        }
    }

    private void getInfo(PermanentAttributeKey<?> perkKey) {
        for (final Perks perks : Perks.values()) {
            if (perks.getKey() == perkKey && perks.getCategory() == player.getPerkCategory()) {
                player.perk = perks.getPerkName();
                player.getPA().sendFrame126("" + perks.getPerkName(), 38021);
                player.getPA().sendFrame126("" + perks.getDescription(), 38031);
                player.getPA().sendFrame126("" + perks.getPrice(), 38024);

                if ((boolean) player.getPermAttributes().getOrDefault(perks.getKey())) {
                    player.getPA().sendFrame126("Refund (" + getRefundAmount((perks.getPrice())) + ")", 38029);
                } else {
                    player.getPA().sendFrame126("Purchase", 38029);
                }
            }
        }
    }

    private void handleRefund(Player player, Perks perks) {
        player.sendMessage(+getRefundAmount(perks.getPrice()) + "x donator tokens refunded for the " + perks.getPerkName() + " perk.");
        player.getItems().addItemUnderAnyCircumstance(Items.DONATOR_TOKEN, getRefundAmount(perks.getPrice()));
        player.getPermAttributes().put(perks.getKey(), false);
        player.getPerks().displayInterface();
    }

    private void handlePurchase() {
        for (final Perks perks : Perks.values()) {
            if (perks.getPerkName().equalsIgnoreCase(player.perk)) {
                if (player.hasPerk(perks.getKey())) {
                    player.start(new DialogueBuilder(player)
                            .statement("Refunds only give 70%, are you sure?")
                            .option(
                                    new DialogueOption("Refund perk", plr -> handleRefund(player, perks)),
                                    new DialogueOption("Don't refund perk", plr -> player.getPerks().displayInterface())
                            )
                    );
                    return;
                }
                if (!player.getItems().playerHasItem(Items.DONATOR_TOKEN, perks.getPrice())) {
                    player.sendMessage("You do not have enough donator tokens to purchase the " + perks.getPerkName() + " perk.");
                    return;
                }

                player.getItems().deleteItem(Items.DONATOR_TOKEN, perks.getPrice());
                player.getPermAttributes().put(perks.getKey(), true);
                player.sendMessage("You have purchased the " + perks.getPerkName() + " perk.");
                PlayerHandler.executeGlobalMessage("@cr35@ @blu@[PERKS] @red@" + player.getDisplayName() + " has purchased the perk: " + perks.getPerkName() + "!");
                player.getPerks().displayInterface();
                DiscordWebhook webhook = new DiscordWebhook(Configuration.AchievementsWebhook);
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setTitle("Perk Purchase")
                        .setDescription(player.getDisplayName() + " has purchased the " + perks.getPerkName() + " perk.")
                        .setColor(Color.RED));
                try {
                    if (Configuration.DiscordEnabled)
                        webhook.execute(); //Handle exception
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public static boolean handleButtons(Player player, int buttonId) {
        for (int i = 0; i < Perks.values().length; i++) {
            if (buttonId == Perks.values()[i].getButtonId() && Perks.values()[i].getCategory() == player.getPerkCategory()) {
                player.getPerks().getInfo(Perks.values()[i].getKey());
                return true;
            }
        }
        if (buttonId == 148118) { //combat category
            player.setPerkCategory(PerkCategories.COMBAT);
            player.getPerks().displayInterface();
            return true;
        }
        if (buttonId == 148122) { //boost category
            player.setPerkCategory(PerkCategories.BOOST);
            player.getPerks().displayInterface();
            return true;
        }
        if (buttonId == 148126) { //other category
            player.setPerkCategory(PerkCategories.OTHER);
            player.getPerks().displayInterface();
            return true;
        }
        if (buttonId == 148114) { //close button
            player.getPA().closeAllWindows(true);
            return true;
        }
        if (buttonId == 148138) { //purchase button
            player.getPerks().handlePurchase();
            return true;
        }
        return false;
    }

}
