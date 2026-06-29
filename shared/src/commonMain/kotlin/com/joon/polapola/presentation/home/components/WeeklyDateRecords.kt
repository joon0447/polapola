package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.data.record.DailyPhotoSummary
import com.joon.polapola.presentation.imagepicker.loadRemoteImageBytes
import com.joon.polapola.presentation.imagepicker.toImageBitmapOrNull
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun WeeklyDateRecords(dailyPhotoSummaries: List<DailyPhotoSummary> = emptyList()) {
    val lastWeekPhotoSummaries =
        remember(dailyPhotoSummaries) {
            val lastWeekDates = lastWeekDateSet()

            dailyPhotoSummaries.filter { summary -> summary.date in lastWeekDates }
        }
    val lastWeekPhotoCount = lastWeekPhotoSummaries.sumOf { summary -> summary.imageCount }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(38.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "저번 주 사진첩",
                    color = Color(0xFF2A1B24),
                    fontSize = 20.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "우리 둘이 저장한 사진 ${lastWeekPhotoCount}장",
                    color = Color(0xFF8B6879),
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        if (lastWeekPhotoSummaries.isEmpty()) {
            EmptyPhotoGrid(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentPadding = PaddingValues(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                items(
                    items = lastWeekPhotoSummaries,
                    key = { summary -> summary.date },
                ) { summary ->
                    DailyPhotoCard(summary = summary)
                }
            }
        }
    }
}

@Composable
private fun DailyPhotoCard(summary: DailyPhotoSummary) {
    val image = rememberRemoteImageBitmap(imageUrl = summary.previewImageUrl)
    val displayDate = summary.date.toDisplayDate()

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFFFF7FA),
        border = BorderStroke(width = 1.dp, color = Color(0xFFF2D7E2)),
        shadowElevation = 1.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFFE6F4)),
                )
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color(0x99000000),
                                    ),
                            ),
                        ),
            )
            Surface(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp),
                shape = CircleShape,
                color = Color(0xE6FFFFFF),
            ) {
                Text(
                    text = "${summary.imageCount}장",
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    color = Color(0xFFD65B87),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = displayDate.date,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = displayDate.day,
                    color = Color(0xFFFFD1E7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun rememberRemoteImageBitmap(imageUrl: String): ImageBitmap? {
    var image by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        image =
            imageUrl
                .takeIf { url -> url.isNotBlank() }
                ?.let { url -> loadRemoteImageBytes(url)?.toImageBitmapOrNull() }
    }

    return image
}

@Composable
private fun EmptyPhotoGrid(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFFFF7FA),
        border = BorderStroke(width = 1.dp, color = Color(0xFFF2D7E2)),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "저번 주에 저장한 데이트 사진이 없어요",
                color = Color(0xFF8B6879),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String.toDisplayDate(): DisplayDate {
    val parsedDate =
        runCatching {
            LocalDate.parse(this)
        }.getOrNull()
    val monthDay =
        parsedDate
            ?.let { date -> "${date.month.ordinal + 1}.${date.day}" }
            ?: this
    val day =
        parsedDate
            ?.dayOfWeek
            ?.toKoreanText()
            .orEmpty()

    return DisplayDate(
        date = monthDay,
        day = day,
    )
}

private fun lastWeekDateSet(): Set<String> {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val lastWeekMonday = today.startOfWeek().minus(DatePeriod(days = DAYS_IN_WEEK))

    return List(DAYS_IN_WEEK) { index ->
        lastWeekMonday.plus(DatePeriod(days = index)).toString()
    }.toSet()
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

private fun DayOfWeek.toKoreanText(): String =
    when (this) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }

private data class DisplayDate(
    val date: String,
    val day: String,
)

private const val DAYS_IN_WEEK = 7

@Preview
@Composable
private fun WeeklyDateRecordsPreview() {
    AppTheme {
        WeeklyDateRecords(
            dailyPhotoSummaries =
                listOf(
                    DailyPhotoSummary(date = "2026-06-23", imageCount = 8, previewImageUrl = ""),
                    DailyPhotoSummary(date = "2026-06-26", imageCount = 4, previewImageUrl = ""),
                    DailyPhotoSummary(date = "2026-06-10", imageCount = 2, previewImageUrl = ""),
                ),
        )
    }
}
