package com.senderman.stickersorterbot.web.entities

import com.senderman.stickersorterbot.model.Sticker

class WebSticker(
        val sticker: Sticker,
        val src: String
) {

    val tags: String = sticker.tags.joinToString(" ")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebSticker

        if (sticker != other.sticker) return false

        return true
    }

    override fun hashCode(): Int {
        return sticker.hashCode()
    }

}