package com.joon.polapola.data.place

expect class KakaoLocalApiClient() {
    suspend fun searchPlaces(
        query: String,
        size: Int = 10,
    ): List<DatePlace>
}
