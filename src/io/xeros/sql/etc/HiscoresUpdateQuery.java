package io.xeros.sql.etc;

import io.xeros.content.skills.Skill;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.mode.ModeType;

import java.sql.*;

public class HiscoresUpdateQuery implements Runnable {

    public static final String HOST = "72.167.205.111"; // website ip address
    public static final String USER = "pitboss121";
    public static final String PASS = "1fhzno6WvTLAsk55";
    public static final String DATABASE = "vanguardrsps_hs_users";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public static String gameMode(Player player) {
        if (player.getMode().getType() == ModeType.HC_IRON_MAN) {
            return "hardcore-ironman";
        } else if (player.getMode().getType() == ModeType.ULTIMATE_IRON_MAN) {
            return "ultimate-ironman";
        } else if (player.getMode().getType() == ModeType.IRON_MAN) {
            return "ironman";
        } else if (player.getMode().isGroupIronman()) {
            return "group-ironman";
        } else if (player.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN) {
            return "rogue-hardcore-ironman";
        } else if (player.getMode().getType() == ModeType.ROGUE) {
            return "rogue";
        }
        return "standard";
    }

    public static int xpRate(Player player) {
        if (player.getMode().getType() == ModeType.ROGUE_HARDCORE_IRONMAN || player.getMode().getType() == ModeType.ROGUE) {
            return 5;
        }
        return 250;
    }

    public HiscoresUpdateQuery(Player player) {
        this.player = player;
    }

    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void run() {
        try {
            String name = player.getDisplayName();
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            if (name.equalsIgnoreCase("grim") || name.equalsIgnoreCase("haku")) return;

            PreparedStatement stmt1 = prepare("DELETE FROM hs_users WHERE username=?");
            stmt1.setString(1, name);
            stmt1.execute();

            PreparedStatement stmt2 = prepare(generateQuery());

            stmt2.setString(1, name);
            stmt2.setInt(2, 35);
            stmt2.setString(3, gameMode(player));
            stmt2.setInt(4, xpRate(player)); // xprate
            stmt2.setInt(5, player.totalLevel); // total level
            stmt2.setInt(6, player.getExperience(Skill.forId(0)));  // attack_xp
            stmt2.setInt(7, player.getExperience(Skill.forId(1)));  // defence_xp
            stmt2.setInt(8, player.getExperience(Skill.forId(2)));  // strength_xp
            stmt2.setInt(9, player.getExperience(Skill.forId(3)));  // constitution_xp
            stmt2.setInt(10, player.getExperience(Skill.forId(4)));  // ranged_xp
            stmt2.setInt(11, player.getExperience(Skill.forId(5)));  // prayer_xp
            stmt2.setInt(12, player.getExperience(Skill.forId(6))); // magic_xp
            stmt2.setInt(13, player.getExperience(Skill.forId(7))); // cooking_xp
            stmt2.setInt(14, player.getExperience(Skill.forId(8))); // woodcutting_xp
            stmt2.setInt(15, player.getExperience(Skill.forId(9))); // fletching_xp
            stmt2.setInt(16, player.getExperience(Skill.forId(10))); // fishing_xp
            stmt2.setInt(17, player.getExperience(Skill.forId(11))); // firemaking_xp
            stmt2.setInt(18, player.getExperience(Skill.forId(12))); // crafting_xp
            stmt2.setInt(19, player.getExperience(Skill.forId(13))); // smithing_xp
            stmt2.setInt(20, player.getExperience(Skill.forId(14))); // mining_xp
            stmt2.setInt(21, player.getExperience(Skill.forId(15))); // herblore_xp
            stmt2.setInt(22, player.getExperience(Skill.forId(16))); // agility_xp
            stmt2.setInt(23, player.getExperience(Skill.forId(17))); // thieving_xp
            stmt2.setInt(24, player.getExperience(Skill.forId(18))); // slayer_xp
            stmt2.setInt(25, player.getExperience(Skill.forId(19))); // farming_xp
            stmt2.setInt(26, player.getExperience(Skill.forId(20))); // runecrafting_xp
            stmt2.setInt(27, player.getExperience(Skill.forId(21))); // hunter_xp
            stmt2.setInt(28, player.tokensSpent); // tokens spent
            stmt2.setTimestamp(29, new Timestamp(System.currentTimeMillis())); // last_updated


            stmt2.execute();

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepare(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public void destroy() {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO hs_users (");
        sb.append("username, ");
        sb.append("rights, ");
        sb.append("gamemode, ");
        sb.append("xprate, ");
        sb.append("total_level, ");
        sb.append("attack_xp, ");
        sb.append("defence_xp, ");
        sb.append("strength_xp, ");
        sb.append("constitution_xp, ");
        sb.append("ranged_xp, ");
        sb.append("prayer_xp, ");
        sb.append("magic_xp, ");
        sb.append("cooking_xp, ");
        sb.append("woodcutting_xp, ");
        sb.append("fletching_xp, ");
        sb.append("fishing_xp, ");
        sb.append("firemaking_xp, ");
        sb.append("crafting_xp, ");
        sb.append("smithing_xp, ");
        sb.append("mining_xp, ");
        sb.append("herblore_xp, ");
        sb.append("agility_xp, ");
        sb.append("thieving_xp, ");
        sb.append("slayer_xp, ");
        sb.append("farming_xp, ");
        sb.append("runecrafting_xp, ");
        sb.append("hunter_xp, ");
        sb.append("tokens_xp, ");
        sb.append("last_updated) ");
        sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return sb.toString();
    }

}
