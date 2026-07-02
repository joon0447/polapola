package com.joon.polapola.data.imagecache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.joon.polapola.data.local.PolapolaDatabase

private lateinit var localImageCacheContext: Context

internal val applicationContext: Context
    get() = localImageCacheContext

fun initializeLocalImageCache(context: Context) {
    localImageCacheContext = context.applicationContext
}

internal actual fun createSqlDriver(): SqlDriver =
    AndroidSqliteDriver(
        schema = PolapolaDatabase.Schema,
        context = applicationContext,
        name = DATABASE_NAME,
    )

private const val DATABASE_NAME = "polapola.db"
