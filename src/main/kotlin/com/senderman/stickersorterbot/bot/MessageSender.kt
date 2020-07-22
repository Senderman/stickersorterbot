package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.api.methods.send.SendMessageMethod
import org.telegram.telegrambots.meta.api.objects.Message

interface MessageSender {

    fun sendMessage(chatId: Long, text: String, replyToMessageId:Int?=null):Message

    fun sendMessage(sm:SendMessageMethod):Message

}