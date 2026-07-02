package com.joon.polapola.data.imagecache

data class StoredImageFile(
    val localFilePath: String,
    val sizeBytes: Long,
)

expect class ImageFileStorage() {
    suspend fun exists(localFilePath: String): Boolean

    suspend fun read(localFilePath: String): ByteArray?

    suspend fun write(
        imageUrl: String,
        bytes: ByteArray,
    ): StoredImageFile?
}
