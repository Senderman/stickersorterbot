package com.senderman.stickersorterbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;


/**
 * Interface which represents executor of commands given to bot by user
 */

public interface CommandExecutor {

    /**
     * The command itself. For example, "/help". It takes value from @Component annotation, by default
     */
    default String getCommand() {
        return getMyCommand(this);
    }

    /**
     * Description of the command
     */
    String getDescription();


    /**
     * Indicates if the command should be displayed in help message
     */
    default boolean showInHelp() {
        return true;
    }


    /**
     * Executes the logic of the given command.
     *
     * @param message user's message with command
     */
    void execute(Message message);


    private String getMyCommand(CommandExecutor executor) {
        return executor.getClass().getAnnotation(Component.class).value();
    }

}
