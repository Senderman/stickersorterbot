package com.senderman.stickersorterbot.web.controller

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.bot.getFirstNameById
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerRepository
import com.senderman.stickersorterbot.web.CachingStickerFileProvider
import com.senderman.stickersorterbot.web.entities.WebSticker
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal

@Controller
class MainPageController(
        @Value("\${website.cacheDirectory}")
        private val cacheDir: String,

        private val telegram: CommonAbsSender,
        private val stickerRepo: StickerRepository,
        private val stickerCache: CachingStickerFileProvider
) {

    @GetMapping("/")
    fun showMainPage(
            @RequestParam("search", required = false, defaultValue = "") search: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        val stickers = stickerRepo.findAllByUserId(userId)

        // filter by search query
        /* if (!search.isBlank()) {
             val tagsToFind = search.split(Regex("\\s+"))
             stickers.removeAll { it.name !in tagsToFind }
         }*/

        return generateWebContent(userId, model, stickers)
    }

    @PostMapping("/deleteSticker")
    fun deleteSticker(
            @RequestParam("search", required = false, defaultValue = "") search: String,
            fileUniqueId: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        stickerRepo.deleteById(Sticker.generateId(userId, fileUniqueId))
        return showMainPage(search, principal, model)
    }

    // generate web page's content from user's stickers or from given source, if present
    private fun generateWebContent(userId: Int, model: Model, source: MutableSet<Sticker>): String {

        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)

        val webStickers: Iterable<WebSticker> = mapToWebStickers(source)

        model.addAttribute("stickers", webStickers)
        return "main"
    }

    private fun mapToWebStickers(stickers: Iterable<Sticker>): Iterable<WebSticker> =
            stickers.map {
                val stickerFile = stickerCache.retrieveSticker(it)
                WebSticker(it, "/$cacheDir/${stickerFile.name}")
            }
}