package com.joon.polapola.data.imagecache

import app.cash.sqldelight.db.SqlDriver
import com.joon.polapola.data.local.PolapolaDatabase

internal object LocalImageCacheDatabase {
    val database: PolapolaDatabase by lazy {
        PolapolaDatabase(createSqlDriver())
    }
}

internal expect fun createSqlDriver(): SqlDriver
