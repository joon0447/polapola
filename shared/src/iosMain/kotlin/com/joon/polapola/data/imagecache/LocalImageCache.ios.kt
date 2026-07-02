package com.joon.polapola.data.imagecache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.joon.polapola.data.local.PolapolaDatabase

internal actual fun createSqlDriver(): SqlDriver =
    NativeSqliteDriver(
        schema = PolapolaDatabase.Schema,
        name = DATABASE_NAME,
    )

private const val DATABASE_NAME = "polapola.db"
