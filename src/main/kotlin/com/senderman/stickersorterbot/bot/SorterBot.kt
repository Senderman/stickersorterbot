package com.senderman.stickersorterbot.bot

import com.annimon.tgbotsmodule.BotHandler
import com.annimon.tgbotsmodule.BotModule
import com.annimon.tgbotsmodule.beans.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SorterBot(
        @Autowired
        private val sorterBotHandler: SorterBotHandler
) : BotModule {

    override fun botHandler(config: Config): BotHandler = sorterBotHandler

}