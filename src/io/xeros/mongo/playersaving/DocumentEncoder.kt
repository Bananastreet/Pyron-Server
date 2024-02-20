package io.xeros.mongo.playersaving

import io.xeros.model.entity.player.Player
import org.bson.Document


interface DocumentEncoder {

    val name: String

    fun asDocument(player: Player) : Document
}
