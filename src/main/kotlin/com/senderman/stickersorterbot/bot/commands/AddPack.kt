package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.api.methods.Methods
import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.StickerManager
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.MessageSender
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

@Component("/addpack")
class AddPack<T>(
        @Autowired
        private val bot: T,

        @Autowired
        private val stickerManager: StickerManager
) : CommandExecutor
        where T : MessageSender,
              T : CommonAbsSender {

    override val command = "/addpack"

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
        val setName = reply.sticker.setName
        bot.sendMessage(chatId, "Добавляю...")
        val stickerPack = Methods.Stickers
                .getStickerSet(setName)
                .call(bot)
        val stickers = stickerPack.stickers.map { StickerEntity(it.fileUniqueId, it.fileId) }
        stickerManager.addUnsortedStickers(message.from.id, stickers)

        bot.sendMessage(
                chatId,
                "Стикеры из стикерпака ${stickerPack.title} успешно добавлены в тег ${StickerTag.UNSORTED}!"
        )
    }
}