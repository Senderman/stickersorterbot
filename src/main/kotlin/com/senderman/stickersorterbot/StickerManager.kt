package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.DatabaseService
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Class to manage stickers. All operations with stickers should be done through this class,
 * not directly through DatabaseService interface.
 */
@Service
class StickerManager(
        @Qualifier("mongoDbService") // change to use another DB
        private val db: DatabaseService
) {

    /**
     * Adds sticker to UNSORTED tag
     * @param userId user's id
     * @param sticker sticker object to add
     * @return true if there was no sticker in UNSORTED tag, else false
     */
    fun addUnsortedSticker(userId: Int, sticker: StickerEntity): Boolean = db.addStickerToTag(userId, StickerTag.UNSORTED, sticker)

    /**
     * Adds all given stickers to UNSORTED taf
     */
    fun addUnsortedStickers(userId: Int, sticker: List<StickerEntity>) {
        db.addStickersToTag(userId, StickerTag.UNSORTED, sticker)
    }

    /**
     * Get all user's sticker by tags
     * @param userId user's id
     * @param tags tags of sticker's to return
     */
    fun getStickersFromTags(userId: Int, tags: List<String>): List<StickerEntity> {
        val user = db.getUser(userId) ?: return emptyList()
        return user.tags
                .filter { it.name in tags }
                .flatMap { it.stickers }
                .distinct()
    }

}