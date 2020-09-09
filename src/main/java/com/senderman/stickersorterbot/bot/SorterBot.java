package com.senderman.stickersorterbot.bot;

import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.BotModule;
import com.annimon.tgbotsmodule.beans.Config;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class SorterBot implements BotModule {

    private final BotHandler botHandler;

    public SorterBot(BotHandler botHandler) {
        this.botHandler = botHandler;
    }

    @Override
    public @NotNull BotHandler botHandler(@NotNull Config config) {
        return botHandler;
    }
}
