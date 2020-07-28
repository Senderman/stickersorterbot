package com.senderman.stickersorterbot.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("user")
class User(
        @Id val userId: Int,
        var password: String,
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
}

class StickerTag(
        @Id val name: String,
        val stickers: MutableSet<StickerEntity>
) {


    companion object {
        const val UNSORTED = "несорт"
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

class StickerEntity(
        @Id val fileUniqueId: String,
        val fileId: String,
        val thumbFileId: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StickerEntity

        if (fileUniqueId != other.fileUniqueId) return false

        return true
    }

    override fun hashCode(): Int {
        return fileUniqueId.hashCode()
    }
}