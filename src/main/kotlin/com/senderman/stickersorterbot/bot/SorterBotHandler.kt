package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.BotHandler
import com.annimon.tgbotsmodule.api.methods.Methods
import com.senderman.stickersorterbot.StickerService
import com.senderman.stickersorterbot.model.StickerEntity
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker
import java.util.*

@Component
class SorterBotHandler(

        @Value("\${bot.token}")
        private var botToken: String,

        @Value("\${bot.username}")
        private var botUsername: String,

        private val stickerManager: StickerService,
        private val commands: CommandExtractor
) : BotHandler() {

    override fun onUpdate(update: Update): BotApiMethod<*>? {

        if (update.hasInlineQuery()) {
            handleInlineQuery(update.inlineQuery)
            return null
        }

        val message = update.message ?: return null

        if (message.isUserMessage && message.hasSticker()) {
            handleSticker(message)
            return null
        }

        val text = message.text ?: return null

        val command = text
                .split("\\s+".toRegex(), 2)[0]
                .toLowerCase(Locale.ENGLISH)
                .replace("@$botUsername", "")
        if ("@" in command) return null

        commands.findExecutor(command)?.execute(message)

        return null
    }

    private fun handleInlineQuery(query: InlineQuery) {
        val tagsString = query.query.trim()

        if (tagsString.isBlank()) {
            answerInlineQuery(
                    query.id,
                    listOf(InlineQueryResultArticle().apply {
                        id = "error"
                        title = "Укажите теги через пробел!"
                        inputMessageContent = InputTextMessageContent().apply {
                            messageText = "Укажите теги через пробел: @$botUsername котики аниме тортики"
                        }
                    })
            )
            return
        }

        val tags = tagsString.split(Regex("\\s+"))
        val stickers = stickerManager.getStickersFromTags(query.from.id, tags).take(50) // telegram limit
        val result = stickers.map {
            InlineQueryResultCachedSticker().apply {
                id = it.fileUniqueId
                stickerFileId = it.fileId
            }
        }
        answerInlineQuery(query.id, result)

    }

    private fun handleSticker(message: Message) {
        val chatId = message.chatId
        val sticker = message.sticker
        val stickerEntity = StickerEntity(sticker.fileUniqueId, sticker.fileId, sticker.thumb.fileId)
        if (stickerManager.addUnsortedStickers(message.from.id, listOf(stickerEntity)))
            sendMessage(chatId, "Стикер добавлен в ${StickerTag.UNSORTED} тег!")
        else
            sendMessage(chatId, "Этот стикер уже есть в неотсортированных!")
    }

    private fun answerInlineQuery(id: String, results: List<InlineQueryResult>) {
        Methods.answerInlineQuery()
                .setInlineQueryId(id)
                .setResults(results)
                .setPersonal(true)
                .setCacheTime(1)
                .call(this)
    }

    override fun getBotUsername(): String = botUsername

    override fun getBotToken(): String = botToken
}