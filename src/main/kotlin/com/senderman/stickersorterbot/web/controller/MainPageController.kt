package com.senderman.stickersorterbot.web.controller

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.StickerService
import com.senderman.stickersorterbot.bot.getFirstNameById
import com.senderman.stickersorterbot.groupByLimit
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import com.senderman.stickersorterbot.web.CachingStickerFileProvider
import com.senderman.stickersorterbot.web.entities.WebSticker
import com.senderman.stickersorterbot.web.entities.WebTag
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal

@Controller
@RequestMapping("/", "main")
class MainPageController(
        @Value("\${website.cacheDirectory}")
        private val cacheDir: String,

        private val telegram: CommonAbsSender,
        private val stickerService: StickerService,
        private val stickerCache: CachingStickerFileProvider
) {

    @GetMapping
    fun showMainPage(
            @RequestParam("searchBy", required = false, defaultValue = "") searchBy: String,
            principal: Principal?,
            model: Model
    ): String {
        if (principal == null) return "redirect:login"
        val userId = principal.name.toInt()
        val stickerTags = stickerService.getAllTagsWithStickers(userId)

        // filter by search query
        if (!searchBy.isBlank()) {
            val tagsToFind = searchBy.split(Regex("\\s+"))
            stickerTags.removeAll { it.name !in tagsToFind }
        }

        return generateWebContent(userId, model, stickerTags)
    }

    @PostMapping("changeTags")
    fun changeTags(
            @RequestParam("action", required = false) action: String?, // will be "copy" or "move" or null
            @RequestParam("searchBy", required = false, defaultValue = "") searchBy: String,
            @RequestParam("tags", required = false, defaultValue = "") tags: String,
            stickerId: String,
            fileId: String,
            tagName: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        if (action == null) return showMainPage(searchBy, principal, model)

        /*if (!tags.matches(Regex("(\\p{LD}\\s*)+"))) throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Укажите стикеры через пробел!"
        )*/

        val stickerEntity = StickerEntity(stickerId, fileId)
        if (action == "move")
            stickerService.removeStickerFromTag(userId, tagName, stickerEntity)

        stickerService.addStickersToTags(userId, tags.split(Regex("\\s+")), listOf(stickerEntity))
        return showMainPage(searchBy, principal, model)
    }

    @PostMapping("deleteFromTag")
    fun deleteFromTag(
            @RequestParam("searchBy", required = false, defaultValue = "") searchBy: String,
            stickerId: String,
            fileId: String,
            tagName: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        val stickerEntity = StickerEntity(stickerId, fileId)
        stickerService.removeStickerFromTag(userId, tagName, stickerEntity)
        return showMainPage(searchBy, principal, model)
    }

    // generate web page's content from user's stickers or from given source, if present
    private fun generateWebContent(userId: Int, model: Model, source: MutableSet<StickerTag>? = null): String {

        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)

        val input = source ?: stickerService.getAllTagsWithStickers(userId)
        val output = mutableListOf<WebTag>()
        for (tag in input) {
            val outputStickers = mutableSetOf<WebSticker>()
            for (sticker in tag.stickers) {
                val stickerFile = stickerCache.retrieveSticker(sticker)
                val webSticker = WebSticker(sticker, "/$cacheDir/${stickerFile.name}")
                outputStickers.add(webSticker)
            }
            output.add(WebTag(tag.name, outputStickers.groupByLimit(5)))
        }

        model.addAttribute("content", output)
        return "main"
    }

}