package com.joon.polapola.data.auth

import dev.gitlive.firebase.firestore.BaseTimestamp
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDocument(
    val uid: String,
    val email: String?,
    val nickname: String?,
    val photoUrl: String?,
    val provider: String,
    val createdAt: BaseTimestamp,
    val updatedAt: BaseTimestamp,
    val lastLoginAt: BaseTimestamp,
    val onboardingCompleted: Boolean,
)

@Serializable
data class UserProfileLoginUpdateDocument(
    val uid: String,
    val email: String?,
    val photoUrl: String?,
    val provider: String,
    val updatedAt: BaseTimestamp,
    val lastLoginAt: BaseTimestamp,
)
