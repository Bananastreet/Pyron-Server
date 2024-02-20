package io.xeros.content.upgrade;

import io.xeros.model.items.GameItem;

public enum UpgradeData {

	/*
	 * Weapons
	 */
	ABYSSAL_TENTACLE(1, 113093, UpgradeType.WEAPON, 12006, 100, false, 0, -1, new GameItem(4151, 1), new GameItem(12004, 1)),
	BOW_OF_FAERDHINEN(1, 113094, UpgradeType.WEAPON, 25865, 100, false, 0, -1, new GameItem(25859, 1)),
	BLADE_OF_SAELDOR(1, 113095, UpgradeType.WEAPON, 23995, 100, false, 0, -1, new GameItem(25859, 1)),
	CORRUPT_BLADE_OF_SAELDOR(1, 113096, UpgradeType.WEAPON, 24551, 100, false, 0, -1, new GameItem(23995, 1), new GameItem(23831, 1), new GameItem(995, 50000000)),
	ENERGIZED_BLADE_OF_SAELDOR(1, 113097, UpgradeType.WEAPON, 25872, 100, false, 0, -1, new GameItem(24551, 1), new GameItem(25959, 1), new GameItem(995, 65000000), new GameItem(4067, 100)),
	ENHANCED_ZAMORAKIAN_SPEAR(1, 113098, UpgradeType.WEAPON, 29075, 100, false, 0, -1, new GameItem(11824, 1), new GameItem(995, 75000000)),
	/*
	 * Armour
	 */
	AVAS_ASSEMBLER(1, 113093, UpgradeType.ARMOR, 22109, 100, false, 1, -1, new GameItem(10499, 1), new GameItem(2425)),
	BOOTS_OF_ZENITH(1, 113094, UpgradeType.ARMOR, 3693, 100, false, 1, -1, new GameItem(13239, 1), new GameItem(13237, 1), new GameItem(13235, 1), new GameItem(995, 50000000), new GameItem(4067, 25)),
	BLOOD_FURY(1, 113095, UpgradeType.ARMOR, 24780, 100, false, 1, -1, new GameItem(6585, 1), new GameItem(24777)),
	FIGHTER_TORSO_IMBUED(1, 113096, UpgradeType.ARMOR, 3695, 100, false, 1, -1, new GameItem(10551, 1), new GameItem(995, 8000000)),
	CRYSTAL_HELM(1, 113097, UpgradeType.ARMOR, 23971, 100, false, 1, -1, new GameItem(23956, 1)),
	CRYSTAL_BODY(1, 113098, UpgradeType.ARMOR, 23975, 100, false, 1, -1, new GameItem(23956, 2)),
	CRYSTAL_LEGS(1, 113099, UpgradeType.ARMOR, 23979, 100, false, 1, -1, new GameItem(23956, 2)),
	NECKLACE_OF_ZENITH(1, 113100, UpgradeType.ARMOR, 23240, 100, false, 1, -1, new GameItem(19553, 1), new GameItem(19547, 1), new GameItem(12002, 1), new GameItem(24780, 1), new GameItem(12018, 1),new GameItem(22111, 1), new GameItem(4067, 25), new GameItem(995, 20000000)),
	RING_OF_ZENITH(1, 113101, UpgradeType.ARMOR, 25541, 100, false, 1, -1, new GameItem(11770, 1), new GameItem(11771, 1), new GameItem(11773, 1), new GameItem(19550, 1), new GameItem(12785, 1), new GameItem(12603, 1), new GameItem(12601, 1), new GameItem(4067, 25)),
	ZENITH_GLOVES(1, 113102, UpgradeType.ARMOR, 3717, 50, false, 1, 22981, new GameItem(22981, 1), new GameItem(19544, 1), new GameItem(995, 15000000)),



	/*
	 * Tools
	 */

	INFERNAL_AXE(1, 113093, UpgradeType.TOOL, 13241, 100, false, 0, -1, new GameItem(6739, 1), new GameItem(13233, 1)),
	ROW_IMBUED(1, 113094, UpgradeType.TOOL, 12785, 100, false, 0, -1, new GameItem(2572, 1), new GameItem(12783, 1)),
	SIGIL_OF_BLOOD(1, 113095, UpgradeType.TOOL, 26125, 100, false, 0, -1, new GameItem(24777, 2), new GameItem(995, 10000000)),
	SIGIL_OF_Vanguard(1, 113096, UpgradeType.TOOL, 26146, 100, false, 0, -1, new GameItem(25990, 1), new GameItem(26017, 1), new GameItem(26041, 1), new GameItem(26065, 1), new GameItem(26125, 1),new GameItem(26128, 1), new GameItem(26032, 1), new GameItem(26062, 1)),
	SUPER_MYSTERY_BOX(1, 113097, UpgradeType.TOOL, 6828, 40, false, 0, -1, new GameItem(6199, 1)),
	ULTRA_MYSTERY_BOX(1, 113098, UpgradeType.TOOL, 13346, 35, false, 0, -1, new GameItem(6828, 1)),
	MASTER_MYSTERY_CHEST(1, 113099, UpgradeType.TOOL, 8167, 25, false, 0, -1, new GameItem(13346, 1)),
	DARK_IMP(1, 113100, UpgradeType.TOOL, 30111, 75, false, 0, 30011, new GameItem(30011, 1), new GameItem(8171, 1)),
	DARK_SHADOW_WARRIOR(1, 113101, UpgradeType.TOOL, 30115, 20, false, 0, -1, new GameItem(30015, 1)),
	DARK_SHADOW_ARCHER(1, 113102, UpgradeType.TOOL, 30116, 20, false, 0, -1, new GameItem(30016, 1)),
	DARK_SHADOW_WIZARD(1, 113103, UpgradeType.TOOL, 30117, 20, false, 0, -1, new GameItem(30017, 1)),
	DARK_SEREN(1, 113104, UpgradeType.TOOL, 30123, 20, false, 0, -1, new GameItem(23939, 1)),
	;

	private final UpgradeType type;

	public final int buttonId;

	public final int clickId;

	private int resultItem;

	private float successRate;

	private boolean otherCurrency;

	private int currencyAmount;

	private GameItem[] ingredients;

	private int safeItem;
	
	
	
	UpgradeData(int buttonId, int clickId, UpgradeType type, int resultItem, float successRate, boolean otherCurrency, int currencyAmount, int safeItem, GameItem... ingredients) {
		this.buttonId = buttonId;
		this.clickId = clickId;
		this.type = type;
		this.resultItem = resultItem;
		this.successRate = successRate;
		this.otherCurrency = otherCurrency;
		this.currencyAmount = currencyAmount;
		this.ingredients = ingredients;
		this.safeItem = safeItem;
	}

	
	public int getButtonId() {
		return buttonId;
	}
	
	public int getClickId() {
		return clickId;
	}
	
	public UpgradeType getType() {
		return type;
	}
	
	public int getResultItem() {
		return resultItem;
	}
	
	public float getSuccessRate() {
		return successRate;
	}
	
	public boolean getOtherCurrency() {
		return otherCurrency;
	}
	
	public int getCurrencyAmount() {
		return currencyAmount;
	}
	
	public GameItem[] getIngredients() {
		return ingredients;
	}
	
	public int getSafeItem() {
		return safeItem;
	}
	

}