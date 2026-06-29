package com.joon.polapola.data.place

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.dataTaskWithURL
import kotlin.coroutines.resume

actual class KakaoLocalApiClient actual constructor() {
    actual suspend fun searchPlaces(
        query: String,
        size: Int,
    ): List<DatePlace> {
        val restApiKey = KakaoLocalApiConfig.restApiKey
        if (restApiKey.isBlank()) return emptyList()

        val encodedQuery = query.urlEncoded()
        val url =
            NSURL.URLWithString("https://dapi.kakao.com/v2/local/search/keyword.json?query=$encodedQuery&size=$size")
                ?: return emptyList()
        val configuration =
            NSURLSessionConfiguration.defaultSessionConfiguration().apply {
                HTTPAdditionalHeaders = mapOf("Authorization" to "KakaoAK $restApiKey")
            }
        val session = NSURLSession.sessionWithConfiguration(configuration)

        return suspendCancellableCoroutine { continuation ->
            val task =
                session.dataTaskWithURL(url) { data, _, _ ->
                    val places =
                        data
                            ?.toDatePlaces()
                            .orEmpty()

                    if (continuation.isActive) {
                        continuation.resume(places)
                    }
                }

            continuation.invokeOnCancellation {
                task.cancel()
            }
            task.resume()
        }
    }
}

private fun String.urlEncoded(): String =
    encodeToByteArray()
        .joinToString(separator = "") { byte ->
            val value = byte.toInt() and 0xFF
            val character = value.toChar()

            if (character.isUrlQueryAllowed()) {
                character.toString()
            } else {
                "%${value.toString(radix = 16).uppercase().padStart(2, '0')}"
            }
        }

private fun Char.isUrlQueryAllowed(): Boolean =
    this in 'A'..'Z' ||
        this in 'a'..'z' ||
        this in '0'..'9' ||
        this == '-' ||
        this == '_' ||
        this == '.' ||
        this == '~'

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toDatePlaces(): List<DatePlace> {
    val jsonObject =
        NSJSONSerialization
            .JSONObjectWithData(this, options = 0u, error = null) as? NSDictionary
            ?: return emptyList()
    val documents = jsonObject.objectForKey("documents") as? List<*> ?: return emptyList()

    return documents
        .mapNotNull { document -> document as? NSDictionary }
        .map { document -> document.toDatePlace() }
}

private fun NSDictionary.toDatePlace(): DatePlace =
    DatePlace(
        name = stringValue("place_name").orEmpty(),
        address = stringValue("address_name"),
        roadAddress = stringValue("road_address_name"),
        latitude = stringValue("y")?.toDoubleOrNull() ?: 0.0,
        longitude = stringValue("x")?.toDoubleOrNull() ?: 0.0,
        kakaoPlaceId = stringValue("id"),
    )

private fun NSDictionary.stringValue(key: String): String? =
    (objectForKey(key) as? String)
        ?.takeIf { value -> value.isNotBlank() }
