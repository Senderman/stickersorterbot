package com.senderman.stickersorterbot

import com.annimon.tgbotsmodule.Runner
import com.senderman.stickersorterbot.bot.SorterBot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer

@SpringBootApplication
class StickerSorterBotApplication

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val context = runApplication<StickerSorterBotApplication>(*args)
    Runner.run("", mutableListOf(context.getBean(SorterBot::class.java)))
}
