package com.joon.polapola.data.record

import com.joon.polapola.data.place.DatePlace
import kotlinx.serialization.Serializable

@Serializable
data class DatePlaceDocument(
    val name: String,
    val address: String?,
    val roadAddress: String?,
    val latitude: Double,
    val longitude: Double,
    val kakaoPlaceId: String?,
)

fun DatePlace.toDatePlaceDocument(): DatePlaceDocument =
    DatePlaceDocument(
        name = name,
        address = address,
        roadAddress = roadAddress,
        latitude = latitude,
        longitude = longitude,
        kakaoPlaceId = kakaoPlaceId,
    )
