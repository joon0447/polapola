package com.joon.polapola.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joon.polapola.data.record.DailyPhotoSummary
import com.joon.polapola.presentation.home.components.EmptyHomeContent
import com.joon.polapola.presentation.home.components.RoomHomeContent
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun HomeScreen(
    hasRoom: Boolean = true,
    roomName: String = "",
    relationshipDayCount: Int? = null,
    photoCount: Int = 0,
    photoPreviewUrls: List<String> = emptyList(),
    dailyPhotoSummaries: List<DailyPhotoSummary> = emptyList(),
    onJoinWithInviteCodeClick: () -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
    onPhotoAlbumClick: () -> Unit = {},
) {
    if (hasRoom) {
        RoomHomeContent(
            roomName = roomName,
            relationshipDayCount = relationshipDayCount,
            photoCount = photoCount,
            photoPreviewUrls = photoPreviewUrls,
            dailyPhotoSummaries = dailyPhotoSummaries,
            onPhotoAlbumClick = onPhotoAlbumClick,
        )
    } else {
        EmptyHomeContent(
            onJoinWithInviteCodeClick = onJoinWithInviteCodeClick,
            onCreateRoomClick = onCreateRoomClick,
        )
    }
}

@Preview
@Composable
private fun RoomHomeScreenPreview() {
    AppTheme {
        HomeScreen(
            hasRoom = true,
            roomName = "민서의 공부방",
            relationshipDayCount = 1000,
            photoCount = 24,
            dailyPhotoSummaries =
                listOf(
                    DailyPhotoSummary(date = "2026-06-14", imageCount = 8, previewImageUrl = ""),
                    DailyPhotoSummary(date = "2026-06-11", imageCount = 4, previewImageUrl = ""),
                ),
        )
    }
}

@Preview
@Composable
private fun EmptyHomeScreenPreview() {
    AppTheme {
        HomeScreen(hasRoom = false)
    }
}
