package com.senderman.stickersorterbot.web.entities

import com.senderman.stickersorterbot.model.StickerEntity
import java.io.File

class WebSticker(
        val sticker: StickerEntity,
        val tags: String, // tags separated by space
        val src: File
)