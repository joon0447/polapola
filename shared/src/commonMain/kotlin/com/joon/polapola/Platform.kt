package com.joon.polapola

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform