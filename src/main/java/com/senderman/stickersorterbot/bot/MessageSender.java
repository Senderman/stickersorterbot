package com.senderman.stickersorterbot.bot;

import com.annimon.tgbotsmodule.api.methods.send.SendMessageMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageSender {

    Message sendMessage(long chatId, String text);

    Message sendMessage(SendMessageMethod sm);

}
