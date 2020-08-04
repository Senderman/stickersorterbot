package com.senderman.stickersorterbot.web.controller

import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class MainAjaxController(
        private val stickerRepo: StickerRepository
) {

    class SetTagAjaxBody(
            val tags: String,
            val fileUniqueId: String
    )

    @PostMapping("/setTags")
    fun setTags(
            @RequestBody ajax: SetTagAjaxBody,
            principal: Principal
    ) {
        val userId = principal.name.toInt()
        val tagsSet = ajax.tags.toLowerCase().split(Regex("\\s+")).toMutableSet()
        stickerRepo.findById(Sticker.generateId(userId, ajax.fileUniqueId)).ifPresent {
            it.tags = tagsSet
            stickerRepo.save(it)
        }
    }

    class DeleteStickerAjaxBody(
            val fileUniqueId: String
    )

    @PostMapping("/deleteSticker")
    fun deleteSticker(
            @RequestBody ajax: DeleteStickerAjaxBody,
            principal: Principal
    ) {
        val userId = principal.name.toInt()
        stickerRepo.deleteById(Sticker.generateId(userId, ajax.fileUniqueId))
    }
}