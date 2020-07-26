package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.getMyCommand
import com.senderman.stickersorterbot.bot.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.reflect.full.findAnnotation

@Component("/help")
class Help(
        private val bot: CommonAbsSender
) : CommandExecutor {

    override val command = getMyCommand()

    override val desc = "помощь по боту"

    override val showInHelp = false

    override fun execute(message: Message) {
        bot.sendMessage(message.chatId, "Тут будет помощь")
    }

}