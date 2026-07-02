package com.joon.polapola.data.imagecache

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile

actual class ImageFileStorage {
    actual suspend fun exists(localFilePath: String): Boolean =
        NSFileManager
            .defaultManager
            .fileExistsAtPath(localFilePath)

    actual suspend fun read(localFilePath: String): ByteArray? =
        NSData
            .dataWithContentsOfFile(localFilePath)
            ?.toByteArray()

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun write(
        imageUrl: String,
        bytes: ByteArray,
    ): StoredImageFile? {
        val directoryPath = imageCacheDirectoryPath() ?: return null
        NSFileManager
            .defaultManager
            .createDirectoryAtPath(
                path = directoryPath,
                withIntermediateDirectories = true,
                attributes = null,
                error = null,
            )

        val filePath = "$directoryPath/${imageUrl.toFnv1aFileName()}"
        val data = bytes.toNSData()
        if (exists(filePath)) {
            NSFileManager
                .defaultManager
                .removeItemAtPath(
                    path = filePath,
                    error = null,
                )
        }
        val isCreated =
            NSFileManager
                .defaultManager
                .createFileAtPath(
                    path = filePath,
                    contents = data,
                    attributes = null,
                )

        return if (isCreated) {
            StoredImageFile(
                localFilePath = filePath,
                sizeBytes = bytes.size.toLong(),
            )
        } else {
            null
        }
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData =
    usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = size.toULong(),
        )
    }

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray? {
    val bytesPointer = bytes ?: return null

    return bytesPointer.readBytes(length.toInt())
}

private fun imageCacheDirectoryPath(): String? {
    val cachesDirectory =
        NSSearchPathForDirectoriesInDomains(
            directory = NSCachesDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true,
        ).firstOrNull() as? String ?: return null

    return "$cachesDirectory/$IMAGE_CACHE_DIRECTORY"
}

private fun String.toFnv1aFileName(): String {
    var hash = FNV_OFFSET_BASIS

    encodeToByteArray().forEach { byte ->
        hash = hash xor (byte.toInt() and 0xff).toULong()
        hash *= FNV_PRIME
    }

    return hash.toString(radix = 16) + IMAGE_FILE_EXTENSION
}

private const val IMAGE_CACHE_DIRECTORY = "remote-images"
private const val IMAGE_FILE_EXTENSION = ".img"
private const val FNV_OFFSET_BASIS = 0xcbf29ce484222325UL
private const val FNV_PRIME = 0x100000001b3UL
