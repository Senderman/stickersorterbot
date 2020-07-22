package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.stereotype.Service

/**
 * Class to manage stickers. All operations with stickers should be done through this class,
 * not directly through DatabaseService interface.
 */
@Service
class StickerService(
        private val userService: UserService
) {

    /**
     * Adds sticker to UNSORTED tag
     * @param userId user's id
     * @param sticker sticker object to add
     * @return true if there was no sticker in UNSORTED tag, else false
     */
    fun addUnsortedSticker(userId: Int, sticker: StickerEntity): Boolean =
            addStickerToTag(userId, StickerTag.UNSORTED, sticker)

    /**
     * Adds all given stickers to UNSORTED tag, skipping existing
     */
    fun addUnsortedStickers(userId: Int, sticker: List<StickerEntity>) {
        addStickersToTag(userId, StickerTag.UNSORTED, sticker)
    }

    /**
     * Adds sticker to tag
     * @param userId user's id
     * @param tag tag name
     * @param sticker sticker object to add
     * @return true if there was no sticker in given tag, else false
     */
    fun addStickerToTag(userId: Int, tag: String, sticker: StickerEntity): Boolean {
        val user = userService.getUser(userId)
        val stickers = user.getTag(tag).stickers
        if (sticker in stickers) return false
        stickers.add(sticker)
        userService.updateUser(user)
        return true
    }

    /**
     * Add stickers to tag. If sticker already in tag, skip the sticker
     * @param userId userId
     * @param tag tag name
     * @param stickers list of stickers
     */
    fun addStickersToTag(userId: Int, tag: String, stickers: List<StickerEntity>) {
        val user = userService.getUser(userId)
        val userStickers = user.getTag(tag).stickers
        for (sticker in stickers) {
            if (sticker !in userStickers)
                userStickers.add(sticker)
        }
        userService.updateUser(user)
    }

    /**
     * Get all user's sticker by tags
     * @param userId user's id
     * @param tags tags of sticker's to return
     */
    fun getStickersFromTags(userId: Int, tags: List<String>): List<StickerEntity> {
        val user = userService.getUser(userId)
        return user.tags
                .filter { it.name in tags }
                .flatMap { it.stickers }
                .distinct()
    }

    /**
     * Remove sticker from tag. Deletes tag if it will be empty
     * @param userId id of user
     * @param tag name of tag
     * @param sticker sticker to delete
     * @return true if sticker deleted, false if sticker was not in tag
     */
    fun removeStickerFromTag(userId: Int, tag: String, sticker: StickerEntity): Boolean {
        val user = userService.getUser(userId)
        val userTag = user.getTag(tag)
        val stickers = userTag.stickers
        val success = stickers.remove(sticker)
        if (!success) return false
        if (stickers.isEmpty()) user.tags.remove(userTag)
        userService.updateUser(user)
        return success
    }
}