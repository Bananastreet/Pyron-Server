package io.xeros.content.wogw;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.xeros.Server;
import io.xeros.content.QuestTab;
import io.xeros.content.achievement.AchievementType;
import io.xeros.content.achievement.Achievements;
import io.xeros.content.event.eventcalendar.EventChallenge;
import io.xeros.model.cycleevent.CycleEvent;
import io.xeros.model.cycleevent.CycleEventContainer;
import io.xeros.model.cycleevent.CycleEventHandler;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Misc;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Wogw {

	public static String getSaveFile() {
		return Server.getSaveDirectory() + "wogw.txt";
	}
	private static final int LEAST_ACCEPTED_AMOUNT = 1_000_000;
	public static final int EXPERIENCE_COINS_REQUIRED = 35_000_000;
	public static final int PC_COINS_REQUIRED = 25_000_000;
	public static final int DROP_RATE_COINS_REQUIRED = 50_000_000;
	public static long EXPERIENCE_TIMER, PC_POINTS_TIMER, _20_PERCENT_DROP_RATE_TIMER;
	public static long MONEY_TOWARDS_EXPERIENCE, MONEY_TOWARDS_PC_POINTS, MONEY_TOWARDS_DROP_RATE_BOOST;
	public static List<Player> recentContributors = new ArrayList<>();
	public static Map<String, Long> allContributions = new ConcurrentHashMap<>();

	public static void init() {
        try {
            File f = new File(getSaveFile());
            if (!f.exists()) {
				Preconditions.checkState(f.createNewFile());
			}
            Scanner sc = new Scanner(f);
            int i = 0;
            while(sc.hasNextLine()){
            	i++;
                String line = sc.nextLine();
                String[] details = line.split("="); //log onto game make sure its ok
                String amount = details[1];
                
                switch (i) {
                case 1:
                	MONEY_TOWARDS_EXPERIENCE = Long.parseLong(amount);
                	break;
                case 2:
                	EXPERIENCE_TIMER = Long.parseLong(amount);
                	break;
                case 3:
                	MONEY_TOWARDS_PC_POINTS = Long.parseLong(amount);
                	break;
                case 4:
                	PC_POINTS_TIMER = Long.parseLong(amount);
                	break;
                case 5:
                	MONEY_TOWARDS_DROP_RATE_BOOST = Long.parseLong(amount);
                	break;
                case 6:
                	_20_PERCENT_DROP_RATE_TIMER = Long.parseLong(amount);
                	break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		checkForFillEvent();
	}
	
	public static void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(getSaveFile()));
			out.write("experience-amount=" + MONEY_TOWARDS_EXPERIENCE);
			out.newLine();
			out.write("experience-timer=" + EXPERIENCE_TIMER);
			out.newLine();
			out.write("pc-amount=" + MONEY_TOWARDS_PC_POINTS);
			out.newLine();
			out.write("pc-timer=" + PC_POINTS_TIMER);
			out.newLine();
			out.write("drops-amount=" + MONEY_TOWARDS_DROP_RATE_BOOST);
			out.newLine();
			out.write("drops-timer=" + _20_PERCENT_DROP_RATE_TIMER);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveContributions() {
		Path path = Paths.get(Server.getSaveDirectory() + "wogwcontributions.json");

		File file = path.toFile();
		file.getParentFile().setWritable(true);
		createFileIfNotExists(file);

		try (FileWriter writer = new FileWriter(file)) {
			JsonObject object = new JsonObject();
			Gson builder = new GsonBuilder()
					.setPrettyPrinting()
					.create();
			object.add("all-contributions",  builder.toJsonTree(allContributions));
			writer.write(builder.toJson(object));
		} catch (Exception e) {
			System.out.println("ERROR SAVING WOGW CONTRIBUTIONS");
			e.printStackTrace();
		}
	}

	public static void loadContributions() {
		Path path = Paths.get(Server.getSaveDirectory() + "wogwcontributions.json");

		File file = path.toFile();
		if(!file.exists()) {
			return;
		}

		file.getParentFile().setWritable(true);

		try (FileReader fileReader = new FileReader(file)) {
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			allContributions = builder.fromJson(reader.get("all-contributions"),
					new TypeToken<HashMap<String, Long>>() {
					}.getType());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createFileIfNotExists(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void checkForFillEvent() {
		CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (MONEY_TOWARDS_PC_POINTS >= PC_COINS_REQUIRED && PC_POINTS_TIMER == 0) {
					MONEY_TOWARDS_PC_POINTS = MONEY_TOWARDS_PC_POINTS - PC_COINS_REQUIRED;
					sendActivateMessage("+5 bonus PC points");
					PC_POINTS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
					QuestTab.updateAllQuestTabs();
				}

				if (MONEY_TOWARDS_DROP_RATE_BOOST >= DROP_RATE_COINS_REQUIRED && _20_PERCENT_DROP_RATE_TIMER == 0) {
					MONEY_TOWARDS_DROP_RATE_BOOST = MONEY_TOWARDS_DROP_RATE_BOOST - DROP_RATE_COINS_REQUIRED;
					sendActivateMessage("+10% drop rate");
					_20_PERCENT_DROP_RATE_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
					QuestTab.updateAllQuestTabs();
				}

				if (MONEY_TOWARDS_EXPERIENCE >= EXPERIENCE_COINS_REQUIRED && EXPERIENCE_TIMER == 0) {
					MONEY_TOWARDS_EXPERIENCE = MONEY_TOWARDS_EXPERIENCE - EXPERIENCE_COINS_REQUIRED;
					sendActivateMessage("bonus experience");
					EXPERIENCE_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
				}
			}
		}, 1);
	}

	public static void sendActivateMessage(String action) {
		PlayerHandler.newsMessage("The Well of Goodwill is granting " + action + " for the next hour!");
	}

	public static void votingBonus() {
		PlayerHandler.executeGlobalMessage("@cr10@[@pur@VOTE@bla@] Global votes have reached @pur@50@bla@! 30 minutes of +10% drop rate activated!");
		_20_PERCENT_DROP_RATE_TIMER += TimeUnit.HOURS.toMillis(1) / 1200;
		QuestTab.updateAllQuestTabs();
	}

	public static void donate(Player player, int amount, int itemId, int choice) {
		if (amount < LEAST_ACCEPTED_AMOUNT) {
			player.sendMessage("You must contribute at least one million coins.");
			return;
		}
		if (itemId == -1 && !player.getItems().playerHasItem(995, amount)) {
			player.sendMessage("You do not have " + Misc.getValueWithoutRepresentation(amount) + " to contribute.");
			return;
		}
		if (itemId != -1 && !player.getItems().playerHasItem(itemId, 1)) {
			player.sendMessage("You do not have the correct item to burn.");
			return;
		}
		if (amount >= 10_000_000) {
			player.getEventCalendar().progress(EventChallenge.CONTRIBUTE_10M_TO_THE_WOGW, 1);
		}
		if (itemId == -1) {
			player.getItems().deleteItem(995, amount);
		} else {
			player.getItems().deleteItem(itemId, 1);
		}
		player.getPA().sendInterfaceHidden(1, 38020);

		String towards = player.getWogwContributeInterface().getSelectedButton() == WogwInterfaceButton.PEST_CONTROL_BOOST ? "+5 bonus PC Points!"
						: player.getWogwContributeInterface().getSelectedButton() == WogwInterfaceButton.EXPERIENCE_BOOST ? "bonus experience!"
						: player.getWogwContributeInterface().getSelectedButton() == WogwInterfaceButton.DROP_RATE_BOOST ? "+10% drop rate!" : "";
		player.sendMessage("@blu@You have donated " + Misc.formatCoins(amount) + " to the Well towards " + towards);
		Achievements.increase(player, AchievementType.WOGW, amount);

		//Announce contributions over 10m
		if (amount >= 10_000_000) {
			PlayerHandler.newsMessage(player.getDisplayNameFormatted() + " has contributed " + Misc.getValueWithoutRepresentation(amount) + " to the Well of Goodwill!");
		}

		//Setting the amounts and enabling bonus if the amount reaches above required
		if (itemId == -1) {
			switch (player.getWogwContributeInterface().getSelectedButton()) {
				case EXPERIENCE_BOOST:
					MONEY_TOWARDS_EXPERIENCE += amount;
					break;

				case PEST_CONTROL_BOOST:
					MONEY_TOWARDS_PC_POINTS += amount;
					break;

				case DROP_RATE_BOOST:
					MONEY_TOWARDS_DROP_RATE_BOOST += amount;
					break;
			}
		} else {
			switch (choice) {
				case 0:
					MONEY_TOWARDS_EXPERIENCE += amount;
					break;
				case 1:
					MONEY_TOWARDS_PC_POINTS += amount;
					break;
				case 2:
					MONEY_TOWARDS_DROP_RATE_BOOST += amount;
					break;
			}
		}

		//Remove them if present and readd, this will refresh the list, that way they are displayed if they donate more.
		recentContributors.remove(player);

		/* If the list has more than 4 (max shown on interface),
		* remove the first (oldest) one. This will shift the list up so oldest always gets removed */
		if (recentContributors.size() > 3) {
			recentContributors.remove(0);
		}

		//Add the player to the list.
		recentContributors.add(player);

		//Add the players contributions to all contributions, used to track gp sink and top contributor
		if (!player.isManagement()) {
			if (allContributions.containsKey(player.getDisplayName())) {
				allContributions.replace(player.getDisplayName(), allContributions.get(player.getDisplayName()) + amount);
			} else {
				allContributions.put(player.getDisplayName(), (long) amount);
			}
		}

		player.getWogwContributeInterface().open();
		player.getQuestTab().updateInformationTab();
	}

	public static String getTopContributor() {
		String topContributor = null;
		long highestAmount = 0;
		for (String player : allContributions.keySet()) {
			if (allContributions.get(player) > highestAmount) {
				highestAmount = allContributions.get(player);
				topContributor = player;
			}
		}
		return topContributor == null ? "N/A" : topContributor;
	}

	public static long getTopContributorAmount() {
		if (getTopContributor().equals("N/A")) {
			return 0;
		}
		return allContributions.get(getTopContributor());
	}

	public static long getTotalCoinsUsed() {
		long total = 0;
		for (String player : allContributions.keySet()) {
			total += allContributions.get(player);
		}
		return total;
	}

}
