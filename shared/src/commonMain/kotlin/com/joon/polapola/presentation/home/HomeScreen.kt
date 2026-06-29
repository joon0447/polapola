package com.joon.polapola.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
    onJoinWithInviteCodeClick: () -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
) {
    if (hasRoom) {
        RoomHomeContent(
            roomName = roomName,
            relationshipDayCount = relationshipDayCount,
            photoCount = photoCount,
            photoPreviewUrls = photoPreviewUrls,
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
