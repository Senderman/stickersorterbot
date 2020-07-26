package com.senderman.stickersorterbot.web.entities

import com.senderman.stickersorterbot.model.StickerEntity
import java.io.File

class WebTag(
        val name: String,
        val stickers: Iterable<Iterable<WebSticker>>
)

class WebSticker(
        val sticker: StickerEntity,
        val src: String
) {

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