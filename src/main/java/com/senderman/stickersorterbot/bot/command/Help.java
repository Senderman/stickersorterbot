package com.senderman.stickersorterbot.bot.command;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import com.senderman.stickersorterbot.bot.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component("/help")
public class Help<TBot extends CommonAbsSender & MessageSender> implements CommandExecutor {

    private final TBot bot;
    private final String helpText;
    private final String botName;


    public Help(
            TBot bot,
            @Value("classpath:help.txt") Resource resource,
            @Value("${bot.username}") String botName
    ) throws IOException {

        this.bot = bot;
        this.botName = botName;

        try (InputStream in = resource.getInputStream()) {
            this.helpText = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        }
    }

    @Override
    public String getDescription() {
        return "помощь по боту";
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        try {
            bot.execute(
                    new SendMessage(chatId, helpText.replaceFirst("%botname%", botName)).enableHtml(true)
            );
        } catch (TelegramApiException e) {
            bot.sendMessage(Methods.sendMessage(chatId, "Сначала напишите боту в лс!")
                    .setReplyToMessageId(message.getMessageId()));
        }
    }
}
