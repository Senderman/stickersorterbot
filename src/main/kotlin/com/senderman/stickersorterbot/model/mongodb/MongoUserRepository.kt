package com.senderman.stickersorterbot.model.mongodb

import com.senderman.stickersorterbot.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface MongoUserRepository : MongoRepository<User, Int>