package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.DatabaseService
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Class to manage stickers
 */
@Service
class StickerManager(
        @Autowired
        private val db: DatabaseService
) {

    /**
     * Adds sticker to UNSORTED tag
     * @param userId user's id
     * @param sticker sticker object to add
     * @return true if there was no sticker in UNSORTED tag, else false
     */
    fun addUnsortedSticker(userId: Int, sticker: Sticker): Boolean = db.addStickerToTag(userId, StickerTag.UNSORTED, sticker)

    /**
     * Get all user's sticker by tags
     * @param userId user's id
     * @param tags tags of sticker's to return
     */
    fun getStickersFromTags(userId: Int, tags: List<String>): Set<Sticker> {
        val user = db.getUser(userId) ?: return emptySet()
        return user.tags
                .filter { it.name in tags }
                .flatMap { it.stickers }
                .toSet()

    }

}