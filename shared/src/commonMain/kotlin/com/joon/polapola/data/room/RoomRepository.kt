package com.joon.polapola.data.room

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

class RoomRepository {
    @Suppress("DEPRECATION")
    suspend fun createRoom(
        name: String,
        firstMetDate: String,
        ownerUid: String,
    ): String {
        val roomDocument = Firebase.firestore.collection(ROOMS_COLLECTION).document
        val serverTimestamp = Timestamp.ServerTimestamp

        roomDocument.set(
            strategy = RoomDocument.serializer(),
            data =
                RoomDocument(
                    id = roomDocument.id,
                    name = name.trim(),
                    firstMetDate = firstMetDate,
                    ownerUid = ownerUid,
                    memberUids = listOf(ownerUid),
                    createdAt = serverTimestamp,
                    updatedAt = serverTimestamp,
                ),
            encodeDefaults = true,
        )

        return roomDocument.id
    }

    private companion object {
        private const val ROOMS_COLLECTION = "rooms"
    }
}
