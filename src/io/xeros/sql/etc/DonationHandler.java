package io.xeros.sql.etc;

import io.xeros.content.bosses.Abomination;
import io.xeros.content.bosses.VoteBoss;
import io.xeros.content.skills.skillrewards.SeasonalTickets;
import io.xeros.model.entity.player.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class DonationHandler implements Runnable {

    public static final String HOST = "72.167.205.111"; // website ip address
    public static final String USER = "storeuser";
    public static final String PASS = "Pyron132";
    public static final String DATABASE = "store";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public static int totalCents;

    /**
     * The constructor
     * @param player
     */
    public DonationHandler(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getDisplayName().replace("_", " ");
            ResultSet rs = executeQuery("SELECT * FROM purchases WHERE username='"+name+"' AND claimed=0");

            while (rs.next()) {
                int tokenAmount = rs.getInt("amount");
                int cents = rs.getInt("cents_paid");

                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String formattedAmount = decimalFormat.format((double) cents / 100);

                player.getItems().addItem(7478, tokenAmount);
                player.sendMessage("You have claimed "+ tokenAmount + " tokens for your donation of $"+ formattedAmount);

                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
                totalCents += cents;
                player.getItems().addItemUnderAnyCircumstance(SeasonalTickets.ticketID, cents * 4);
                if (totalCents >= Abomination.requiredCents) {
                    Abomination.spawnBoss();
                    totalCents = 0;
                }
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param host the host ip address or url
     * @param database the name of the database
     * @param user the user attached to the database
     * @param pass the users password
     * @return true if connected
     */
    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!" + e);
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and destroy the connection
     * and statement instances
     */
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

    /**
     * Executes an update query on the database
     * @param query
     * @see {@link Statement#executeUpdate}
     */
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

    /**
     * Executres a query on the database
     * @param query
     * @see {@link Statement#executeQuery(String)}
     * @return the results, never null
     */
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
