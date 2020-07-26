package com.senderman.stickersorterbot.web.controller

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.bot.getFirstNameById
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/", "main")
class MainPageController(
        private val telegram:CommonAbsSender
) {

    @GetMapping
    fun showMainPage(principal: Principal, model: Model):String {
        val userId = principal.name.toInt()
        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)
        return "main"
    }

}