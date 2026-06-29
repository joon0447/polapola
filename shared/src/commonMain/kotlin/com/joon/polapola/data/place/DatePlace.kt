package com.joon.polapola.data.place

data class DatePlace(
    val name: String,
    val address: String?,
    val roadAddress: String?,
    val latitude: Double,
    val longitude: Double,
    val kakaoPlaceId: String?,
)
