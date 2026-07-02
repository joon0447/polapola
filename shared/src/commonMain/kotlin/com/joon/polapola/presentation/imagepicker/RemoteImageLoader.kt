package com.joon.polapola.presentation.imagepicker

import com.joon.polapola.data.imagecache.CachedRemoteImageRepository

suspend fun loadRemoteImageBytes(url: String): ByteArray? = remoteImageRepository.loadImageBytes(imageUrl = url)

internal expect suspend fun downloadRemoteImageBytes(url: String): ByteArray?

private val remoteImageRepository by lazy {
    CachedRemoteImageRepository()
}
