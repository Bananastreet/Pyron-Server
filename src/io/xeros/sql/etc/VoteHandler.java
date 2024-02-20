package io.xeros.sql.etc;

import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.bosses.VoteBoss;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class VoteHandler implements Runnable {

    public static final String HOST = "72.167.205.111";
    public static final String USER = "pitboss121";
    public static final String PASS = "1fhzno6WvTLAsk55";
    public static final String DATABASE = "vanguardrsps_serverlogs";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public VoteHandler(Player player) {
        this.player = player;
    }

    public static int totalVotes;

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getDisplayName().replace(" ", "_");
            ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='" + name + "' AND claimed=0 AND callback_date IS NOT NULL");

            boolean claimFound = false; // Flag to track if at least one claim is found

            while (rs.next()) {
                claimFound = true; // Set the flag to true if a claim is found

                String timestamp = rs.getTimestamp("callback_date").toString();
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");
                if (player.getPermAttributes().getOrDefault(Player.BATTLEPASS_WINTER_2023)) {
                    player.getItems().addItemUnderAnyCircumstance(23933, 1);
                }
                player.getItems().addItemUnderAnyCircumstance(23933, 1);
                if (Misc.hasOneOutOf(10)) {
                    player.getItems().addItemUnderAnyCircumstance(22093, 1);
                    player.sendMessage("You've received a vote chest key.");
                }
                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
                totalVotes++;
                if (totalVotes >= VoteBoss.requiredVotes) {
                    VoteBoss.spawnVoteBoss();
                    totalVotes = 0;
                }
            }

            if (claimFound) {
                player.sendMessage("Thank you for claiming your votes.");
                Achievements.increase(player, AchievementType.VOTER, 1);
                PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has claimed their votes ::claimvote");
                if (Misc.hasOneOutOf(10)) {
                    player.getItems().addItemUnderAnyCircumstance(21046, 1);
                    PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has received a chest rate bonus from voting");
                }
               /* if (Misc.hasOneOutOf(40)) {
                    player.getItems().addItemUnderAnyCircumstance(5020, 1);
                    PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has received a perk ticket from voting");
                } */
                if (Misc.hasOneOutOf(35)) {
                    player.getItems().addItemUnderAnyCircumstance(23497, 1);
                    PlayerHandler.executeGlobalMessage("@cr36@@red@[VOTE] @blu@" + player.getDisplayName() + " has received an upgrade token from voting");
                }
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
