package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.api.methods.Methods
import com.annimon.tgbotsmodule.services.CommonAbsSender
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

fun CommonAbsSender.sendMessage(
        chatId: Long,
        text: String,
        replyToMessageId: Int? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
        enableWebPagePreview: Boolean = false
): Message = Methods.sendMessage()
        .setChatId(chatId)
        .setText(text)
        .setReplyToMessageId(replyToMessageId)
        .setReplyMarkup(replyMarkup)
        .enableHtml()
        .setWebPagePreviewEnabled(enableWebPagePreview)
        .call(this)