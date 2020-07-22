package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.BotHandler
import com.annimon.tgbotsmodule.BotModule
import com.annimon.tgbotsmodule.beans.Config
import org.springframework.stereotype.Component

@Component
class SorterBot(
        private val sorterBotHandler: SorterBotHandler
) : BotModule {

    override fun botHandler(config: Config): BotHandler = sorterBotHandler

}