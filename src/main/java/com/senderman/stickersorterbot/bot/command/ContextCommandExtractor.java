package com.senderman.stickersorterbot.bot.command;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * Default CommandExtractor implementation that can be used to get CommandExecutor by command, using spring context
 * To make things work, annotate each CommandExecutor interface implementation as @Component(command)
 * For example: @Component("/help") class Help{...}
 */

@Component
public class ContextCommandExtractor implements CommandExtractor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    @Nullable
    public CommandExecutor findExecutor(String command) {
        try {
            return context.getBean(command, CommandExecutor.class);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
