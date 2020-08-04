package com.senderman.stickersorterbot

import com.senderman.stickersorterbot.model.User
import com.senderman.stickersorterbot.model.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
        private val repo: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    /**
     * Creates a new user with random UUID password
     * @param userId id of new user
     * @return new User object
     */
    fun newUser(userId: Int): User {
        val passwordUuid = UUID.randomUUID().toString()
        val encodedPassword = passwordEncoder.encode(passwordUuid)
        val user = User(userId, encodedPassword)
        return repo.save(user)
    }

    /**
     * Check if user with given userId exists
     * @param userId id of user
     * @return true if exists, else false
     */
    fun userExists(userId: Int): Boolean = repo.existsById(userId)

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

    /**
     * Delete all user data from repo
     * @param userId id of user
     */
    fun deleteUser(userId: Int): Unit = repo.deleteById(userId)

}