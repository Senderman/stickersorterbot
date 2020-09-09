package com.senderman.stickersorterbot;

import com.annimon.tgbotsmodule.BotModule;
import com.annimon.tgbotsmodule.Runner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.List;

@SpringBootApplication
public class StickersorterbotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        var context = SpringApplication.run(StickersorterbotApplication.class, args);
        var botModule = context.getBean(BotModule.class);
        Runner.run("", List.of(botModule));
    }

}
