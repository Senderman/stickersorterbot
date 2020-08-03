package com.senderman.stickersorterbot.model

import org.springframework.data.mongodb.repository.MongoRepository

/**
 * To change database type for the whole application,
 * Include proper spring data dependency in build.gradle.kts
 * And inherit these interfaces from proper spring data's interfaces. That's all =)
 */

interface UserRepository : MongoRepository<User, Int>

interface StickerRepository : MongoRepository<Sticker, String> {

    fun findAllByUserId(userId: Int): MutableSet<Sticker>

    fun findByUserIdAndTagsIn(userId: Int, tags: Collection<String>): MutableSet<Sticker>

}