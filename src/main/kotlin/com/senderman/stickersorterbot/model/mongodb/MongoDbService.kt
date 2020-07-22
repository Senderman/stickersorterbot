package com.senderman.stickersorterbot.model.mongodb

import com.senderman.stickersorterbot.model.DatabaseService
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MongoDbService(
        private val userRepo: MongoUserRepository
) : DatabaseService {

    private fun createNewUser(userId: Int): User = userRepo.save(User.newUser(userId))


    override fun addStickerToTag(userId: Int, tag: String, sticker: StickerEntity): Boolean {
        val user = userRepo.findByIdOrNull(userId) ?: createNewUser(userId)
        val stickers = user.getTag(tag).stickers
        if (sticker in stickers) return false
        stickers.add(sticker)
        userRepo.save(user)
        return true
    }

    override fun addStickersToTag(userId: Int, tag: String, stickers: List<StickerEntity>) {
        val user = userRepo.findByIdOrNull(userId) ?: createNewUser(userId)
        val userStickers = user.getTag(tag).stickers
        for (sticker in stickers) {
            if (sticker !in userStickers)
                userStickers.add(sticker)
        }
        userRepo.save(user)
    }

    override fun removeStickerFromTag(userId: Int, tag: String, sticker: StickerEntity) {
        val user = userRepo.findByIdOrNull(userId) ?: return
        val userTag = user.getTag(tag)
        val stickers = userTag.stickers
        stickers.remove(sticker)
        if (stickers.isEmpty()) user.tags.remove(userTag)
        userRepo.save(user)
    }

    override fun getUser(userId: Int): User? = userRepo.findByIdOrNull(userId)
}