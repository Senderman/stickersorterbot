package com.senderman.stickersorterbot.bot.command;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.senderman.stickersorterbot.UserService;
import com.senderman.stickersorterbot.bot.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component("/manage")
class WebSite implements CommandExecutor {

    private final MessageSender bot;
    private final UserService userService;
    private final String websiteUrl;

    public WebSite(MessageSender bot, UserService userService, @Value("${website.url}") String websiteUrl) {
        this.bot = bot;
        this.userService = userService;
        this.websiteUrl = websiteUrl;
    }


    @Override
    public String getDescription() {
        return "Получить ссылку на сайт";
    }

    /**
     * WARNING:
     * To use localhost, set website.url to http://127.0.0.1:8080 instead of http://localhost:8080,
     * Or this method will fail
     */
    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        int userId = message.getFrom().getId();
        if (!message.isUserMessage()) {
            bot.sendMessage(chatId, "Команду можно использовать только в лс!");
            return;
        }

        if (!userService.userExists(userId)) {
            bot.sendMessage(chatId, "Вы не зарегистрированы в системе! " +
                    "Для регистрации, отправьте боту в лс стикер, либо выполните команду " +
                    "/password вашНовыйПароль");
            return;
        }

        var keyboard = List.of(List.of(new InlineKeyboardButton()
                .setText("Перейти на сайт управления стикерами")
                .setUrl(websiteUrl + "/login?telegram_id=" + userId)
        ));
        var markup = new InlineKeyboardMarkup().setKeyboard(keyboard);

        bot.sendMessage(Methods.sendMessage(chatId,
                "Для управления стикерами перейдите по ссылке ниже")
                .setReplyMarkup(markup));
    }
}