package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun RoomHomeContent(
    roomName: String,
    relationshipDayCount: Int?,
    photoCount: Int = 0,
    photoPreviewUrls: List<String> = emptyList(),
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RoomTitle(roomName = roomName)
        AnniversarySummaryCard(relationshipDayCount = relationshipDayCount)
        PhotoAlbumMenu(
            photoCount = photoCount,
            previewImageUrls = photoPreviewUrls,
        )
        WeekCalendarTools()
        WeeklyDateRecords()
    }
}

@Preview
@Composable
private fun RoomHomeContentPreview() {
    AppTheme {
        RoomHomeContent(
            roomName = "민서의 공부방",
            relationshipDayCount = 1000,
            photoCount = 24,
        )
    }
}
