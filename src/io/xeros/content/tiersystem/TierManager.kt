package io.xeros.content.tiersystem

import io.xeros.content.dialogue.DialogueBuilder
import io.xeros.content.dialogue.DialogueOption
import io.xeros.content.tiersystem.TierConstants.ITEM_CONTAINER_MAIN_TAB
import io.xeros.content.tiersystem.TierConstants.ITEM_LIST_BUTTONS
import io.xeros.content.tiersystem.TierConstants.ITEM_LIST_SCROLL
import io.xeros.content.tiersystem.TierConstants.ITEM_LIST_START
import io.xeros.content.tiersystem.TierConstants.ITEM_NAME_START_MAIN_TAB
import io.xeros.content.tiersystem.TierConstants.PROGRESS_AMT_START_MAIN_TAB
import io.xeros.content.tiersystem.TierConstants.TOKEN_AMT_START_MAIN_TAB
import io.xeros.content.tiersystem.TierConstants.TOKEN_ITEM
import io.xeros.content.tiersystem.TierConstants.upgradeButtons
import io.xeros.content.tiersystem.UpgradeableItems.Companion.getValidItems
import io.xeros.model.definitions.ItemDef
import io.xeros.model.entity.player.Player
import io.xeros.model.entity.player.PlayerHandler
import io.xeros.model.items.ItemAssistant
import io.xeros.util.Misc

class TierManager(val player : Player) {

    private val tabButtons = listOf(45298,45299,45300)
    private val fetchSlotFromIndex : MutableMap<Int,Int> = emptyMap<Int, Int>().toMutableMap()

    fun open() {
        switchTab(0)
        player.pa.showInterface(TierConstants.INTERFACE_ID)
        player.pa.sendString(45307,"Total Tokens\\n${Misc.formatNumber(player.items.getItemAmount(TOKEN_ITEM).toLong())}")
    }

    private fun switchTab(id : Int) {
        val replaceWith = when(id) {
            0 -> TierConstants.WIDGET_MAIN
            1 -> TierConstants.WIDGET_ITEM_LIST
            2 -> TierConstants.WIDGET_INFO
            else ->  TierConstants.WIDGET_MAIN
        }
        tabButtons.forEach { player.pa.sendSprite(it,24,25) }

        player.pa.sendSprite(tabButtons[0] + id,23,23)

        player.pa.sendParent(TierConstants.INTERFACE_ID,16,replaceWith)
        updateTab(id)
    }

    fun clickButton(id : Int) {
        //System.out.println(id)
        when(id) {
            in tabButtons -> switchTab(id - tabButtons[0])
            in upgradeButtons -> getValidItems()[fetchSlotFromIndex[id - upgradeButtons.first]]?.let { doUpgrade(it) }
            in ITEM_LIST_BUTTONS -> updateItemInfoSidePanel(id - ITEM_LIST_BUTTONS.first)
            45295 -> player.pa.closeAllWindows()
        }
    }

    private fun updateTab(id : Int) {
        when(id) {
            0 -> mainTab()
            1 -> itemInfo()
            2 -> player.pa.sendString(45147,
                "Obtaining Upgrade Tokens" +
                        "\\n" +
                        "\\n- AFK Token Shop" +
                        "\\n- Vote Shop" +
                        "\\n- Exchange Shop" +
                        "\\n- Stardust Shop" +
                        "\\n - Donator token Shop" +
                        "\\n - Corrupted Slayer Keys" +
                        "\\n- Nex" +
                        "\\n- Vote Bos" +
                        "\\n- Chambers of Xeric" +
                        "\\n- Theatre of Blood" +
                        "\\n- Voting" +
                        "\\n- Donator Boss" +
                        "\\n" +
                        "\\nThe main screen will show all items on you." +
                        "\\nIf they can be upgraded, you can select it." +
                        "\\nIf you have enough tokens it will be upgraded.")
        }
    }

