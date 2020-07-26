package com.senderman.stickersorterbot.web.config

import com.senderman.stickersorterbot.model.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
        private val userRepo: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByIdOrNull(username.toInt()) ?: throw UsernameNotFoundException(
                "No such id"
        )
        val authorities = listOf(SimpleGrantedAuthority("user"))
        return User(username, user.password, authorities)
    }
}