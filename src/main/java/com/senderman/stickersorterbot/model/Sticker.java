package com.senderman.stickersorterbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@TypeAlias("sticker")
public class Sticker {

    @Id
    private String id;

    private int userId;
    private String fileUniqueId;
    private String fileId;
    private String thumbFileId;

    private Set<String> tags;

    public Sticker() {
    }

    public Sticker(int userId, String fileUniqueId, String fileId, String thumbFileId) {
        this.userId = userId;
        this.fileUniqueId = fileUniqueId;
        this.fileId = fileId;
        this.thumbFileId = thumbFileId;
        this.id = generateId(userId, fileUniqueId);
        this.tags = new HashSet<>();
    }

    public Sticker(int userId, String fileUniqueId, String fileId) {
        this.userId = userId;
        this.fileUniqueId = fileUniqueId;
        this.fileId = fileId;
        this.id = generateId(userId, fileUniqueId);
    }


    public static String generateId(int userId, String fileUniqueId) {
        return fileUniqueId + " " + userId;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFileUniqueId() {
        return fileUniqueId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getThumbFileId() {
        return thumbFileId;
    }

    public void setThumbFileId(String thumbFileId) {
        this.thumbFileId = thumbFileId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sticker sticker = (Sticker) o;
        return id.equals(sticker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
