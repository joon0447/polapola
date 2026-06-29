package com.joon.polapola.data.record

data class RecordPhotoAlbumSummary(
    val totalImageCount: Int,
    val previewImageUrls: List<String>,
    val dailyPhotoSummaries: List<DailyPhotoSummary>,
)

data class PhotoAlbumRecord(
    val id: String,
    val date: String,
    val memo: String,
    val previewImageUrl: String,
)

data class PhotoAlbumDetailRecord(
    val id: String,
    val date: String,
    val photos: List<PhotoAlbumDetailPhoto>,
)

data class PhotoAlbumDetailPhoto(
    val url: String,
    val memo: String,
    val placeName: String?,
)

data class DailyPhotoSummary(
    val recordId: String = "",
    val date: String,
    val imageCount: Int,
    val previewImageUrl: String,
)
