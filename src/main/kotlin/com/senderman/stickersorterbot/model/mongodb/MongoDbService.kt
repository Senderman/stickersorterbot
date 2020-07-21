package com.senderman.stickersorterbot.model.mongodb

import com.senderman.stickersorterbot.model.DatabaseService
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MongoDbService(

        @Autowired
        private val userRepo: MongoUserRepository

) : DatabaseService {

    private fun createNewUser(userId: Int): User = userRepo.save(User.newUser(userId))


    override fun addStickerToTag(userId: Int, tag: String, sticker: Sticker): Boolean {
        val user = userRepo.findByIdOrNull(userId) ?: createNewUser(userId)
        val stickers = user.getTag(tag).stickers
        if (sticker.fileUniqueId in stickers.map { it.fileUniqueId }) return false
        stickers.add(sticker)
        userRepo.save(user)
        return true
    }

    override fun removeStickerFromTag(userId: Int, tag: String, sticker: Sticker) {
        val user = userRepo.findByIdOrNull(userId) ?: return
        val stickers = user.getTag(tag).stickers
        stickers.removeIf { it.fileUniqueId == sticker.fileUniqueId }
        if (stickers.isEmpty()) user.tags.removeIf { it.name == tag }
        userRepo.save(user)
    }

    override fun getUser(userId: Int): User? = userRepo.findByIdOrNull(userId)
}