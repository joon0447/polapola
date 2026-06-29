package com.joon.polapola.presentation.imagepicker

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.decodeToImageBitmap

fun ByteArray.toImageBitmapOrNull(): ImageBitmap? =
    runCatching {
        decodeToImageBitmap()
    }.getOrNull()
