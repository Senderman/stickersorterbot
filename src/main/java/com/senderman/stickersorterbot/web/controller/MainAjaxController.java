package com.senderman.stickersorterbot.web.controller;

import com.senderman.stickersorterbot.model.Sticker;
import com.senderman.stickersorterbot.repository.StickerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
public class MainAjaxController {

    private final StickerRepository stickerRepository;

    public MainAjaxController(StickerRepository stickerRepository) {
        this.stickerRepository = stickerRepository;
    }

    @PostMapping("/setTags")
    public void setTags(
            @RequestBody SetTagAjaxBody ajax,
            Principal principal
    ) {
        int userId = Integer.parseInt(principal.getName());
        String[] tags = ajax.tags.toLowerCase().split("\\s+");
        stickerRepository.findById(Sticker.generateId(userId, ajax.getFileUniqueId())).ifPresent(it -> {
                    it.setTags(Set.of(tags));
                    stickerRepository.save(it);
                }
        );
    }

    @PostMapping("/deleteSticker")
    public void deleteSticker(
            @RequestBody DeleteStickerAjaxBody ajax,
            Principal principal
    ) {
        int userId = Integer.parseInt(principal.getName());
        stickerRepository.deleteById(Sticker.generateId(userId, ajax.getFileUniqueId()));
    }

    private static class DeleteStickerAjaxBody {
        private final String fileUniqueId;

        public DeleteStickerAjaxBody(String fileUniqueId) {
            this.fileUniqueId = fileUniqueId;
        }

        public String getFileUniqueId() {
            return fileUniqueId;
        }
    }

    private static class SetTagAjaxBody {
        private final String tags;
        private final String fileUniqueId;

        public SetTagAjaxBody(String tags, String fileUniqueId) {
            this.tags = tags;
            this.fileUniqueId = fileUniqueId;
        }

        public String getTags() {
            return tags;
        }

        public String getFileUniqueId() {
            return fileUniqueId;
        }
    }
}
