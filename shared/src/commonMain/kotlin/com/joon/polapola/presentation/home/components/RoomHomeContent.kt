package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joon.polapola.data.record.DailyPhotoSummary
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun RoomHomeContent(
    roomName: String,
    relationshipDayCount: Int?,
    photoCount: Int = 0,
    photoPreviewUrls: List<String> = emptyList(),
    dailyPhotoSummaries: List<DailyPhotoSummary> = emptyList(),
    onPhotoAlbumClick: () -> Unit = {},
    onPhotoDateClick: (String) -> Unit = {},
) {
    val today =
        remember {
            Clock.System.todayIn(TimeZone.currentSystemDefault())
        }
    var selectedWeekStart by remember(today) { mutableStateOf(today.startOfWeek()) }

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
            onClick = onPhotoAlbumClick,
        )
        WeekCalendarTools(
            photoDates = dailyPhotoSummaries.map { summary -> summary.date },
            selectedWeekStart = selectedWeekStart,
            onWeekStartChange = { weekStart ->
                selectedWeekStart = weekStart
            },
        )
        WeeklyDateRecords(
            selectedWeekStart = selectedWeekStart,
            dailyPhotoSummaries = dailyPhotoSummaries,
            onDateClick = onPhotoDateClick,
        )
    }
}

private fun LocalDate.startOfWeek(): LocalDate = minus(DatePeriod(days = dayOfWeek.toIsoDayNumber() - 1))

private fun DayOfWeek.toIsoDayNumber(): Int =
    when (this) {
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
        DayOfWeek.SUNDAY -> 7
    }

@Preview
@Composable
private fun RoomHomeContentPreview() {
    AppTheme {
        RoomHomeContent(
            roomName = "민서의 공부방",
            relationshipDayCount = 1000,
            photoCount = 24,
            dailyPhotoSummaries =
                listOf(
                    DailyPhotoSummary(date = "2026-06-14", imageCount = 8, previewImageUrl = ""),
                    DailyPhotoSummary(date = "2026-06-11", imageCount = 4, previewImageUrl = ""),
                    DailyPhotoSummary(date = "2026-06-10", imageCount = 2, previewImageUrl = ""),
                ),
        )
    }
}
