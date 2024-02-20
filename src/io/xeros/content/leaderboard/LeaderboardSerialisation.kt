package io.xeros.content.leaderboard

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import io.xeros.content.leaderboard.impl.Boss
import io.xeros.model.entity.player.Player
import io.xeros.model.entity.player.mode.ModeType
import io.xeros.mongo.DatabaseManager
import io.xeros.mongo.playersaving.DocumentDecoder
import io.xeros.mongo.playersaving.DocumentEncoder
import org.bson.Document
import org.bson.conversions.Bson


object LeaderboardSerialisation {

    /**
     * Creates a document for the player and saves it to the database.
     */
    fun updateCount(player : Player,leaderboardType : LeaderboardData,saveData: LeaderboardSaveData) {
        val doc = Document()

        doc.append("username", player.displayName.lowercase())
        doc.append("mode", player.mode.type.name.lowercase())
        doc.append("content", emptyMap<String,Document>())

        val check1: Bson = eq("username", player.displayName.lowercase())

        if (!playerExists(check1,leaderboardType.name.lowercase())) {
            DatabaseManager.getCollection(leaderboardType.name.lowercase()).insertOne(doc)
        }

        DatabaseManager.getCollection(leaderboardType.name.lowercase()).updateOne(Filters.and(check1), set("content.${saveData.name}", saveData.asDocument(player)))

    }

    fun updateCount(player : Player,leaderboardType : LeaderboardData,amount : Int, type : ContentType) {
        updateCount(player,leaderboardType,LeaderboardSaveData(amount,type))
    }

    /**
     * Check if a player exists in the database
     */
    private fun playerExists(bson1: Bson, collection : String): Boolean {
        return DatabaseManager.getCollection(collection)
            .find(Filters.and(bson1))
        .toList().isNotEmpty()
    }

}

data class LeaderboardSaveData(
    val amount : Int,
    val type : ContentType,
) : DocumentEncoder, DocumentDecoder {

    override val name : String = type.toString().lowercase()

    override fun asDocument(player: Player) = Document()
        .append("timestamp", System.currentTimeMillis())
        .append("amount", amount)

    override fun fromDocument(player: Player, doc: Document) {
        TODO("Not yet implemented")
    }
}

data class LeaderboardData2(var timestamp : Long = 0, var amount: Int = 0, val username : String = "", val mode : ModeType = ModeType.STANDARD, val content : String = "") {
    fun fromDocument(doc: Document) : LeaderboardData2 {
        timestamp = doc.getLong("timestamp")
        amount = doc.getInteger("amount")
        return this
    }
}

