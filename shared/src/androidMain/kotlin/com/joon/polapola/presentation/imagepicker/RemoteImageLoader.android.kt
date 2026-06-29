package com.joon.polapola.presentation.imagepicker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

actual suspend fun loadRemoteImageBytes(url: String): ByteArray? =
    withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection

        runCatching {
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
            connection.readTimeout = READ_TIMEOUT_MILLIS
            connection.inputStream.use { inputStream ->
                inputStream.readBytes()
            }
        }.getOrNull()
            .also {
                connection.disconnect()
            }
    }

private const val CONNECT_TIMEOUT_MILLIS = 8_000
private const val READ_TIMEOUT_MILLIS = 8_000
