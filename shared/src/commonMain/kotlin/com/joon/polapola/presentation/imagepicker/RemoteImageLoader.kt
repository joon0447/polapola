package com.joon.polapola.presentation.imagepicker

expect suspend fun loadRemoteImageBytes(url: String): ByteArray?
