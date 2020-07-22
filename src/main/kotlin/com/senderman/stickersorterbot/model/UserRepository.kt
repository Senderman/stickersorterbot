package com.senderman.stickersorterbot.model

import org.springframework.data.mongodb.repository.MongoRepository

/**
 * To change database type for the whole application,
 * Include proper spring data dependency build.gradle.kts
 * And inherit this interface from proper interface. That's all =)
 */
interface UserRepository : MongoRepository<User, Int>