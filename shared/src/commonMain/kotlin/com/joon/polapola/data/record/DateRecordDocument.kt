package com.joon.polapola.data.record

import dev.gitlive.firebase.firestore.BaseTimestamp
import kotlinx.serialization.Serializable

@Serializable
data class DateRecordDocument(
    val id: String,
    val roomId: String,
    val date: String,
    val place: DatePlaceDocument?,
    val memo: String,
    val images: List<DateRecordImageDocument>,
    val createdByUid: String,
    val createdAt: BaseTimestamp,
    val updatedAt: BaseTimestamp,
)

@Serializable
data class DateRecordImageDocument(
    val url: String,
    val storagePath: String,
    val fileName: String?,
    val mimeType: String?,
    val order: Int,
)
