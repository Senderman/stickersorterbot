package com.senderman.stickersorterbot.web;

import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import com.senderman.stickersorterbot.model.Sticker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class CachingStickerFileProvider {

    private final CommonAbsSender telegram;
    private final String cacheDir;
    private final Map<String, Long> stickersAccessTime = new HashMap<>(); // {fileUniqueId: lastAccessTime}
    private final long storeFor = TimeUnit.HOURS.toMillis(2); // for how many milliseconds store stickers
    private boolean cacheCleaningEnabled = false;

    public CachingStickerFileProvider(CommonAbsSender telegram, @Value("${website.cacheDirectory}") String cacheDir) {
        this.telegram = telegram;
        this.cacheDir = cacheDir;

        File cache = new File(cacheDir);
        if (cache.isDirectory()) {
            for (var file : cache.listFiles()) {
                updateAccessTime(file.getName().replaceAll("\\.[\\w\\d]+$", ""));
            }
        } else {
            cache.mkdirs();
        }
        enableCacheCleaning();
    }

    /**
     * Retrieve sticker from cache as file
     * If file exists, returns it, else downloads and returns
     *
     * @param sticker StickerEntity object
     * @return File object with stickers
     */

    public File retrieveSticker(Sticker sticker) {

        var output = getStickerFile(sticker.getFileUniqueId());
        if (output.exists()) {
            updateAccessTime(sticker.getFileUniqueId());
            return output;
        }

        var telegramFile = Methods.getFile(sticker.getThumbFileId()).call(telegram);
        updateAccessTime(sticker.getFileUniqueId());
        try {
            return telegram.downloadFile(telegramFile, output);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return output;
        }

    }


    public void enableCacheCleaning() {
        cacheCleaningEnabled = true;
        runCacheCleaning();
    }

    public void disableCacheCleaning() {
        cacheCleaningEnabled = false;
    }

    private void updateAccessTime(String fileUniqueId) {
        stickersAccessTime.replace(fileUniqueId, System.currentTimeMillis());
    }

    private File getStickerFile(String fileUniqueId) {
        return new File(cacheDir, fileUniqueId + ".webp");
    }

    private void removeStickerFromCache(String fileUniqueId) {
        stickersAccessTime.remove(fileUniqueId);
        getStickerFile(fileUniqueId).delete();
    }

    private void runCacheCleaning() {
        new Thread(() -> {
            while (cacheCleaningEnabled) {
                for (var entry : stickersAccessTime.entrySet()) {
                    if (System.currentTimeMillis() - entry.getValue() > storeFor) {
                        removeStickerFromCache(entry.getKey());
                    }
                }
            }

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}