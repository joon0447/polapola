package com.joon.polapola.data.room

import dev.gitlive.firebase.firestore.BaseTimestamp
import kotlinx.serialization.Serializable

@Serializable
data class RoomDocument(
    val id: String,
    val name: String,
    val firstMetDate: String,
    val ownerUid: String,
    val memberUids: List<String>,
    val createdAt: BaseTimestamp,
    val updatedAt: BaseTimestamp,
)
