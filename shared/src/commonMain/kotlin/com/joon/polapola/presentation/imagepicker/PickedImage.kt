package com.joon.polapola.presentation.imagepicker

data class PickedImage(
    val bytes: ByteArray,
    val fileName: String?,
    val mimeType: String?,
)
