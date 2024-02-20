package io.xeros.mongo.playersaving

import io.xeros.model.entity.player.Player
import org.bson.Document


interface DocumentDecoder {

    val name: String

    fun fromDocument(player: Player, doc: Document)
}
