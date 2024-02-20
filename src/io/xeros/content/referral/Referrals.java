package io.xeros.content.referral;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.xeros.Configuration;
import io.xeros.Server;
import io.xeros.model.Items;
import io.xeros.model.entity.player.Player;
import io.xeros.model.items.GameItem;
import io.xeros.sql.DiscordWebhook;
import io.xeros.util.Misc;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Referrals {

    private static final List<GameItem> rewards = List.of(new GameItem(Items.COINS, 1500000),
            new GameItem(6199, 1), new GameItem(2528, 1), new GameItem(11937, 100));

    private static final String REFERRAL_ATTRIBUTE_KEY = "referalls";
    private static final String REFERRAL_MAP_ATTRIBUTE_KEY = "referalls_map";
    public static Map<String, Integer> referrals = new ConcurrentHashMap<>();
    private static final Path saveFile = Paths.get(Server.getSaveDirectory() + "referrals.json");

    public static void start(Player player) {
        var totalLevelRequired = player.getMode().is5x() ? 100 : 200;
        if (player.totalLevel < totalLevelRequired && !player.getDisplayName().equalsIgnoreCase("bubly")) {
            player.getDH().sendStatement("You need a total level of " + totalLevelRequired + " to claim a referral.");
            return;
        }
        if (!canGetReward(player) && !player.getDisplayName().equalsIgnoreCase("bubly") || player.usedReferral && !player.getDisplayName().equalsIgnoreCase("bubly")) {
            player.getDH().sendStatement("You have already claimed a referral.");
            return;
        }

        player.getPA().sendEnterString("Where did you find us?", (Referrals::claimReferral));
    }

    private static void claimReferral(Player player, String source) {
        setUsedReferral(player, source);
        player.getDH().sendItemStatement("Thanks for trying out Pyron! Enjoy your stay!", Items.YOUTUBE_MYSTERY_BOX);
        rewards.forEach(item -> player.getItems().addItemUnderAnyCircumstance(item.getId(), item.getAmount()));
        DiscordWebhook webhook = new DiscordWebhook(Configuration.refWebhook);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Referral Code - "+ source)
                .setDescription(player.getDisplayName() + " has used the referral code "+ source)
                .setColor(Color.RED));
        try {
            webhook.execute(); //Handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean canGetReward(Player player) {
        if (getUsedReferrals().stream().anyMatch(data -> data.equals(player.getMacAddress()))) {
            return false;
        }
        HashSet<String> used = getUsedMapReferrals();
        return !used.contains(player.getIpAddress()) && !used.contains(player.getMacAddress());
    }

    @SuppressWarnings("unchecked")
    private static List<String> getUsedReferrals() {
        if (Server.getServerAttributes().getList(REFERRAL_ATTRIBUTE_KEY) == null) {
            Server.getServerAttributes().setList(REFERRAL_ATTRIBUTE_KEY, new ArrayList<>());
        }
        return (List<String>) Server.getServerAttributes().getList(REFERRAL_ATTRIBUTE_KEY);
    }

    @SuppressWarnings("unchecked")
    private static HashSet<String> getUsedMapReferrals() {
        if (Server.getServerAttributes().getHashSet(REFERRAL_MAP_ATTRIBUTE_KEY) == null) {
            Server.getServerAttributes().setHashSet(REFERRAL_MAP_ATTRIBUTE_KEY, new HashSet<String>());
        }
        return (HashSet<String>) Server.getServerAttributes().getHashSet(REFERRAL_MAP_ATTRIBUTE_KEY);
    }

    private static void setUsedReferral(Player c, String source) {
        c.usedReferral = true;

        //Writes to HashMap, so we can track how many referrals each source has
        if (referrals.containsKey(source)) {
            referrals.replace(source, referrals.get(source) + 1);
        } else {
            referrals.put(source, 1);
        }

        //Writes to server attributes so they cannot claim again
        getUsedMapReferrals().add(c.getIpAddress());
        getUsedMapReferrals().add(c.getMacAddress());
        Server.getServerAttributes().write();
    }

    public static String getTopReferral() {
        String top = "";
        int topAmount = 0;
        for (Map.Entry<String, Integer> entry : referrals.entrySet()) {
            if (entry.getValue() > topAmount) {
                top = entry.getKey();
                topAmount = entry.getValue();
            }
        }
        return "The top referral is @blu@" + top + "@bla@ with @blu@" + topAmount + "@bla@ referrals.";
    }

    public static String checkReferral(String source) {
        if (referrals.containsKey(source)) {
            return "@blu@" + source + "@bla@ has @blu@" + referrals.get(source) + " @bla@referrals.";
        }
        return "No referrals found.";
    }

    public static void topRefs(Player player, int amount) {
        var sortedReferrals = referrals.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(amount).map(Map.Entry::getKey).collect(Collectors.toList());
        player.sendMessage("Top @blu@" + amount + "@bla@ referrals:");
        for (int i = 0; i < sortedReferrals.size(); i++) {
            player.sendMessage((i + 1) + ": @blu@" + sortedReferrals.get(i) + "@bla@ with @blu@" + referrals.get(sortedReferrals.get(i)) + "@bla@ referrals.");
        }
    }

    public static void saveReferrals() {
        File file = saveFile.toFile();
        file.getParentFile().setWritable(true);
        Misc.createFileIfNotExists(file);

        try (FileWriter writer = new FileWriter(file)) {
            JsonObject object = new JsonObject();
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            object.add("all-referrals",  builder.toJsonTree(referrals));
            writer.write(builder.toJson(object));
        } catch (Exception e) {
            System.out.println("ERROR SAVING REFERRALS");
            e.printStackTrace();
        }
    }

    public static void loadReferrals() {
        File file = saveFile.toFile();
        if(!file.exists()) {
            return;
        }

        file.getParentFile().setWritable(true);

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);

            referrals = builder.fromJson(reader.get("all-referrals"),
                    new TypeToken<HashMap<String, Integer>>() {
                    }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
