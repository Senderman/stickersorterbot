package com.senderman.stickersorterbot.bot.command;

import com.senderman.stickersorterbot.UserService;
import com.senderman.stickersorterbot.bot.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/password")
class SetPassword implements CommandExecutor {

    private final MessageSender bot;
    private final UserService userManager;

    public SetPassword(MessageSender bot, UserService userManager) {
        this.bot = bot;
        this.userManager = userManager;
    }

    @Override
    public String getDescription() {
        return "изменить пароль. " + getCommand() + " p@ssw0rd";
    }

    public void execute(Message message) {
        long chatId = message.getChatId();

        if (!message.isUserMessage()) {
            bot.sendMessage(chatId, "Команду можно использовать только в лс!");
            return;
        }

        int userId = message.getFrom().getId();
        String[] args = message.getText().split("\\s+", 2);
        if (args.length < 2) {
            bot.sendMessage(chatId, "Использование: " + getCommand() + " p@ssw0rd");
            return;
        }

        String password = args[1];
        userManager.changePassword(userId, password);
        bot.sendMessage(chatId, "Ваш пароль успешно изменен!\n" +
                "Ваш Telegram ID: " + userId + "\n" +
                "Ваш пароль: " + password);
    }

}
