package com.senderman.stickersorterbot.util;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;

public class BotExtensions {

    public static String getFirstNameById(int userId, CommonAbsSender telegram) {
        var member = Methods.getChatMember()
                .setChatId(userId)
                .setUserId(userId)
                .call(telegram);
        if (member.getUser() == null)
            return "Без имени";

        var user = member.getUser();
        String name = user.getFirstName().isBlank() ? "Без имени" : user.getFirstName();
        return name
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("&", "&amp;");
    }

}
