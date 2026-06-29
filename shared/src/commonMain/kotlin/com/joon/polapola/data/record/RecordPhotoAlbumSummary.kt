package com.joon.polapola.data.record

data class RecordPhotoAlbumSummary(
    val totalImageCount: Int,
    val previewImageUrls: List<String>,
    val dailyPhotoSummaries: List<DailyPhotoSummary>,
)

data class DailyPhotoSummary(
    val date: String,
    val imageCount: Int,
    val previewImageUrl: String,
)
