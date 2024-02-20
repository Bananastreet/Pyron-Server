package io.xeros.content.commands.all;

import io.xeros.Configuration;
import io.xeros.model.entity.player.Player;
import io.xeros.sql.etc.DonationHandler;
import io.xeros.sql.etc.HiscoresUpdateQuery;

import java.util.Optional;

public class claimdonation extends Commands {
    @Override
    public void execute(Player c, String commandName, String input) {
        try {
            // DonationHandler updateQuery = new DonationHandler(c);
            // updateQuery.run();
            new java.lang.Thread() {
                public void run() {
                    try {
                        com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("YglTT3Ab0TvHkc7ifW0bDemvAYouBquLpmUiLIhFs0ycB388WNrjlLcZhZvj72nq6V87Fehs",
                                c.getDisplayName());
                        if (donations.length == 0) {
                            c.sendMessage("You currently don't have any items waiting. You must donate first!");
                            return;
                        }
                        if (donations[0].message != null) {
                            c.sendMessage(donations[0].message);
                            return;
                        }
                        for (com.everythingrs.donate.Donation donate: donations) {
                            c.getItems().addItem(donate.product_id, donate.product_amount);
                        }
                        c.sendMessage("Thank you for donating!");
                    } catch (Exception e) {
                        c.sendMessage("Api Services are currently offline. Please check back shortly");
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("claim a donation you made");
    }
}
