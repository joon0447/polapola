package com.joon.polapola.data.record

import com.joon.polapola.presentation.imagepicker.PickedImage
import dev.gitlive.firebase.storage.Data

actual fun PickedImage.toStorageData(): Data = Data(bytes)

actual fun PickedImage.clearStorageData() = Unit
