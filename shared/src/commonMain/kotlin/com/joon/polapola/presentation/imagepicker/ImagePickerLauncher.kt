package com.joon.polapola.presentation.imagepicker

import androidx.compose.runtime.Composable

expect class ImagePickerLauncher {
    fun launchGallery()

    fun launchCamera()
}

@Composable
expect fun rememberImagePickerLauncher(onImagesPicked: (List<PickedImage>) -> Unit): ImagePickerLauncher
