package com.joon.polapola.presentation.imagepicker

import platform.Foundation.NSData
import platform.Foundation.NSUUID

internal object IosPickedImageDataStore {
    private val dataByKey = mutableMapOf<String, NSData>()

    fun put(data: NSData): String {
        val key = NSUUID.UUID().UUIDString
        dataByKey[key] = data

        return key
    }

    fun get(key: String?): NSData? = key?.let(dataByKey::get)

    fun remove(key: String?) {
        key?.let(dataByKey::remove)
    }
}
