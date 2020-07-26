package com.senderman.stickersorterbot.web.controller

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.StickerService
import com.senderman.stickersorterbot.UserService
import com.senderman.stickersorterbot.bot.getFirstNameById
import com.senderman.stickersorterbot.groupByLimit
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import com.senderman.stickersorterbot.web.CachingStickerProvider
import com.senderman.stickersorterbot.web.entities.WebSticker
import com.senderman.stickersorterbot.web.entities.WebTag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@Controller
@RequestMapping("/", "main")
class MainPageController(
        @Value("\${website.cacheDirectory}")
        private val cacheDir: String,

        private val telegram: CommonAbsSender,
        private val stickerService: StickerService,
        private val userService: UserService,
        private val stickerCache: CachingStickerProvider
) {

    @GetMapping
    fun showMainPage(
            @RequestParam("searchBy", required = false, defaultValue = "") searchByTags: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)

        val stickerTags = stickerService.getAllTagsWithStickers(userId)
        if (!searchByTags.isBlank()) {
            val tagsToFind = searchByTags.split(Regex("\\s+"))
            stickerTags.removeAll { it.name !in tagsToFind }
        }
        val webTags = generateWebTags(stickerTags)
        model.addAttribute("webtags", webTags)
        return "main"
    }

    @PostMapping("changeTags")
    fun changeTags(
            stickerId: String,
            fileId: String,
            tagName: String,
            action: String,
            tags: String,
            principal: Principal,
            model: Model
    ): String {
        val userId = principal.name.toInt()
        val user = userService.getUser(userId)
        if (!tags.matches(Regex("(\\p{LD}\\s*)+"))) throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Укажите стикеры через пробел!"
        )

        val stickerEntity = StickerEntity(stickerId, fileId)
        if (action == "move")
            user.getTag(tagName).stickers.remove(stickerEntity)
        for (tag in tags.split(Regex("\\s+")))
            user.getTag(tag).stickers.add(stickerEntity)
        userService.updateUser(user)

        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)

        val webTags = generateWebTags(user.tags)
        model.addAttribute("webtags", webTags)
        return "main"
    }

    @PostMapping("deleteFromTag")
    fun deleteFromTag(
            stickerId: String,
            fileId: String,
            tagName: String,
            principal: Principal,
            model: Model
    ):String{
        val userId = principal.name.toInt()
        val username = telegram.getFirstNameById(userId)
        model.addAttribute("username", username)

        val user = userService.getUser(userId)
        val stickerEntity = StickerEntity(stickerId, fileId)
        user.getTag(tagName).stickers.remove(stickerEntity)
        userService.updateUser(user)

        val webTags = generateWebTags(user.tags)
        model.addAttribute("webtags", webTags)
        return "main"
    }

    private fun generateWebTags(stickerTags: MutableSet<StickerTag>): Iterable<WebTag> {
        val output = mutableListOf<WebTag>()
        for (tag in stickerTags) {
            val outputStickers = mutableSetOf<WebSticker>()
            for (sticker in tag.stickers) {
                val stickerFile = stickerCache.retrieveSticker(sticker.fileUniqueId, sticker.fileId)
                val webSticker = WebSticker(sticker, "/$cacheDir/${stickerFile.name}")
                outputStickers.add(webSticker)
            }
            output.add(WebTag(tag.name, outputStickers.groupByLimit(5)))
        }
        return output
    }

}