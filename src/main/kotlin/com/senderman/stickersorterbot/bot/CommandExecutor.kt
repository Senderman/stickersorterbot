package com.senderman.stickersorterbot.bot

import org.telegram.telegrambots.meta.api.objects.Message

/**
 * Interface which represents executor of commands given to bot by user
 */
interface CommandExecutor {

    /**
     * The command itself. For example, "/help"
     */
    val command: String

    /**
     * Description of command
     */
    val desc: String

    /**
     * Indicates if the command should be displayed in help message
     */
    val showInHelp: Boolean
        get() = false


    /**
     * Executes the logic of the given command.
     * @param message user's message with command
     */
    fun execute(message:Message)

}