package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.BotHandler
import com.annimon.tgbotsmodule.api.methods.Methods
import com.senderman.stickersorterbot.StickerManager
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerTag
import org.springframework.beans.factory.annotation.Autowired
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

@Component
class SorterBotHandler(

        @Value("\${bot.token}")
        private var botToken: String,

        @Value("\${bot.username}")
        private var botUsername: String,

        @Autowired
        private val stickerManager: StickerManager
) : BotHandler() {


    override fun onUpdate(update: Update): BotApiMethod<*>? {

        if (update.hasInlineQuery()) {
            handleInlineQuery(update.inlineQuery)
            return null
        }

        val message = update.message ?: return null

        val chatId = message.chatId

        if (message.isUserMessage && message.hasSticker()) {
            handleSticker(message)
            return null
        }

        return null
    }

    fun handleInlineQuery(query: InlineQuery) {
        val tagsString = query.query.trim()

        if (!tagsString.matches(Regex("(\\p{LD}\\s*)+"))) {
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

        val tags = if (!tagsString.isBlank()) listOf(StickerTag.UNSORTED) else tagsString.split(Regex("\\s+"))
        val stickers = stickerManager.getStickersFromTags(query.from.id, tags)
        val result = stickers.map {
            InlineQueryResultCachedSticker().apply {
                id = it.fileUniqueId
                stickerFileId = it.fileId
            }
        }
        answerInlineQuery(query.id, result)

    }

    fun handleSticker(message: Message) {
        val chatId = message.chatId
        val sticker = message.sticker
        val stickerEntity = Sticker(sticker.fileUniqueId, sticker.fileId)

        if (stickerManager.addUnsortedSticker(message.from.id, stickerEntity))
            sendMessage(chatId, "Стикер добавлен в ${StickerTag.UNSORTED} тег!")
        else
            sendMessage(chatId, "Этот стикер уже есть в неотсортированных!")
    }

    fun answerInlineQuery(id: String, results: List<InlineQueryResult>) {
        Methods.answerInlineQuery()
                .setInlineQueryId(id)
                .setResults(results)
                .setPersonal(true)
                .call(this)
    }

    fun sendMessage(chatId: Long, text: String): Message = Methods.sendMessage()
            .setChatId(chatId)
            .setText(text)
            .enableHtml()
            .call(this)

    override fun getBotUsername(): String = botUsername

    override fun getBotToken(): String = botToken
}