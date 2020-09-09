package com.senderman.stickersorterbot.bot.command;


import javax.annotation.Nullable;

public interface CommandExtractor {

    /**
     * Find executor by command
     *
     * @param command command starting with "/"
     * @return appropriate command executor implementation, or null if command not found
     */
    @Nullable
    public CommandExecutor findExecutor(String command);

}
