package com.senderman.stickersorterbot.model

interface DatabaseService {

    /**
     * Adds sticker to tag
     * @param userId id of user
     * @param tag name of tag
     * @param sticker sticker to add
     * @return true if there wasn't sticker in this tag before, else false
     */
    fun addStickerToTag(userId: Int, tag: String, sticker: StickerEntity): Boolean

    /**
     * Adds given sticker to tag
     * @param userId id of user
     * @param tag name of tag
     * @param stickers list of stickers to add
     * Should ignore stickers which are already in given tag
     */
    fun addStickersToTag(userId: Int, tag: String, stickers:List<StickerEntity>)

    /**
     * Removes sticker from tag (or does nothing if
     */
    fun removeStickerFromTag(userId: Int, tag: String, sticker: StickerEntity)

    /**
     * Get full user data by user's id if exists
     * @param userId id of user
     * @return User object or null
     */
    fun getUser(userId: Int): User?

}