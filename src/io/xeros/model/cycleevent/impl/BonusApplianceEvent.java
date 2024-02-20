package io.xeros.model.cycleevent.impl;

import io.xeros.Configuration;
import io.xeros.content.bosses.hespori.*;
import io.xeros.content.wogw.Wogw;
import io.xeros.model.cycleevent.Event;
import io.xeros.model.entity.player.PlayerHandler;
import io.xeros.util.Misc;

import java.util.concurrent.TimeUnit;

public class BonusApplianceEvent extends Event<Object> {
	
	/**
	 * The amount of time in game cycles (600ms) that the event pulses at
	 */
	private static final int INTERVAL = Misc.toCycles(1, TimeUnit.SECONDS);

	/**
	 * Creates a new event to cycle through messages for the entirety of the runtime
	 */
	public BonusApplianceEvent() {
		super("", new Object(), INTERVAL);
	}

	private void bonusExpiredMessage(String action) {
		PlayerHandler.newsMessage("The Well of Goodwill is no longer granting " + action + ".");
	}

	@Override
	public void execute() {
		if (Wogw.EXPERIENCE_TIMER > 0) {
			Wogw.EXPERIENCE_TIMER--;
			if (Wogw.EXPERIENCE_TIMER == 1) {
				bonusExpiredMessage("bonus experience");
			}
		}
		if (Wogw.PC_POINTS_TIMER > 0) {
			Wogw.PC_POINTS_TIMER--;
			if (Wogw.PC_POINTS_TIMER == 1) {
				bonusExpiredMessage("bonus PC points");
			}
		}
		if (Configuration.DOUBLE_DROPS_TIMER > 0) {
			Configuration.DOUBLE_DROPS_TIMER--;
			if (Configuration.DOUBLE_DROPS_TIMER == 1) {
				bonusExpiredMessage("double drops");
			}
		}
		if (Wogw._20_PERCENT_DROP_RATE_TIMER > 0) {
			Wogw._20_PERCENT_DROP_RATE_TIMER--;
			if (Wogw._20_PERCENT_DROP_RATE_TIMER == 1) {
				bonusExpiredMessage("+10% drop rate");
			}
		}

		/**
		 * Hespori Seeds
		 */
		if (Hespori.ATTAS_TIMER > 0) {
			Hespori.ATTAS_TIMER--;
			if (Hespori.ATTAS_TIMER == 1) {
				PlayerHandler.newsMessage("Additional xp is no longer active.");
				new AttasBonus().deactivate();
			}
		}
		if (Hespori.KRONOS_TIMER > 0) {
			Hespori.KRONOS_TIMER--;
			if (Hespori.KRONOS_TIMER == 1) {
				PlayerHandler.newsMessage("Double CoX keys event is no longer active.");
				new KronosBonus().deactivate();
			}
		}
		if (Hespori.IASOR_TIMER > 0) {
			Hespori.IASOR_TIMER--;
			if (Hespori.IASOR_TIMER == 1) {
				PlayerHandler.newsMessage("Drop rate bonus is no longer active.");
				new IasorBonus().deactivate();
			}
		}

		if (Hespori.GOLPAR_TIMER > 0) {
			Hespori.GOLPAR_TIMER--;
			if (Hespori.GOLPAR_TIMER == 1) {
				PlayerHandler.newsMessage("More loot is no longer active.");
				new GolparBonus().deactivate();
			}
		}
		if (Hespori.BUCHU_TIMER > 0) {
			Hespori.BUCHU_TIMER--;
			if (Hespori.BUCHU_TIMER == 1) {
				PlayerHandler.newsMessage("Double boss points is no longer active.");
				new BuchuBonus().deactivate();
			}
		}
		if (Hespori.KELDA_TIMER > 0) {
			Hespori.KELDA_TIMER--;
			if (Hespori.KELDA_TIMER == 1) {
				PlayerHandler.newsMessage("Double larrans keys is no longer active.");
				new KeldaBonus().deactivate();
			}
		}
		if (Hespori.NOXIFER_TIMER > 0) {
			Hespori.NOXIFER_TIMER--;
			if (Hespori.NOXIFER_TIMER == 1) {
				PlayerHandler.newsMessage("Double slayer points is no longer active.");
				new NoxiferBonus().deactivate();
			}
		}
		if (Hespori.CELASTRUS_TIMER > 0) {
			Hespori.CELASTRUS_TIMER--;
			if (Hespori.CELASTRUS_TIMER == 1) {
				PlayerHandler.newsMessage("Double brimstone keys is no longer active.");
				new CelastrusBonus().deactivate();
			}
		}


	}
}
