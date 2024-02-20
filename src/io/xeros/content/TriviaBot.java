/*package io.xeros.content;


import io.xeros.Configuration;
import io.xeros.model.definitions.ItemDef;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.entity.player.Right;
import io.xeros.model.entity.player.broadcasts.Broadcast;
import io.xeros.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TriviaBot {

    public static int rewardRandom[] = {5020, 23497, 6199, 11740, 7629, 26706, 25365, 25087, 4151, 11840, 6585, 6731, 6733, 6735, 6737, 21046, 12004, 22415, 22416};

    public static int mysteryRandom() {
        return rewardRandom[(int) (Math.random()*rewardRandom.length)];
    }


    public static final int TIMER = 30000; //1000 = 1 minute
    public static int botTimer = TIMER;

    public static int answerCount;
    public static String firstPlace;

    private static List<String> winners = new ArrayList<String>(3);

    public static void sequence() {

        if(botTimer > 0)
            botTimer--;
        if(botTimer <= 0) {
            botTimer = TIMER;
            didSend = false;
            askQuestion();
        }
    }

    private static boolean hasAnswered(Player p) {
        for (int i = 0; i < winners.size(); i++) {
            if (winners.get(i).equalsIgnoreCase(p.getLoginName())) {
                return true;
            }
        }
        return false;
    }


    public static void attemptAnswer(Player p, String attempt) {

        boolean accountInLobby = winners
                .stream()
                .anyMatch(winners -> p.getMacAddress().equalsIgnoreCase(p.getMacAddress()));
        if (accountInLobby) {
            p.sendMessage("@gre@You have already entered the trivia on another account.");
            p.getPA().removeAllWindows();
            return;
        }

        if (hasAnswered(p)) {
            p.sendMessage("@gre@You have already answered the trivia question!");
            return;
        }

        if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {

            if (answerCount == 0) {
                int reward = mysteryRandom();
                String itemNameLowerCase = ItemDef.forId(reward).getName().toLowerCase();
                p.getItems().addItemUnderAnyCircumstance(reward, 1);
                answerCount++;
                firstPlace = p.getLoginName();
                PlayerHandler.executeGlobalMessage("@cr10@@gre@[TRIVIA] @blu@" + firstPlace + " has won a "+ itemNameLowerCase +" from trivia. ");
                resetForNextQuestion();
                currentQuestion = "";
                didSend = false;
                botTimer = TIMER;
                answerCount = 0;
            }


        } else {
            if(attempt.contains("question") || attempt.contains("repeat")){
                p.sendMessage("<col=800000>"+(currentQuestion));
                return;
            }
            p.sendMessage("<img=10> Trivia: Sorry! Wrong answer! "+(currentQuestion));
        }

    }


    public static void resetForNextQuestion() {
        if (!winners.isEmpty()) {
            winners.clear();
        }
        answerCount = 0;
    }

    public static boolean acceptingQuestion() {
        return !currentQuestion.equals("");
    }

    private static void askQuestion() {
        for (int i = 0; i < TRIVIA_DATA.length; i++) {
            if (Misc.random(TRIVIA_DATA.length - 1) == i) {
                if(!didSend) {
                    didSend = true;
                    currentQuestion = TRIVIA_DATA[i][0];
                    currentAnswer = TRIVIA_DATA[i][1];
                    resetForNextQuestion();
                    PlayerHandler.executeGlobalMessage(currentQuestion);
                    PlayerHandler.executeGlobalMessage("@cr10@@gre@[TRIVIA] @blu@You can answer trivia using ::answer responsehere");

                }
            }
        }
    }

    public static boolean didSend = false;

    private static final String[][] TRIVIA_DATA = {

            {"@cr10@@gre@[TRIVIA] @blu@What is the name of this server?", "Vanguard"},
            {"@cr10@@gre@[TRIVIA] @blu@What is 10 x 5 - 32?", "18"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the highest level for each skill?", "99"},
            {"@cr10@@gre@[TRIVIA] @blu@Who is the owner of Vanguard?", "Grim"},
            {"@cr10@@gre@[TRIVIA] @blu@What is 100 x 3 + 42?", "342"},
            {"@cr10@@gre@[TRIVIA] @blu@How many categories of perks are there?", "3"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the highest tier you can upgrade an item to?", "5"},
            {"@cr10@@gre@[TRIVIA] @blu@What year did osrs release?", "2013"},
            {"@cr10@@gre@[TRIVIA] @blu@What year was RS released?", "2001"},
            {"@cr10@@gre@[TRIVIA] @blu@OSRS was based on a game backup from what year?", "2007"},
            {"@cr10@@gre@[TRIVIA] @blu@What ship sank on its maiden voyage in 1912?", "titanic"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the 7th month of the year?", "july"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the 3rd month of the year?", "march"},
            {"@cr10@@gre@[TRIVIA] @blu@How many days are there in a year?", "365"},
            {"@cr10@@gre@[TRIVIA] @blu@How many hours are there in each week?", "168"},
            {"@cr10@@gre@[TRIVIA] @blu@What company makes the iphone?", "apple"},
            {"@cr10@@gre@[TRIVIA] @blu@How many weeks are in one year?", "52"},
            {"@cr10@@gre@[TRIVIA] @blu@How many states make up the USA?", "50"},
            {"@cr10@@gre@[TRIVIA] @blu@Which ocean is the largest?", "pacific"},
            {"@cr10@@gre@[TRIVIA] @blu@The answer is: 3ke3aaab", "3ke3aaab"},
            {"@cr10@@gre@[TRIVIA] @blu@Which continent is the largest?", "asia"},
            {"@cr10@@gre@[TRIVIA] @blu@The answer is: ilovemboxes", "ilovemboxes"},
            {"@cr10@@gre@[TRIVIA] @blu@The answer is: teapot , spelled backwards", "topaet"},
            {"@cr10@@gre@[TRIVIA] @blu@Which wilderness boss is an angry bear?", "callisto"},
            {"@cr10@@gre@[TRIVIA] @blu@What skill allows you to catch implings?", "hunter"},
            {"@cr10@@gre@[TRIVIA] @blu@What skill involves mining rocks?", "mining"},
            {"@cr10@@gre@[TRIVIA] @blu@What is 19 + 31 multiplied by 5", "250"},
            {"@cr10@@gre@[TRIVIA] @blu@What is 78 - 8 multiplied by 2", "140"},
            {"@cr10@@gre@[TRIVIA] @blu@What is 3 + 6 divided by 3", "3"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the link to our website?", "vanguardrsps.com"},
            {"@cr10@@gre@[TRIVIA] @blu@What gem is dropped by demonic gorillas?", "zenyte"},
            {"@cr10@@gre@[TRIVIA] @blu@What is the highest prestige for each skill?", "10"},
            {"@cr10@@gre@[TRIVIA] @blu@What is vanguard spelled backwards?", "draugnav"},
            {"@cr10@@gre@[TRIVIA] @blu@The answer is 100 + 50 - 75?", "75"},
            {"@cr10@@gre@[TRIVIA] @blu@How many banker npcs are in the bank at home?", "4"},


    };

    public static String currentQuestion;
    private static String currentAnswer;
} */