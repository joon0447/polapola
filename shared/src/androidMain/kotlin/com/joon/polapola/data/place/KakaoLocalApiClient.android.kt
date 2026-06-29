package com.joon.polapola.data.place

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

actual class KakaoLocalApiClient actual constructor() {
    actual suspend fun searchPlaces(
        query: String,
        size: Int,
    ): List<DatePlace> =
        withContext(Dispatchers.IO) {
            val restApiKey = KakaoLocalApiConfig.restApiKey
            if (restApiKey.isBlank()) return@withContext emptyList()

            val encodedQuery = URLEncoder.encode(query, Charsets.UTF_8.name())
            val url = URL("https://dapi.kakao.com/v2/local/search/keyword.json?query=$encodedQuery&size=$size")
            val connection = url.openConnection() as HttpURLConnection

            runCatching {
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "KakaoAK $restApiKey")
                connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
                connection.readTimeout = READ_TIMEOUT_MILLIS

                val responseText =
                    connection
                        .inputStream
                        .bufferedReader()
                        .use { reader -> reader.readText() }

                responseText.toDatePlaces()
            }.getOrElse {
                emptyList()
            }.also {
                connection.disconnect()
            }
        }
}

private fun String.toDatePlaces(): List<DatePlace> {
    val documents = JSONObject(this).optJSONArray("documents") ?: return emptyList()

    return List(documents.length()) { index ->
        documents.getJSONObject(index).toDatePlace()
    }
}

private fun JSONObject.toDatePlace(): DatePlace =
    DatePlace(
        name = optString("place_name"),
        address = optStringOrNull("address_name"),
        roadAddress = optStringOrNull("road_address_name"),
        latitude = optString("y").toDoubleOrNull() ?: 0.0,
        longitude = optString("x").toDoubleOrNull() ?: 0.0,
        kakaoPlaceId = optStringOrNull("id"),
    )

private fun JSONObject.optStringOrNull(name: String): String? =
    optString(name)
        .takeIf { value -> value.isNotBlank() }

private const val CONNECT_TIMEOUT_MILLIS = 8_000
private const val READ_TIMEOUT_MILLIS = 8_000
