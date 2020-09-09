package com.senderman.stickersorterbot.bot.command;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import com.senderman.stickersorterbot.bot.MessageSender;
import com.senderman.stickersorterbot.model.Sticker;
import com.senderman.stickersorterbot.repository.StickerRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;


@Component("/addpack")
class AddPack<TBot extends CommonAbsSender & MessageSender> implements CommandExecutor {

    private final TBot bot;
    private final StickerRepository stickerRepo;

    public AddPack(TBot bot, StickerRepository stickerRepo) {
        this.bot = bot;
        this.stickerRepo = stickerRepo;
    }

    @Override
    public String getDescription() {
        return "добавить весь пак реплаем на стикер";
    }


    @Override
    public void execute(Message message) {
        final var chatId = message.getChatId();
        if (!message.isReply()) {
            bot.sendMessage(chatId, "Ответьте этой командой на стикер, стикерпак которого вы хотите добавить!");
            return;
        }

        final var reply = message.getReplyToMessage();
        if (!reply.hasSticker()) {
            bot.sendMessage(chatId, "Ответьте этой командой на стикер, стикерпак которого вы хотите добавить!");
            return;
        }

        bot.sendMessage(chatId, "Добавляю...");

        final var setName = reply.getSticker().getSetName();
        final var stickerPack = Methods.Stickers
                .getStickerSet(setName)
                .call(bot);

        final var stickers = stickerPack.getStickers().stream()
                .map(it -> new Sticker(
                        message.getFrom().getId(),
                        it.getFileUniqueId(),
                        it.getFileId(),
                        it.getThumb().getFileId()))
                .collect(Collectors.toList());

        stickerRepo.saveAll(stickers);
        bot.sendMessage(
                chatId,
                "Стикеры из стикерпака " + stickerPack.getTitle() + "успешно добавлены в список!"
        );
    }
}