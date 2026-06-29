package com.joon.polapola.presentation.record

import com.joon.polapola.presentation.imagepicker.PickedImage

data class DateRecordInput(
    val date: String,
    val place: String,
    val memo: String,
    val images: List<PickedImage>,
)
