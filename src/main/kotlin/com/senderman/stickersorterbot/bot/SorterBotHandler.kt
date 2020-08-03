package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.BotHandler
import com.annimon.tgbotsmodule.api.methods.Methods
import com.senderman.stickersorterbot.model.Sticker
import com.senderman.stickersorterbot.model.StickerRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker
import java.util.*

@Component
class SorterBotHandler(

        @Value("\${bot.token}")
        private var botToken: String,

        @Value("\${bot.username}")
        private var botUsername: String,

        private val stickerRepo: StickerRepository,
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

        val stickers: List<Sticker> = (
                if (tagsString.isBlank())
                    stickerRepo.findAllByUserId(query.from.id)
                else
                    stickerRepo.findByUserIdAndTagsIn(query.from.id, tagsString.split(Regex("\\+s")))
                )
                .take(50)

        val result = stickers.map {
            InlineQueryResultCachedSticker()
                    .setId(it.fileUniqueId)
                    .setStickerFileId(it.fileId)
        }
        answerInlineQuery(query.id, result)

    }

    private fun handleSticker(message: Message) {
        val chatId = message.chatId
        val userId = message.from.id
        val sticker = message.sticker
        val stickerEntity = Sticker(
                userId = userId,
                fileUniqueId = sticker.fileUniqueId,
                fileId = sticker.fileId,
                thumbFileId = sticker.thumb.fileId
        )
        if (stickerRepo.existsById(Sticker.generateId(userId, sticker.fileUniqueId)))
            sendMessage(chatId, "Этот стикер уже есть в вашем списке!")
        else {
            stickerRepo.save(stickerEntity)
            sendMessage(chatId, "Стикер добавлен в ваш список!")
        }

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