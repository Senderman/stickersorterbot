package com.senderman.stickersorterbot.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("user")
class User(
        @Id val userId: Int,
        val tags: MutableSet<StickerTag>
) {

    /**
     * Return StickerTag object by given name
     * If there's no tag with given name, creates and returns it
     *
     * @param name name of tag
     * @return StickerTag Object
     */
    fun getTag(name: String): StickerTag {
        val tag = tags.find { it.name == name }
        return if (tag != null)
            tag
        else {
            val newTag = StickerTag(name, mutableSetOf())
            tags.add(newTag)
            newTag
        }
    }

    companion object {

        /**
         * Creates a new user with no stickers and empty StickerTag.UNSORTED tag
         * @param userId id of new user
         * @return new User object
         */
        fun newUser(userId: Int): User {
            val tag = StickerTag(StickerTag.UNSORTED, mutableSetOf())
            return User(userId, mutableSetOf(tag))
        }
    }
}

class StickerTag(
        @Id val name: String,
        val stickers: MutableSet<Sticker>
) {


    companion object {
        const val UNSORTED = "unsorted"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StickerTag

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Sticker(
        @Id val fileUniqueId: String,
        val fileId: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sticker

        if (fileUniqueId != other.fileUniqueId) return false

        return true
    }

    override fun hashCode(): Int {
        return fileUniqueId.hashCode()
    }
}