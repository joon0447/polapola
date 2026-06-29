package com.joon.polapola.data.record

import com.joon.polapola.presentation.imagepicker.PickedImage
import dev.gitlive.firebase.storage.Data

expect fun PickedImage.toStorageData(): Data

expect fun PickedImage.clearStorageData()
