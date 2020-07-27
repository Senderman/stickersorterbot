package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.stereotype.Service

/**
 * Class to manage stickers. All operations with stickers should be done through this class,
 * not directly through DatabaseService interface or UserService class.
 */
@Service
class StickerService(
        private val userService: UserService
) {

    /**
     * Get all user's stickers sorted by tags
     * @param userId user's id
     * @return set of StickerTag objects
     */
    fun getAllTagsWithStickers(userId: Int): MutableSet<StickerTag> = userService.getUser(userId).tags

    /**
     * Get all user's sticker by tags
     * @param userId user's id
     * @param tags tags of sticker's to return
     */
    fun getStickersFromTags(userId: Int, tags: List<String>): MutableSet<StickerEntity> =
            getAllTagsWithStickers(userId)
                    .filter { it.name in tags }
                    .flatMap { it.stickers }
                    .toMutableSet()

    /**
     * Adds all given stickers to UNSORTED tag, skipping existing
     * @param userId user's id
     * @param stickers sticker object to add
     * @return true if at least one sticker was added
     */
    fun addUnsortedStickers(userId: Int, stickers: List<StickerEntity>): Boolean =
            addStickersToTags(userId, listOf(StickerTag.UNSORTED), stickers)

    /**
     * Add stickers to tags. If sticker already in tag, skip the sticker
     * @param userId userId
     * @param tags list of tag names
     * @param stickers list of stickers
     * @return true if at least one sticker was added, else false
     */
    fun addStickersToTags(userId: Int, tags: List<String>, stickers: List<StickerEntity>): Boolean {
        val user = userService.getUser(userId)
        var result = false
        for (tag in tags) {
            if (user.getTag(tag).stickers.addAll(stickers)) result = true
        }
        userService.updateUser(user)
        return result
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