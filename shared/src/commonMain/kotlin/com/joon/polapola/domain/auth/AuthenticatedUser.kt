package com.joon.polapola.domain.auth

data class AuthenticatedUser(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
)
