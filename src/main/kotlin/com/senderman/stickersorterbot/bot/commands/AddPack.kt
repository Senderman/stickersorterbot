package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.api.methods.Methods
import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.getMyCommand
import com.senderman.stickersorterbot.bot.sendMessage
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerRepository
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

@Component("/addpack")
class AddPack(
        private val bot: CommonAbsSender,
        private val stickerRepo: StickerRepository
) : CommandExecutor {

    override val command = getMyCommand()

    override val desc = "добавить весь пак реплаем на стикер"

    override fun execute(message: Message) {
        val chatId = message.chatId
        if (!message.isReply) {
            bot.sendMessage(chatId, "Ответьте этой командой на стикер, стикерпак которого вы хотите добавить!")
            return
        }
        val reply = message.replyToMessage
        if (!reply.hasSticker()) {
            bot.sendMessage(chatId, "Ответьте этой командой на стикер, стикерпак которого вы хотите добавить!")
            return
        }

        bot.sendMessage(chatId, "Добавляю...")
        val setName = reply.sticker.setName
        val stickerPack = Methods.Stickers
                .getStickerSet(setName)
                .call(bot)

        val stickers = stickerPack.stickers.map {
            Sticker(
                    userId = message.from.id,
                    fileUniqueId = it.fileUniqueId,
                    fileId = it.fileId,
                    thumbFileId = it.thumb.fileId
            )
        }

        stickerRepo.saveAll(stickers)
        bot.sendMessage(
                chatId,
                "Стикеры из стикерпака ${stickerPack.title} успешно добавлены в список!"
        )
    }
}