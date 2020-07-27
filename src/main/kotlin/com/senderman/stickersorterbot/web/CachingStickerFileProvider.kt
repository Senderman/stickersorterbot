package com.senderman.stickersorterbot.web

import com.annimon.tgbotsmodule.api.methods.Methods
import com.annimon.tgbotsmodule.services.CommonAbsSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Component
class CachingStickerFileProvider(
        @Value("\${website.cacheDirectory}") private val cacheDir: String,
        private val telegram: CommonAbsSender
) {

    private final val stickersAccessTime = HashMap<String, Long>() // {fileUniqueId: lastAccessTime}
    val storeFor = TimeUnit.HOURS.toMillis(2) // for how many milliseconds store stickers
    private var cacheCleaningEnabled = false

    init {
        val cache = File(cacheDir)
        if (cache.isDirectory) {
            for (file in cache.listFiles()!!) {
                stickersAccessTime[file.nameWithoutExtension] = System.currentTimeMillis()
            }
        } else {
            cache.mkdirs()
            cache.mkdir()
        }
        enableCacheCleaning()
    }


    /**
     * Retrieve sticker from cache as file
     * If file exists, returns it, else downloads and returns
     * @param fileUniqueId fileUniqueId of telegram sticker
     * @param fileId fileId of telegram sticker
     * @return File object with sticker
     */
    fun retrieveSticker(fileUniqueId: String, fileId: String): File {
        val output = getStickerFile(fileUniqueId)
        if (output.exists()) {
            updateAccessTime(fileUniqueId)
            return output
        }

        val telegramFile = Methods.getFile(fileId).call(telegram)
        updateAccessTime(fileUniqueId)
        return telegram.downloadFile(telegramFile, output)
    }

    final fun enableCacheCleaning() {
        cacheCleaningEnabled = true
        runCacheCleaning()
    }

    fun disableCacheCleaning() {
        cacheCleaningEnabled = false
    }

    private fun updateAccessTime(fileUniqueId: String) {
        stickersAccessTime[fileUniqueId] = System.currentTimeMillis()
    }

    private fun getStickerFile(fileUniqueId: String) = File("$cacheDir/${fileUniqueId}.webp")

    private fun removeStickerFromCache(fileUniqueId: String) {
        stickersAccessTime.remove(fileUniqueId)
        getStickerFile(fileUniqueId).delete()
    }

    private fun runCacheCleaning() {
        thread {
            while (cacheCleaningEnabled) {
                for ((fileUniqueId, time) in stickersAccessTime) {
                    if (System.currentTimeMillis() - time > storeFor) {
                        removeStickerFromCache(fileUniqueId)
                    }
                }
                Thread.sleep(TimeUnit.MINUTES.toMillis(10))
            }
        }
    }
}