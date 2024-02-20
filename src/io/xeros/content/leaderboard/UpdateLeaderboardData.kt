package io.xeros.content.leaderboard

import com.github.benmanes.caffeine.cache.*

import io.xeros.model.entity.player.mode.ModeType
import io.xeros.mongo.DatabaseManager
import org.bson.Document
import java.time.Duration

object UpdateLeaderboardData {



    val highscoreData: Cache<LeaderboardData, List<LeaderboardData2>> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(15))
        .removalListener { key: LeaderboardData?, file: List<LeaderboardData2>?, cause: RemovalCause? ->
            if (cause == RemovalCause.EXPIRED) update(key!!)
        }
        .build<LeaderboardData, List<LeaderboardData2>>()


    fun update(key : LeaderboardData) {
        val entry = emptyList<LeaderboardData2>().toMutableList()
        DatabaseManager.getCollection(key.name.lowercase()).find().forEach {
            val username = it.getString("username")
            val mode = ModeType.valueOf(it.getString("mode").uppercase().replace(" ","_"))
            (it["content"] as Map<String,Document>).forEach { contentType, data ->
                entry.add(LeaderboardData2(username = username, mode = mode, content = contentType).fromDocument(data))
            }
        }
        entry.sortBy { it.timestamp }
        entry.sortBy { it.amount }
        highscoreData.put(key,entry)
    }

    fun init() {
        LeaderboardData.values().forEach {
            update(it)
        }
    }

}