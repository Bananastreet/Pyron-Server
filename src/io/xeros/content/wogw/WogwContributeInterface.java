package io.xeros.content.wogw;

import com.google.common.collect.Lists;
import io.xeros.model.entity.player.Player;
import io.xeros.util.Misc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WogwContributeInterface {

    public static final int INTERFACE_ID = 22931;
    public static final int WOGW_BONUS_BUTTON_SELECTION = 1373;
    private static final int CONTRIBUTE_BUTTON = 22940;
    private static final int CLOSE_BUTTON = 22942;
    private static final int TOP_CONTRIBUTOR_TEXT = 22935;
    private static final int RECENT_CONTRIBUTORS_CONTAINER = 22937;

    private final Player player;
    private WogwInterfaceButton selectedButton = WogwInterfaceButton.EXPERIENCE_BOOST;

    public WogwContributeInterface(Player player) {
        this.player = player;
    }

    public void open() {
        List<Player> recent = Wogw.recentContributors;

        updateConfig();

        player.getPA().sendString(TOP_CONTRIBUTOR_TEXT, Wogw.getTopContributor());
        player.getPA().sendStringContainer(RECENT_CONTRIBUTORS_CONTAINER, recent.isEmpty() ? Lists.newArrayList("N/A") : recent.stream().limit(4).map(Player::getDisplayName).collect(Collectors.toList()));

        for (WogwInterfaceButton button : WogwInterfaceButton.values()) {
            player.getPA().sendString(button.getCoinsTextId(), Misc.getCoinColour(button.getCurrentCoins()) + Misc.formatCoins(button.getCurrentCoins()) + "</col>/" + Misc.getCoinColour(button.getCoinsRequired()) + Misc.formatCoins(button.getCoinsRequired()));
        }

        player.getPA().showInterface(INTERFACE_ID);
    }

    private void updateConfig() {
        player.getPA().sendConfig(WOGW_BONUS_BUTTON_SELECTION, selectedButton.ordinal());
    }

    public boolean clickButton(int buttonId) {
        if (buttonId == CONTRIBUTE_BUTTON) {
            player.getPA().sendEnterAmount("Donate to " + selectedButton.toString(), (p, a) -> Wogw.donate(player, a, -1, -1));
            return true;
        }

        if (buttonId == CLOSE_BUTTON) {
            player.getPA().closeAllWindows();
            return true;
        }

        if (buttonId == TOP_CONTRIBUTOR_TEXT) {
            player.sendMessage("@blu@" + Wogw.getTopContributor() + " has contributed " + Misc.formatCoins(Wogw.getTopContributorAmount()) + " coins to the Well.");
            return true;
        }

        Optional<WogwInterfaceButton> button = Arrays.stream(WogwInterfaceButton.values()).filter(it -> it.getButtonId() == buttonId).findAny();
        if (button.isPresent()) {
            selectedButton = button.get();
            updateConfig();
            return true;
        }
        return false;
    }

    public WogwInterfaceButton getSelectedButton() {
        return selectedButton;
    }
}