    private fun mainTab() {
        fetchSlotFromIndex.clear()
        repeat(28) {
            player.pa.sendString(ITEM_NAME_START_MAIN_TAB + it, "---")
            player.pa.sendString(PROGRESS_AMT_START_MAIN_TAB + it, "---")
            player.pa.sendString(TOKEN_AMT_START_MAIN_TAB + it, "---")
            player.pa.sendFrame34a(ITEM_CONTAINER_MAIN_TAB, -1, it, -1)
        }

        player.items.inventoryItems.forEachIndexed { index, item ->
            var itemName = item.def.name
            var progress = "N/A"
            var tokenAmt = ""
            var itemAmt = 0

            if ((getValidItems().contains(item.id))) {
                itemName = item.def.name
                progress = "${(getValidItems()[item.id]?.tierLevel?: 1) + 1} / 5"
                tokenAmt = Misc.formatCoins(getValidItems()[item.id]?.tokensNeeded?.toLong() ?: 0)
                itemAmt = item.amount
                player.pa.sendFrame34a(ITEM_CONTAINER_MAIN_TAB, item.id, index, item.amount)
                fetchSlotFromIndex[index] = item.id
            }

            player.pa.sendString(ITEM_NAME_START_MAIN_TAB + index, itemName)
            player.pa.sendString(PROGRESS_AMT_START_MAIN_TAB + index, progress)
            player.pa.sendString(TOKEN_AMT_START_MAIN_TAB + index, tokenAmt)
            player.pa.sendFrame34a(ITEM_CONTAINER_MAIN_TAB, item?.id ?: -1, index, itemAmt)
        }

    }

    private fun doUpgrade(data: TierData) {
        DialogueBuilder(player).statement("Are you sure you wish to upgrade this.").option(
            DialogueOption("Yes") {
                if (player.getItems().getItemAmount(TOKEN_ITEM) >= data.tokensNeeded) {
                    player.items.deleteItem(TOKEN_ITEM,data.tokensNeeded)
                    player.items.deleteItem(data.itemID,1)
                    player.items.addItem(data.replacmentItem,1)
                    //player.sendMessage("You have upgraded your "+ ItemAssistant.getItemName(data.itemID) +" to "+ ItemAssistant.getItemName(data.replacmentItem))
                    if (ItemAssistant.getItemName(data.replacmentItem).lowercase().contains("t5")) {
                        PlayerHandler.dropMessage(player.getDisplayNameFormatted() + " has upgraded to the "+ItemAssistant.getItemName(data.replacmentItem)+"!")
                    }
                    player.sendStatement("You do not have enough tokens (Required: ${data.tokensNeeded})")
                    it.pa.closeAllWindows()
                    player.getDH().sendItemStatement("You've created a "+ ItemAssistant.getItemName(data.replacmentItem), data.replacmentItem)
                } else {
                    it.pa.closeAllWindows()
                    player.sendMessage("You do not have enough tokens.")
                }

                //player.tierManager.open()
            },
            DialogueOption("No") { it.pa.closeAllWindows() }
        ).send()
    }

    private fun itemInfo() {
        updateItemInfoSidePanel(0)
        player.pa.resetScrollBar(ITEM_LIST_SCROLL)
        player.pa.setScrollableMaxHeight(ITEM_LIST_SCROLL,UpgradeableItems.values().size * 19)
        repeat(60) {
            player.pa.sendInterfaceHidden(1,ITEM_LIST_START + it)
        }

        UpgradeableItems.values().forEachIndexed { index, item ->
            player.pa.sendString(ITEM_LIST_START + index, Misc.formatPlayerName(item.name).replace("_"," "))
            player.pa.sendInterfaceHidden(0,ITEM_LIST_START + index)
        }
    }

    fun updateItemInfoSidePanel(index : Int) {
        val data : UpgradeableItems = UpgradeableItems.values()[index]
        player.pa.sendFrame34a(50745, data.teirs.first().itemID, 0, 1)
        player.pa.sendString(50746, Misc.formatPlayerName(data.name.replace("_"," ")))
        player.pa.sendString(50747, "Max Tier: ${data.maxTier}")
        var itemList = ""
        var space = ""
        data.teirs.forEach {
            itemList += "- ${ItemDef.forId(it.itemID).name}\\n"
            space += "\\n"
        }
        space += "\\n"
        player.pa.sendString(50749, itemList)
        player.pa.sendString(50750, "${space} ${data.info}")
    }

}