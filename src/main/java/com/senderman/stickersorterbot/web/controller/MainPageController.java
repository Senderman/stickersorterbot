package com.senderman.stickersorterbot.web.controller;

import com.annimon.tgbotsmodule.services.CommonAbsSender;
import com.senderman.stickersorterbot.model.Sticker;
import com.senderman.stickersorterbot.repository.StickerRepository;
import com.senderman.stickersorterbot.util.BotExtensions;
import com.senderman.stickersorterbot.web.CachingStickerFileProvider;
import com.senderman.stickersorterbot.web.entity.WebSticker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainPageController {

    private final String cacheDir;
    private final CommonAbsSender telegram;
    private final StickerRepository stickerRepository;
    private final CachingStickerFileProvider stickerCache;

    public MainPageController(
            @Value("${website.cacheDirectory}") String cacheDir,
            CommonAbsSender telegram,
            StickerRepository stickerRepository,
            CachingStickerFileProvider stickerCache
    ) {
        this.cacheDir = cacheDir;
        this.telegram = telegram;
        this.stickerRepository = stickerRepository;
        this.stickerCache = stickerCache;
    }

    @GetMapping("/")
    public String showMainPage(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            Principal principal,
            Model model
    ) {

        int userId = Integer.parseInt(principal.getName());
        String[] tagsToFind = search.split("\\s+");

        var stickers = search.isBlank() ?
                stickerRepository.findAllByUserId(userId) :
                stickerRepository.findAllByUserIdAndTagsContaining(userId, List.of(tagsToFind));

        generateWebContent(userId, model, stickers);
        return "main";
    }

    private void generateWebContent(int userId, Model model, Collection<Sticker> source) {

        String userName = BotExtensions.getFirstNameById(userId, telegram);
        model.addAttribute("username", userName);

        var webStickers = mapToWebStickers(source);

        model.addAttribute("stickers", webStickers);
    }

    private Collection<WebSticker> mapToWebStickers(Collection<Sticker> stickers) {
        return stickers.stream().map(it -> {
            var stickerFile = stickerCache.retrieveSticker(it);
            return new WebSticker(it, "/" + cacheDir + "/" + stickerFile.getName());
        }).collect(Collectors.toList());
    }
}
