package com.joon.polapola.data.imagecache

import com.joon.polapola.data.local.PolapolaDatabase
import com.joon.polapola.presentation.imagepicker.downloadRemoteImageBytes
import kotlin.time.Clock

class CachedRemoteImageRepository(
    private val database: PolapolaDatabase = LocalImageCacheDatabase.database,
    private val fileStorage: ImageFileStorage = ImageFileStorage(),
    private val cacheLifetimeMillis: Long = CACHE_LIFETIME_MILLIS,
) {
    suspend fun loadImageBytes(imageUrl: String): ByteArray? {
        if (imageUrl.isBlank()) return null

        val now = Clock.System.now().toEpochMilliseconds()
        val cachedImage = database.cachedImageQueries.selectByUrl(imageUrl).executeAsOneOrNull()

        if (
            cachedImage != null &&
            now - cachedImage.cachedAt <= cacheLifetimeMillis &&
            fileStorage.exists(cachedImage.localFilePath)
        ) {
            fileStorage.read(cachedImage.localFilePath)?.let { bytes -> return bytes }
        }

        return downloadRemoteImageBytes(imageUrl)
            ?.also { bytes ->
                val storedFile = fileStorage.write(imageUrl = imageUrl, bytes = bytes) ?: return@also

                database.cachedImageQueries.upsert(
                    imageUrl = imageUrl,
                    localFilePath = storedFile.localFilePath,
                    updatedAt = now,
                    sizeBytes = storedFile.sizeBytes,
                    cachedAt = now,
                )
            }
    }

    private companion object {
        private const val CACHE_LIFETIME_MILLIS = 1000L * 60 * 60 * 24 * 30
    }
}
