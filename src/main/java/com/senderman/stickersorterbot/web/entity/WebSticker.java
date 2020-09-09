package com.senderman.stickersorterbot.web.entity;

import com.senderman.stickersorterbot.model.Sticker;

import java.util.Objects;

public class WebSticker {

    private final Sticker sticker;
    private final String src;
    private final String tags;

    public WebSticker(Sticker sticker, String src) {
        this.sticker = sticker;
        this.src = src;
        this.tags = String.join(" ", sticker.getTags());
    }

    public Sticker getSticker() {
        return sticker;
    }

    public String getSrc() {
        return src;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSticker that = (WebSticker) o;
        return sticker.equals(that.sticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sticker);
    }
}
