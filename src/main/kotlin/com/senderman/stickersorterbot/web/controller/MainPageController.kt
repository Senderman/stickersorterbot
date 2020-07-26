package com.senderman.stickersorterbot.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/", "main")
class MainPageController {

    @GetMapping
    fun showMainPage(principal: Principal, model: Model) {

    }

}