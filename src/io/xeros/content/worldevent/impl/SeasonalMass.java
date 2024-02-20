package io.xeros.content.worldevent.impl;

public class SeasonalMass {

    public static boolean massActive = false;
    public static final int TIMER = 600; // 600ms
    public static final int TICKS_PER_HOUR = 1000;
    public static final int MASS_ACTIVE_DURATION = 20 * TICKS_PER_HOUR; // 20 minutes in ticks

    private static int tickCounter = 0;

    public static void sequence() {
        tickCounter++;

        // Check if 1 hour has passed
        if (tickCounter % TICKS_PER_HOUR == 0) {
            massActive = true;
        }

        // Check if mass has been active for 20 minutes
        if (massActive && tickCounter % MASS_ACTIVE_DURATION == 0) {
            massActive = false;
        }

        // Continue with the existing logic for activating and deactivating mass
        if (!massActive) {
            if (tickCounter % TIMER == 0) {
                // Reset the timer every 600ms
                tickCounter = 0;
            }
        }
    }
}
