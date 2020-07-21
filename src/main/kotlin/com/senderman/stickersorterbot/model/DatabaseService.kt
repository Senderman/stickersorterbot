package com.senderman.stickersorterbot.model

interface DatabaseService {

    /**
     * Adds sticker to tag
     * @param userId id of user
     * @param tag name of tag
     * @param sticker sticker to add
     * @return true if there wasn't sticker in this tag before, else false
     */
    fun addStickerToTag(userId: Int, tag: String, sticker: Sticker): Boolean

    /**
     * Removes sticker from tag (or does nothing if
     */
    fun removeStickerFromTag(userId: Int, tag: String, sticker: Sticker)

    /**
     * Get full user data by user's id if exists
     * @param userId id of user
     * @return User object or null
     */
    fun getUser(userId: Int): User?

}