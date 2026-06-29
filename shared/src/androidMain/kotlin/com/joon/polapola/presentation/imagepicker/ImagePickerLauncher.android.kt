package com.joon.polapola.presentation.imagepicker

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

actual class ImagePickerLauncher internal constructor(
    private val launchGalleryPicker: () -> Unit,
    private val launchCameraPicker: () -> Unit,
) {
    actual fun launchGallery() {
        launchGalleryPicker()
    }

    actual fun launchCamera() {
        launchCameraPicker()
    }
}

@Composable
actual fun rememberImagePickerLauncher(onImagesPicked: (List<PickedImage>) -> Unit): ImagePickerLauncher {
    val context = LocalContext.current
    val pickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_IMAGE_PICK_COUNT),
            onResult = { uris ->
                uris
                    .mapNotNull { uri -> uri.toPickedImage(context) }
                    .takeIf { pickedImages -> pickedImages.isNotEmpty() }
                    ?.let(onImagesPicked)
            },
        )
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bitmap ->
                bitmap
                    ?.toPickedImage()
                    ?.let { pickedImage -> onImagesPicked(listOf(pickedImage)) }
            },
        )

    return remember(pickerLauncher, cameraLauncher) {
        ImagePickerLauncher(
            launchGalleryPicker = {
                pickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
            launchCameraPicker = {
                cameraLauncher.launch(null)
            },
        )
    }
}

private const val MAX_IMAGE_PICK_COUNT = 10

private fun Uri.toPickedImage(context: Context): PickedImage? {
    val bytes =
        context
            .contentResolver
            .openInputStream(this)
            ?.use { inputStream -> inputStream.readBytes() }
            ?: return null

    return PickedImage(
        bytes = bytes,
        fileName = context.getDisplayName(this),
        mimeType = context.contentResolver.getType(this),
    )
}

private fun Bitmap.toPickedImage(): PickedImage {
    val outputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 92, outputStream)

    return PickedImage(
        bytes = outputStream.toByteArray(),
        fileName = "camera-photo.jpg",
        mimeType = "image/jpeg",
    )
}

private fun Context.getDisplayName(uri: Uri): String? =
    contentResolver
        .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor ->
            if (!cursor.moveToFirst()) return@use null
            val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex < 0) return@use null

            cursor.getString(displayNameIndex)
        }
