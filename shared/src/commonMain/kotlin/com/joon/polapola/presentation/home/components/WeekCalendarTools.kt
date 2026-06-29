package com.joon.polapola.presentation.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekCalendarTools(
    photoDates: List<String> = emptyList(),
    selectedWeekStart: LocalDate? = null,
    onWeekStartChange: (LocalDate) -> Unit = {},
) {
    val today =
        remember {
            Clock.System.todayIn(TimeZone.currentSystemDefault())
        }
    val currentWeekStart = remember(today) { today.startOfWeek() }
    val photoDateSet = remember(photoDates) { photoDates.toSet() }
    var internalSelectedWeekStart by remember { mutableStateOf(currentWeekStart) }
    val visibleWeekStart = selectedWeekStart ?: internalSelectedWeekStart
    var weekSlideDirection by remember { mutableStateOf(WeekSlideDirection.NONE) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dragAmount by remember { mutableFloatStateOf(0f) }
    val updateSelectedWeekStart: (LocalDate) -> Unit = { nextWeekStart ->
        if (selectedWeekStart == null) {
            internalSelectedWeekStart = nextWeekStart
        }
        onWeekStartChange(nextWeekStart)
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(92.dp)
                .pointerInput(visibleWeekStart, currentWeekStart) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragAmount = 0f
                        },
                        onHorizontalDrag = { _, dragDistance ->
                            dragAmount += dragDistance
                        },
                        onDragEnd = {
                            val nextWeekStart =
                                visibleWeekStart.swipedWeekStart(
                                    dragAmount = dragAmount,
                                    currentWeekStart = currentWeekStart,
                                )
                            if (nextWeekStart != visibleWeekStart) {
                                weekSlideDirection = nextWeekStart.toWeekSlideDirection(visibleWeekStart)
                                updateSelectedWeekStart(nextWeekStart)
                            }
                            dragAmount = 0f
                        },
                    )
                },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text =
                    visibleWeekStart.toWeekTitle(
                        currentWeekStart = currentWeekStart,
                    ),
                color = Color(0xFF1A1A1A),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleSmall,
            )
            Surface(
                modifier =
                    Modifier
                        .width(102.dp)
                        .height(32.dp),
                shape = CircleShape,
                color = Color(0xFFF3F4F6),
                onClick = { showDatePicker = true },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CalendarIcon()
                    Text(
                        text = "달력 보기",
                        color = Color(0xFF4B5563),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
        AnimatedContent(
            targetState = visibleWeekStart,
            transitionSpec = {
                val direction = weekSlideDirection
                val enterOffset = if (direction == WeekSlideDirection.NEXT) { width: Int -> width } else { width: Int -> -width }
                val exitOffset = if (direction == WeekSlideDirection.NEXT) { width: Int -> -width } else { width: Int -> width }

                slideInHorizontally(
                    animationSpec = tween(durationMillis = WEEK_SLIDE_ANIMATION_MILLIS),
                    initialOffsetX = enterOffset,
                ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(durationMillis = WEEK_SLIDE_ANIMATION_MILLIS),
                        targetOffsetX = exitOffset,
                    ) using
                    SizeTransform(clip = false)
            },
        ) { animatedWeekStart ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                animatedWeekStart
                    .toWeekDays(photoDateSet = photoDateSet)
                    .forEach { day ->
                        WeekDayCell(
                            day = day,
                            modifier = Modifier.weight(1f),
                        )
                    }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = visibleWeekStart.toUtcEpochMilliseconds(),
                selectableDates =
                    object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= today.toUtcEpochMilliseconds()
                    },
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    enabled =
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            selectedDateMillis <= today.toUtcEpochMilliseconds()
                        } ?: false,
                    onClick = {
                        datePickerState.selectedDateMillis
                            ?.toLocalDate()
                            ?.takeIf { selectedDate -> selectedDate <= today }
                            ?.let { selectedDate ->
                                val nextWeekStart = selectedDate.startOfWeek().coerceAtMost(currentWeekStart)
                                if (nextWeekStart != visibleWeekStart) {
                                    weekSlideDirection = nextWeekStart.toWeekSlideDirection(visibleWeekStart)
                                    updateSelectedWeekStart(nextWeekStart)
                                }
                            }
                        showDatePicker = false
                    },
                ) {
                    Text(text = "확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "취소")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun WeekDayCell(
    day: WeekDay,
    modifier: Modifier = Modifier,
) {
    val accent = if (day.hasRecord) Color(0xFFFF4FB6) else Color(0xFF9CA3AF)

    Surface(
        modifier =
            modifier
                .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (day.hasRecord) Color(0xFFFFF7FB) else Color.White,
        border = BorderStroke(width = 1.dp, color = if (day.hasRecord) Color(0xFFFF4FB6) else Color(0xFFE5E7EB)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = day.label,
                color = accent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = day.date,
                color = Color(0xFF1A1A1A),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun CalendarIcon() {
    Canvas(modifier = Modifier.size(17.dp)) {
        val stroke = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRectLine(point(3f, 4f), point(14f, 14f), stroke, Color(0xFFFF4FB6))
        drawIconLine(point(6f, 2.5f), point(6f, 5.5f), stroke, Color(0xFFFF4FB6))
        drawIconLine(point(11f, 2.5f), point(11f, 5.5f), stroke, Color(0xFFFF4FB6))
        drawIconLine(point(3f, 7f), point(14f, 7f), stroke, Color(0xFFFF4FB6))
    }
}

private fun LocalDate.startOfWeek(): LocalDate = minus(DatePeriod(days = dayOfWeek.toIsoDayNumber() - 1))

private fun LocalDate.coerceAtMost(maximumValue: LocalDate): LocalDate =
    if (this > maximumValue) {
        maximumValue
    } else {
        this
    }

private fun LocalDate.toWeekTitle(currentWeekStart: LocalDate): String {
    val weeksAgo = weeksUntil(currentWeekStart)

    return when (weeksAgo) {
        0 -> "이번 주"
        1 -> "저번 주"
        2 -> "2주 전"
        3 -> "3주 전"
        else -> "${month.ordinal + 1}월 ${weekOfMonth()}주차"
    }
}

private fun LocalDate.weeksUntil(other: LocalDate): Int {
    var cursor = this
    var weekCount = 0

    while (cursor < other) {
        cursor = cursor.plus(DatePeriod(days = DAYS_IN_WEEK))
        weekCount += 1
    }

    return weekCount
}

private fun LocalDate.weekOfMonth(): Int = ((day - 1) / DAYS_IN_WEEK) + 1

private fun LocalDate.swipedWeekStart(
    dragAmount: Float,
    currentWeekStart: LocalDate,
): LocalDate =
    when {
        dragAmount <= -SWIPE_THRESHOLD -> plus(DatePeriod(days = DAYS_IN_WEEK)).coerceAtMost(currentWeekStart)
        dragAmount >= SWIPE_THRESHOLD -> minus(DatePeriod(days = DAYS_IN_WEEK))
        else -> this
    }

private fun LocalDate.toWeekSlideDirection(previousWeekStart: LocalDate): WeekSlideDirection =
    when {
        this > previousWeekStart -> WeekSlideDirection.NEXT
        this < previousWeekStart -> WeekSlideDirection.PREVIOUS
        else -> WeekSlideDirection.NONE
    }

private fun LocalDate.toWeekDays(photoDateSet: Set<String>): List<WeekDay> =
    List(DAYS_IN_WEEK) { index ->
        val date = plus(DatePeriod(days = index))

        WeekDay(
            label = date.dayOfWeek.toKoreanShortText(),
            date = date.day.toString(),
            isoDate = date.toString(),
            hasRecord = date.toString() in photoDateSet,
        )
    }

private fun LocalDate.toUtcEpochMilliseconds(): Long =
    atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()

private fun Long.toLocalDate(): LocalDate =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date

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

private fun DayOfWeek.toKoreanShortText(): String =
    when (this) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }

private fun DrawScope.point(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 21f, y = size.height * y / 21f)

private fun DrawScope.drawIconLine(
    start: Offset,
    end: Offset,
    stroke: Stroke,
    color: Color,
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = stroke.width,
        cap = stroke.cap,
    )
}

private fun DrawScope.drawRectLine(
    topLeft: Offset,
    bottomRight: Offset,
    stroke: Stroke,
    color: Color,
) {
    drawRoundRect(
        color = color,
        topLeft = topLeft,
        size =
            Size(
                width = bottomRight.x - topLeft.x,
                height = bottomRight.y - topLeft.y,
            ),
        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
        style = stroke,
    )
}

private data class WeekDay(
    val label: String,
    val date: String,
    val isoDate: String,
    val hasRecord: Boolean,
)

private enum class WeekSlideDirection {
    PREVIOUS,
    NEXT,
    NONE,
}

private const val DAYS_IN_WEEK = 7
private const val SWIPE_THRESHOLD = 45f
private const val WEEK_SLIDE_ANIMATION_MILLIS = 240

@Preview
@Composable
private fun WeekCalendarToolsPreview() {
    AppTheme {
        WeekCalendarTools(
            photoDates =
                listOf(
                    "2026-06-23",
                    "2026-06-26",
                ),
        )
    }
}
