package com.joon.polapola.data.imagecache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

actual class ImageFileStorage {
    actual suspend fun exists(localFilePath: String): Boolean =
        withContext(Dispatchers.IO) {
            File(localFilePath).exists()
        }

    actual suspend fun read(localFilePath: String): ByteArray? =
        withContext(Dispatchers.IO) {
            runCatching {
                File(localFilePath)
                    .takeIf { file -> file.exists() }
                    ?.readBytes()
            }.getOrNull()
        }

    actual suspend fun write(
        imageUrl: String,
        bytes: ByteArray,
    ): StoredImageFile? =
        withContext(Dispatchers.IO) {
            runCatching {
                val directory = File(applicationContext.cacheDir, IMAGE_CACHE_DIRECTORY)
                directory.mkdirs()

                val imageFile = File(directory, imageUrl.toSha256FileName())
                imageFile.writeBytes(bytes)

                StoredImageFile(
                    localFilePath = imageFile.absolutePath,
                    sizeBytes = bytes.size.toLong(),
                )
            }.getOrNull()
        }
}

private fun String.toSha256FileName(): String {
    val digest =
        MessageDigest
            .getInstance("SHA-256")
            .digest(encodeToByteArray())

    return digest.joinToString(separator = "") { byte -> "%02x".format(byte) } + IMAGE_FILE_EXTENSION
}

private const val IMAGE_CACHE_DIRECTORY = "remote-images"
private const val IMAGE_FILE_EXTENSION = ".img"
