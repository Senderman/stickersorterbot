package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.UserService
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

@Component("/password")
class SetPassword(
        private val bot: CommonAbsSender,
        private val userManager: UserService
) : CommandExecutor {

    override val command = "/password"

    override val desc = "изменить пароль. /password p@ssw0rd"

    override fun execute(message: Message) {
        val chatId = message.chatId
        if (!message.isUserMessage) {
            bot.sendMessage(chatId, "Команду можно использовать только в лс!")
            return
        }

        val userId = message.from.id
        val args = message.text.split(Regex("\\s+"), 2)
        if (args.size < 2) {
            bot.sendMessage(chatId, "Использование: $command p@ssw0rd")
            return
        }
        val password = args[1]
        userManager.changePassword(userId, password)
        bot.sendMessage(chatId, "Ваш пароль успешно изменен!\n" +
                "Ваш Telegram ID: $userId\n" +
                "Ваш пароль: $password")
    }

}