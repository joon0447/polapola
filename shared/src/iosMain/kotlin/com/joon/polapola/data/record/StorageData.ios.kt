package com.joon.polapola.data.record

import com.joon.polapola.presentation.imagepicker.IosPickedImageDataStore
import com.joon.polapola.presentation.imagepicker.PickedImage
import dev.gitlive.firebase.storage.Data

actual fun PickedImage.toStorageData(): Data =
    Data(
        IosPickedImageDataStore.get(storageKey)
            ?: error("iOS picked image data is missing."),
    )

actual fun PickedImage.clearStorageData() {
    IosPickedImageDataStore.remove(storageKey)
}
