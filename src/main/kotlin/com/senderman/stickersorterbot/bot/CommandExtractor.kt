package com.senderman.stickersorterbot.bot

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * Class that can be used to get CommandExecutor by command.
 * To make things work, annotate each CommandExecutor interface implementation as @Component(command)
 * For example: @Component("/help") class Help{...}
 */
@Component
class CommandExtractor(
        private val context: ApplicationContext
) {

    fun findExecutor(command: String): CommandExecutor? =
            try {
                context.getBean(command, CommandExecutor::class.java)
            } catch (e: NoSuchBeanDefinitionException) {
                null
            }
}