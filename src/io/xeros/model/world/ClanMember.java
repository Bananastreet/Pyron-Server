package io.xeros.model.world;

import io.xeros.model.entity.player.Player;

public class ClanMember {

    private final String loginName;
    private final String displayName;
    private final String formattedName;

    public ClanMember(Player player) {
        this.loginName = player.getLoginName();
        this.displayName = player.getDisplayName();
        this.formattedName = player.getDisplayNameFormatted();
    }

    public ClanMember(String loginName, String displayName, String formattedName) {
        this.loginName = loginName;
        this.displayName = displayName;
        this.formattedName = formattedName;
    }

    public boolean is(Player player) {
        return loginName.equalsIgnoreCase(player.getLoginName());
    }

    public boolean is(String name) {
        return loginName.equalsIgnoreCase(name) || displayName.equalsIgnoreCase(name);
    }

    public String getLoginName() {
        return loginName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getFormattedName() {
        return formattedName;
    }
}
