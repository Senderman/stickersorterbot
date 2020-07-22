package com.senderman.stickersorterbot.bot.commands

import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.MessageSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

@Component("/help")
class Help(
        private val bot: MessageSender
) : CommandExecutor {

    override val command = "/help"

    override val desc = "помощь по боту"

    override val showInHelp = false

    override fun execute(message: Message) {
        bot.sendMessage(message.chatId, "Тут будет помощь")
    }

}