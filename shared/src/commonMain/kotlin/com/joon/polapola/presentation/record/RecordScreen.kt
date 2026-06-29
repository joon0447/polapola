package com.joon.polapola.presentation.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joon.polapola.data.place.DatePlace
import com.joon.polapola.presentation.imagepicker.PickedImage
import com.joon.polapola.presentation.imagepicker.rememberImagePickerLauncher
import com.joon.polapola.presentation.record.components.PhotoUploadCard
import com.joon.polapola.presentation.record.components.RecordDateField
import com.joon.polapola.presentation.record.components.RecordEditIcon
import com.joon.polapola.presentation.record.components.RecordHeader
import com.joon.polapola.presentation.record.components.RecordHeartIcon
import com.joon.polapola.presentation.record.components.RecordPlaceField
import com.joon.polapola.presentation.record.components.RecordSaveButton
import com.joon.polapola.presentation.record.components.RecordTextField
import com.joon.polapola.presentation.record.place.PlaceSearchScreen
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (DateRecordInput) -> Unit = {},
) {
    val today =
        remember {
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
    var selectedDate by remember { mutableStateOf(today) }
    var selectedImages by remember { mutableStateOf(emptyList<PickedImage>()) }
    var selectedPlace by remember { mutableStateOf<DatePlace?>(null) }
    var memo by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPhotoSourcePicker by remember { mutableStateOf(false) }
    var showPlaceSearch by remember { mutableStateOf(false) }
    val imagePickerLauncher =
        rememberImagePickerLauncher { pickedImages ->
            selectedImages =
                (selectedImages + pickedImages)
                    .take(MAX_RECORD_IMAGE_COUNT)
        }
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        )
    val isSaveEnabled = selectedPlace != null || memo.isNotBlank() || selectedImages.isNotEmpty()

    if (showPlaceSearch) {
        PlaceSearchScreen(
            onBackClick = { showPlaceSearch = false },
            onPlaceSelected = { place ->
                selectedPlace = place
                showPlaceSearch = false
            },
        )
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RecordHeader(onBackClick = onBackClick)
        PhotoUploadCard(
            pickedImages = selectedImages,
            maxImageCount = MAX_RECORD_IMAGE_COUNT,
            onClick = { showPhotoSourcePicker = true },
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            RecordDateField(
                value = selectedDate.toDisplayText(),
                onClick = { showDatePicker = true },
            )
            RecordPlaceField(
                place = selectedPlace,
                onClick = { showPlaceSearch = true },
            )
            RecordTextField(
                label = "메모",
                value = memo,
                placeholder = "함께 웃었던 순간을 짧게 적어보세요",
                onValueChange = { memo = it },
                leadingIcon = { RecordHeartIcon() },
                trailingIcon = { RecordEditIcon() },
                height = 92.dp,
                singleLine = false,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        RecordSaveButton(
            enabled = isSaveEnabled,
            onClick = {
                onSaveClick(
                    DateRecordInput(
                        date = selectedDate.toString(),
                        place = selectedPlace,
                        memo = memo.trim(),
                        images = selectedImages,
                    ),
                )
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

    if (showPhotoSourcePicker) {
        AlertDialog(
            onDismissRequest = { showPhotoSourcePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPhotoSourcePicker = false
                        imagePickerLauncher.launchCamera()
                    },
                ) {
                    Text(text = "사진 촬영")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPhotoSourcePicker = false
                        imagePickerLauncher.launchGallery()
                    },
                ) {
                    Text(text = "앨범에서 선택")
                }
            },
            title = {
                Text(text = "사진 추가")
            },
            text = {
                Text(text = "사진을 어떻게 추가할까요?")
            },
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    enabled = datePickerState.selectedDateMillis != null,
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            selectedDate = selectedDateMillis.toLocalDate()
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

private fun Long.toLocalDate(): LocalDate =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date

private fun LocalDate.toDisplayText(): String =
    "${year.toString().padStart(4, '0')}. ${(month.ordinal + 1).twoDigits()}. ${day.twoDigits()} ${
        dayOfWeek.toKoreanText()
    }"

private fun Int.twoDigits(): String = toString().padStart(2, '0')

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

private const val MAX_RECORD_IMAGE_COUNT = 10

@Preview
@Composable
private fun RecordScreenPreview() {
    AppTheme {
        RecordScreen()
    }
}
