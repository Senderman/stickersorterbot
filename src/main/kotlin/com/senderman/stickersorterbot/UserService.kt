package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.StickerTag
import com.senderman.stickersorterbot.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
        private val repo: CrudRepository<User, Int>,
        private val passwordEncoder: PasswordEncoder
) {

    /**
     * Creates a new user with no stickers, random UUID password, and empty StickerTag.UNSORTED tag
     * @param userId id of new user
     * @return new User object
     */
    fun newUser(userId: Int): User {
        val passwordUuid = UUID.randomUUID().toString()
        val encodedPassword = passwordEncoder.encode(passwordUuid)
        val tag = StickerTag(StickerTag.UNSORTED, mutableSetOf())
        val user = User(userId, encodedPassword, mutableSetOf(tag))
        return repo.save(user)
    }

    /**
     * Get user data. Creates new user if not exists
     */
    fun getUser(userId: Int): User = repo.findByIdOrNull(userId) ?: newUser(userId)

    /**
     * Update user data or create a new one if not exists
     * @param user User object
     * @return updated user object
     */
    fun updateUser(user: User): User = repo.save(user)

    /**
     * Changes user's password. Creates a new user if not exists
     * @param userId id of user
     * @param password raw password
     * @return updated user
     */
    fun changePassword(userId: Int, password: String): User {
        val user = repo.findByIdOrNull(userId) ?: newUser(userId)
        user.password = passwordEncoder.encode(password)
        return repo.save(user)
    }

}