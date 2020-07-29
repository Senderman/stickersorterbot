package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.getMyCommand
import com.senderman.stickersorterbot.bot.sendMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component("/help")
class Help(
        private val bot: CommonAbsSender,

        @Value("\${bot.username}")
        private val botName: String

) : CommandExecutor {

    override val command = getMyCommand()

    override val desc = "помощь по боту"

    override val showInHelp = false

    private val helpText: String = this::class.java.getResource("/help.txt").readText()

    override fun execute(message: Message) {
        val chatId = message.chatId
        try {
            bot.execute(
                    SendMessage(chatId, helpText.replaceFirst("%botname%", botName)).enableHtml(true)
            )
        } catch (e: TelegramApiException) {
            bot.sendMessage(chatId, "Сначала напишите боту в лс!", replyToMessageId = message.messageId)
        }
    }

}