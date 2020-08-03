package com.senderman.stickersorterbot.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("user")
class User(
        @Id val userId: Int,
        var password: String


)

@TypeAlias("sticker")
class Sticker(
        val userId: Int,
        val fileUniqueId: String,
        val fileId: String = "",
        val thumbFileId: String = ""
) {

    @Id
    var id: String = generateId(userId, fileUniqueId)

    var tags: MutableSet<String> = HashSet()

    // equals by id
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sticker

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {

        fun generateId(userId: Int, fileUniqueId: String): String = "$fileUniqueId $userId"

    }
}