package io.xeros.content.worldevent.events;

import io.xeros.Server;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.model.world.objects.GlobalObject;
import io.xeros.util.Misc;

import java.util.ArrayList;

public class ShootingStar {

    public static int dustsRemaining;
    public static int starStage = 0;
    public static int[] starId = { 41020, 41021, 41223, 41224, 41227 };
    public static int currentStarX;
    public static int currentStarY;
    public static boolean active;
    private static boolean alreadyRemoved;

    static GlobalObject shootingStar = null;


    private enum Locations {
        EDGEVILLE(3104, 3501, "in Edgeville"),
        LUMBRIDGE(3233, 3221, "by the Lumbridge castle"),
        VARROCK(3217, 3425, "by Varrock Square"),
        DRAYNOR(3099, 3235, "in Draynor Village"),
        YANILLE(2617, 3109, "outside Yanille"),
        PEST_CONTROL(2659, 2660, "on the Void Knights' Outpost"),
        FOSSIL_ISLAND(3728, 3810, "on Fossil Island");

        private final int xCoord;
        private final int yCoord;
        private final String location;

        Locations(int xCoord, int yCoord, String location) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.location = location;
        }

        public int getXCoord() { return xCoord; }
        public int getYCoord() { return yCoord; }
        public String getLocation() { return location; }

    }

    public static void startShootingStar() {
        ArrayList<Integer> possibleLocations = new ArrayList<>();
        for (final Locations loc : Locations.values()) {
            possibleLocations.add(loc.ordinal());
        }

        Locations loc = Locations.values()[possibleLocations.get(Misc.random(possibleLocations.size() - 1))];

        alreadyRemoved = false;
        currentStarX = loc.getXCoord();
        currentStarY = loc.getYCoord();
        shootingStar = new GlobalObject(starId[starStage], currentStarX, currentStarY, 0);
        Server.getGlobalObjects().add(shootingStar);
        PlayerHandler.newsMessage("A shooting star has crashed " + loc.getLocation() + "! ::star");
        dustsRemaining = 600;
        active = true;
    }

    public static void updateStar(String type) {
        switch (type) {
            case "dust":
                dustsRemaining--;
                if (dustsRemaining <= 0) {
                    endShootingStar();
                    return;
                }
                int[] dustThreshold = { 280, 210, 140, 70, 0 };
                if (dustsRemaining == dustThreshold[starStage]) {
                    starStage += 1;
                    updateStar("newStarObject");
                }
                break;

            case "newStarObject":
                Server.getGlobalObjects().updateObject(shootingStar, starId[starStage]);
                break;
        }
    }

    public static void endShootingStar() {
        if (alreadyRemoved) {
            return;
        }
        Server.getGlobalObjects().remove(shootingStar);
        shootingStar = null;
        currentStarX = 0;
        currentStarY = 0;
        dustsRemaining = 0;
        starStage = 0;
        active = false;
        alreadyRemoved = true;
        PlayerHandler.newsMessage("The crashed star has been depleted of its resources.");
    }

}