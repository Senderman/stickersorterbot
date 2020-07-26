package com.senderman.stickersorterbot.bot.commands

import com.annimon.tgbotsmodule.services.CommonAbsSender
import com.senderman.stickersorterbot.UserService
import com.senderman.stickersorterbot.bot.CommandExecutor
import com.senderman.stickersorterbot.bot.getMyCommand
import com.senderman.stickersorterbot.bot.sendMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component("/manage")
class WebSite(
        private val bot: CommonAbsSender,
        private val userService: UserService,

        @Value("\${website.url}")
        private val webSiteUrl: String
) : CommandExecutor {

    override val command = getMyCommand()

    override val desc = "Получить ссылку на сайт"

    /**
     * WARNING:
     * To use localhost, set website.url to http://127.0.0.1:8080 instead of http://localhost:8080,
     * Or this method will fail
     */
    override fun execute(message: Message) {
        val chatId = message.chatId
        val userId = message.from.id
        if (!userService.userExists(userId)) {
            bot.sendMessage(chatId, "Вы не зарегистрированы в системе! " +
                    "Для регистрации, отправьте боту в лс стикер, либо выполните команду " +
                    "/password вашНовыйПароль")
            return
        }
        val keyboard = listOf(listOf(InlineKeyboardButton().apply {
            text = "Перейти на сайт управления стикерами"
            url = "$webSiteUrl/login?telegram_id=$userId"
        }))
        val markup = InlineKeyboardMarkup().apply { this.keyboard = keyboard }
        bot.sendMessage(
                chatId,
                "Для управления стикерами перейдите по ссылке ниже", replyMarkup = markup)
    }

}