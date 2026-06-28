package com.joon.polapola.presentation.createroom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    onBackClick: () -> Unit = {},
    onCreateRoomClick: (roomName: String, firstDate: String) -> Unit = { _, _ -> },
    isCreating: Boolean = false,
) {
    var roomName by remember { mutableStateOf("") }
    var firstDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val isCreateRoomEnabled = roomName.isNotBlank() && firstDate.isNotBlank() && !isCreating

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CreateRoomTopBar(onBackClick = onBackClick)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .width(353.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "초대 코드로 상대를 초대하고, 데이트 기록을 함께 쌓을 수 있어요.",
                    modifier = Modifier.width(297.dp),
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Column(
                    modifier = Modifier.width(343.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CreateRoomInput(
                        value = roomName,
                        placeholder = "방 이름",
                        leadingIcon = { HomeOutlineIcon() },
                        onValueChange = { roomName = it },
                    )
                    CreateRoomDateInput(
                        value = firstDate,
                        placeholder = "처음 만난 날",
                        leadingIcon = { CalendarOutlineIcon() },
                        onClick = { showDatePicker = true },
                    )
                }
            }
        }
        Surface(
            modifier =
                Modifier
                    .width(297.dp)
                    .height(48.dp)
                    .clickable(enabled = isCreateRoomEnabled) {
                        onCreateRoomClick(roomName.trim(), firstDate)
                    },
            shape = RoundedCornerShape(15.dp),
            color = if (isCreateRoomEnabled) Color(0xFFFF4FB6) else Color(0xFF808080),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (isCreating) "방 만드는 중" else "방 만들기",
                    modifier = Modifier.width(297.dp),
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    enabled = datePickerState.selectedDateMillis != null,
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            firstDate = selectedDateMillis.toYearMonthDay()
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
private fun CreateRoomTopBar(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            ArrowLeftIcon()
        }
        Text(
            text = "방 만들기",
            color = Color(0xFF111827),
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun CreateRoomInput(
    value: String,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color(0xFF808080)),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 42.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon()
            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle =
                    MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                    ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(0xFF808080),
                                fontSize = 18.sp,
                                lineHeight = 20.sp,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

@Composable
private fun CreateRoomDateInput(
    value: String,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color(0xFF808080)),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 42.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon()
            Text(
                text = value.ifEmpty { placeholder },
                modifier = Modifier.weight(1f),
                color = if (value.isEmpty()) Color(0xFF808080) else Color.Black,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ArrowLeftIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(start = point(19f, 12f), end = point(5f, 12f), stroke = stroke)
        drawIconLine(start = point(12f, 5f), end = point(5f, 12f), stroke = stroke)
        drawIconLine(start = point(5f, 12f), end = point(12f, 19f), stroke = stroke)
    }
}

@Composable
private fun HomeOutlineIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val path =
            Path().apply {
                val roofStart = point(4f, 11f)
                moveTo(roofStart.x, roofStart.y)
                lineTo(point(12f, 4f).x, point(12f, 4f).y)
                lineTo(point(20f, 11f).x, point(20f, 11f).y)
                lineTo(point(20f, 20f).x, point(20f, 20f).y)
                lineTo(point(15f, 20f).x, point(15f, 20f).y)
                lineTo(point(15f, 14f).x, point(15f, 14f).y)
                lineTo(point(9f, 14f).x, point(9f, 14f).y)
                lineTo(point(9f, 20f).x, point(9f, 20f).y)
                lineTo(point(4f, 20f).x, point(4f, 20f).y)
                close()
            }
        drawPath(path, Color(0xFF374151), style = stroke)
    }
}

@Composable
private fun CalendarOutlineIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = Color(0xFF374151),
            topLeft = point(4f, 5f),
            size = Size(width = size.width * 16f / 24f, height = size.height * 15f / 24f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke,
        )
        drawIconLine(start = point(8f, 3f), end = point(8f, 7f), stroke = stroke)
        drawIconLine(start = point(16f, 3f), end = point(16f, 7f), stroke = stroke)
        drawIconLine(start = point(4f, 10f), end = point(20f, 10f), stroke = stroke)
    }
}

private fun DrawScope.point(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 24f, y = size.height * y / 24f)

private fun DrawScope.drawIconLine(
    start: Offset,
    end: Offset,
    stroke: Stroke,
) {
    drawLine(
        color = Color(0xFF374151),
        start = start,
        end = end,
        strokeWidth = stroke.width,
        cap = stroke.cap,
    )
}

private fun Long.toYearMonthDay(): String {
    val epochDay = floorDiv(other = MILLIS_PER_DAY)
    val (year, month, day) = epochDay.toCivilDate()

    return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${
        day.toString().padStart(2, '0')
    }"
}

private fun Long.floorDiv(other: Long): Long =
    if (this >= 0) {
        this / other
    } else {
        (this - other + 1) / other
    }

private fun Long.toCivilDate(): CivilDate {
    val shiftedDays = this + DAYS_FROM_CIVIL_TO_EPOCH
    val era =
        if (shiftedDays >= 0) {
            shiftedDays / DAYS_PER_ERA
        } else {
            (shiftedDays - DAYS_PER_ERA + 1) / DAYS_PER_ERA
        }
    val dayOfEra = shiftedDays - era * DAYS_PER_ERA
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val year = yearOfEra + era * 400
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val monthPart = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * monthPart + 2) / 5 + 1
    val month = monthPart + if (monthPart < 10) 3 else -9

    return CivilDate(
        year = (year + if (month <= 2) 1 else 0).toInt(),
        month = month.toInt(),
        day = day.toInt(),
    )
}

private data class CivilDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

private const val MILLIS_PER_DAY = 86_400_000L
private const val DAYS_FROM_CIVIL_TO_EPOCH = 719_468L
private const val DAYS_PER_ERA = 146_097L

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    AppTheme {
        CreateRoomScreen()
    }
}
