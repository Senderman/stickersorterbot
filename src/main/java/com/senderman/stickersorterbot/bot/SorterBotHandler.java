package com.senderman.stickersorterbot.bot;

import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.api.methods.send.SendMessageMethod;
import com.senderman.stickersorterbot.bot.command.CommandExtractor;
import com.senderman.stickersorterbot.model.Sticker;
import com.senderman.stickersorterbot.repository.StickerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SorterBotHandler extends BotHandler implements
        MessageSender {

    private final String botToken;
    private final String botUsername;
    private final StickerRepository stickerRepository;
    private final CommandExtractor commandExtractor;

    public SorterBotHandler(
            @Value("${bot.token}") String botToken,
            @Value("${bot.username}") String botUsername,
            StickerRepository stickerRepository,
            CommandExtractor commandExtractor) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.stickerRepository = stickerRepository;
        this.commandExtractor = commandExtractor;
    }

    @Override
    protected BotApiMethod<?> onUpdate(@NotNull Update update) {

        if (update.hasInlineQuery()) {
            handleInlineQuery(update.getInlineQuery());
            return null;
        }

        if (!update.hasMessage()) return null;

        final var message = update.getMessage();
        if (message.isUserMessage() && message.hasSticker()) {
            handleSticker(message);
            return null;
        }

        if (!message.hasText()) return null;

        final String text = message.getText();

        final String command = text
                .split("\\s+", 2)[0]
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("@" + botUsername, "");

        if (command.contains("@")) return null;

        var executor = commandExtractor.findExecutor(command);
        if (executor != null)
            executor.execute(message);

        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void handleSticker(Message message) {
        var chatId = message.getChatId();
        var userId = message.getFrom().getId();
        var sticker = message.getSticker();
        var stickerEntity = new Sticker(
                userId,
                sticker.getFileUniqueId(),
                sticker.getFileId(),
                sticker.getThumb().getFileId()
        );
        if (stickerRepository.existsById(stickerEntity.getId()))
            sendMessage(chatId, "Этот стикер уже есть в вашем списке!");
        else {
            stickerRepository.save(stickerEntity);
            sendMessage(chatId, "Стикер добавлен в ваш список!");
        }
    }

    private void handleInlineQuery(InlineQuery query) {
        String tagsString = query.getQuery().trim().toLowerCase();

        Set<Sticker> stickers = tagsString.isBlank() ?
                stickerRepository.findAllByUserId(query.getFrom().getId()) :
                stickerRepository.findAllByUserIdAndTagsContaining(query.getFrom().getId(), List.of(tagsString));

        var result = stickers.stream()
                .limit(50)
                .<InlineQueryResult>map(s -> new InlineQueryResultCachedSticker()
                        .setId(s.getFileUniqueId())
                        .setStickerFileId(s.getFileId()))
                .collect(Collectors.toList());
        answerInlineQuery(query.getId(), result);
    }

    private void answerInlineQuery(String id, List<InlineQueryResult> results) {
        Methods.answerInlineQuery()
                .setInlineQueryId(id)
                .setResults(results)
                .setPersonal(true)
                .setCacheTime(1)
                .call(this);
    }

    @Override
    public Message sendMessage(long chatId, String text) {
        return sendMessage(Methods.sendMessage(chatId, text));
    }

    @Override
    public Message sendMessage(SendMessageMethod sm) {
        var text = sm.getText()
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");

        return sm
                .setText(text)
                .disableWebPagePreview()
                .enableHtml()
                .call(this);
    }
}
