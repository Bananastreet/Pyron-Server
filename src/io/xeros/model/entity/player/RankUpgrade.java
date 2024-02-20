package io.xeros.model.entity.player;

enum RankUpgrade {
    REGULAR_DONATOR(Right.SAPPHIRE_DONATOR, 50),
    EXTREME_DONOR(Right.EMERALD_DONATOR, 100),
    LEGENDARY_DONATOR(Right.RUBY_DONATOR,250),
    DIAMOND_CLUB(Right.DIAMOND_DONATOR, 500),
    ONYX_CLUB(Right.ONYX_DONATOR, 1000);

    /**
     * The rights that will be appended if upgraded
     */
    public final Right rights;

    /**
     * The amount required for the upgrade
     */
    public final int amount;

    RankUpgrade(Right rights, int amount) {
        this.rights = rights;
        this.amount = amount;
    }
}
