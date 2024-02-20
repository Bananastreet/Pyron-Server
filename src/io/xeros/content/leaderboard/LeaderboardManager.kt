package io.xeros.content.leaderboard

import io.xeros.content.leaderboard.UpdateLeaderboardData.highscoreData
import io.xeros.content.leaderboard.impl.Boss
import io.xeros.content.leaderboard.impl.Boxes
import io.xeros.content.leaderboard.impl.Raid
import io.xeros.model.entity.player.Player
import io.xeros.model.entity.player.mode.ModeType
import io.xeros.util.Misc

class LeaderboardManager(val player : Player) {

    var currentPage = 0
    var totalPages = 0

    lateinit var selectedTab : LeaderboardData
    lateinit var contentType: ContentType
    lateinit var modeViewing : ModeType
    lateinit var customViewing : String

    fun open() {
        player.pa.showInterface(59820)

    }

    private fun openMain(mode : ModeType = ModeType.STANDARD, custom : String = "") {
        val modeName = if (custom.isNotEmpty()) Misc.formatPlayerName(custom) else mode.formattedName
        player.pa.sendString(46027,"Leaderboards - $modeName")
        modeViewing = mode
        customViewing = custom
        player.pa.showInterface(46025)
        switch(LeaderboardData.BOSSES)
    }

    fun switch(data : LeaderboardData) {
        selectedTab = data
        when(data) {
            LeaderboardData.BOSSES -> loadData(Boss.NEX)
            LeaderboardData.RAIDS -> loadData(Raid.CHAMBER_OF_XERIC)
            LeaderboardData.MYSTERY_BOXES -> loadData(Boxes.NORMAL)
            LeaderboardData.MISCELLANEOUS -> loadData(io.xeros.content.leaderboard.impl.Misc.DONATOR_TOKENS)
        }
        player.pa.sendString(46043, Misc.formatPlayerName(data.name).replace("_"," "))

        player.pa.setScrollableMaxHeight(46044,if (data.content.size <= 8) 0 else (data.content.size * 21))
        repeat(60) {
            var name = ""
            if (data.content.size > it) {
                name = Misc.formatPlayerName(data.content[it].toString()).replace("_"," ")
            }
            player.pa.sendString(46045 + it,name)
            player.pa.sendInterfaceHidden(if (name.isEmpty()) 1 else 0,46045 + it)
        }
        player.pa.sendString(46109,data.verb)

    }

    private fun loadData(type : ContentType, page : Int = 0) {
        currentPage = page
        contentType = type

        val res = highscoreData.getIfPresent(selectedTab)?.filter { it.content == type.toString().lowercase() }

        if (res != null ) {
            val final = when (customViewing) {
                "all" -> res
                "all rogue" -> res.filter {
                    listOf(
                        ModeType.ROGUE,
                        ModeType.ROGUE_IRONMAN,
                        ModeType.ROGUE_HARDCORE_IRONMAN
                    ).contains(it.mode)
                }
                "" -> {
                    when (modeViewing) {
                        ModeType.STANDARD, ModeType.OSRS, ModeType.EVENT_MAN -> res.filter { it.mode == ModeType.STANDARD }
                        ModeType.IRON_MAN -> res.filter { it.mode == ModeType.IRON_MAN }
                        ModeType.ULTIMATE_IRON_MAN -> res.filter { it.mode == ModeType.ULTIMATE_IRON_MAN }
                        ModeType.HC_IRON_MAN -> res.filter { it.mode == ModeType.HC_IRON_MAN }
                        ModeType.ROGUE -> res.filter { it.mode == ModeType.ROGUE }
                        ModeType.ROGUE_HARDCORE_IRONMAN -> res.filter { it.mode == ModeType.ROGUE_HARDCORE_IRONMAN }
                        ModeType.ROGUE_IRONMAN -> res.filter { it.mode == ModeType.ROGUE_IRONMAN }
                        ModeType.GROUP_IRONMAN -> res.filter { it.mode == ModeType.GROUP_IRONMAN }
                    }
                }

                else -> res
            }

            val sorted = final.sortedByDescending { it.amount }

            totalPages = (res.size) / 35

            player.pa.sendSprite(46252,33,if (page == 0) 34 else 34)
            player.pa.sendSprite(46253,35,if (totalPages <= 0) 35 else 36)
            player.pa.sendString(46251,"Page ${page + 1} / ${totalPages + 1}")

            repeat(35) {
                var rank = "--"
                var name = "--"
                var amt = "--"
                val icon = when(it) {
                    0 -> "<cimg=39> "
                    1 -> "<cimg=38> "
                    2 -> "<cimg=37> "
                    else -> ""
                }
                if (sorted?.size!! > it) {
                    rank = "$icon${it + 1}"
                    name = Misc.formatPlayerName(sorted[it].username)
                    amt = sorted[it].amount.toString()
                }
                player.pa.sendString(46146 + it,rank)
                player.pa.sendString(46181 + it,name)
                player.pa.sendString(46216 + it,amt)
            }
        } else {
            repeat(35) {
                val rank = "--"
                val name = "--"
                val amt = "--"
                player.pa.sendString(46146 + it,rank)
                player.pa.sendString(46181 + it,name)
                player.pa.sendString(46216 + it,amt)
            }
        }
        

    }

    fun clickButton(id : Int) {
        //println(id)
        when(id) {
            59824 -> player.pa.closeAllWindows()
            46029 -> player.pa.closeAllWindows()
            46032 -> switch(LeaderboardData.BOSSES)
            46034 -> switch(LeaderboardData.RAIDS)
            46036 -> switch(LeaderboardData.MYSTERY_BOXES)
            46038 -> switch(LeaderboardData.MISCELLANEOUS)
            in (46045..46151) -> {
                val index = id - 46045
                val contentType = when(selectedTab) {
                    LeaderboardData.BOSSES -> Boss.values()[index]
                    LeaderboardData.MISCELLANEOUS -> io.xeros.content.leaderboard.impl.Misc.values()[index]
                    LeaderboardData.RAIDS -> Raid.values()[index]
                    LeaderboardData.MYSTERY_BOXES -> Boxes.values()[index]
                }
                loadData(contentType,0)
            }
            46258 -> if (currentPage != 0) loadData(contentType,currentPage--)
            46259 -> if (currentPage != totalPages) loadData(contentType,currentPage++)
            59826 -> openMain(custom = "all")
            59827 -> openMain(custom = "all rogue")
            59828 -> openMain(mode = ModeType.STANDARD)
            59829 -> openMain(mode = ModeType.IRON_MAN)
            59830 -> openMain(mode = ModeType.ULTIMATE_IRON_MAN)
            59831 -> openMain(mode = ModeType.HC_IRON_MAN)
            59832 -> openMain(mode = ModeType.ROGUE)
            59833 -> openMain(mode = ModeType.ROGUE_HARDCORE_IRONMAN)
            59834 -> openMain(mode = ModeType.ROGUE_IRONMAN)
            59835 -> openMain(mode = ModeType.GROUP_IRONMAN)
            //in (59828..59837) -> openMain(mode = ModeType.values()[id - 59828])
        }
    }



}