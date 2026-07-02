package com.joon.polapola.presentation.imagepicker

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.dataTaskWithURL
import kotlin.coroutines.resume

internal actual suspend fun downloadRemoteImageBytes(url: String): ByteArray? {
    val nsUrl = NSURL.URLWithString(url) ?: return null

    return suspendCancellableCoroutine { continuation ->
        val task =
            NSURLSession.sharedSession.dataTaskWithURL(nsUrl) { data, _, _ ->
                val bytes = data?.toByteArray()

                if (continuation.isActive) {
                    continuation.resume(bytes)
                }
            }

        continuation.invokeOnCancellation {
            task.cancel()
        }
        task.resume()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray? {
    val bytesPointer = bytes ?: return null

    return bytesPointer.readBytes(length.toInt())
}
