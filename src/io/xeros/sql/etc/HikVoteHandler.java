package io.xeros.sql.etc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.bosses.VoteBoss;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Misc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class HikVoteHandler implements Runnable {

    public static final String HOST = "72.167.205.111";
    public static final String USER = "pitboss121";
    public static final String PASS = "1fhzno6WvTLAsk55";
    public static final String DATABASE = "vangaurdrsps_serverlogs";

    public static int totalVotes;

    private Player player;

    private HikariDataSource dataSource;

    public HikVoteHandler(Player player) {
        this.player = player;

        // HikariCP setup
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + HOST + ":3306/" + DATABASE);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setMaximumPoolSize(10); // Adjust this value as per your needs
        dataSource = new HikariDataSource(config);
    }

    @Override
    public void run() {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            String name = player.getDisplayName().replace(" ", "_");
            ResultSet rs = stmt.executeQuery("SELECT * FROM fx_votes WHERE username='" + name + "' AND claimed=0 AND callback_date IS NOT NULL");

            boolean claimFound = false; // Flag to track if at least one claim is found

            while (rs.next()) {
                claimFound = true; // Set the flag to true if a claim is found

                String timestamp = rs.getTimestamp("callback_date").toString();
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");
                if (player.getPermAttributes().getOrDefault(Player.BATTLEPASS_FALL_2023)) {
                    player.getItems().addItemUnderAnyCircumstance(23933, 1);
                }
                player.getItems().addItemUnderAnyCircumstance(23933, 1);
                if (Misc.hasOneOutOf(10)) {
                    player.getItems().addItemUnderAnyCircumstance(22093, 1);
                    player.sendMessage("You've received a vote chest key.");
                }
                player.sendMessage("Thank you for claiming a vote.");
                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
                totalVotes++;
                if (totalVotes >= VoteBoss.requiredVotes) {
                    VoteBoss.spawnVoteBoss();
                    totalVotes = 0;
                }
            }

            if (claimFound) {
                Achievements.increase(player, AchievementType.VOTER, 1);
                PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has claimed their votes ::claimvote");
               /* if (Misc.hasOneOutOf(40)) {
                    player.getItems().addItemUnderAnyCircumstance(5020, 1);
                    PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has received a perk ticket from voting");
                } */
                if (Misc.hasOneOutOf(35)) {
                    player.getItems().addItemUnderAnyCircumstance(23497, 1);
                    PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has received an upgrade token from voting");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Your other methods can remain unchanged
    // ...

    // Don't forget to add a method to clean up HikariCP resources when they are no longer needed
    public void cleanup() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
