package com.joon.polapola.data.room

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlin.random.Random

class RoomRepository {
    @Suppress("DEPRECATION")
    suspend fun createRoom(
        name: String,
        firstMetDate: String,
        ownerUid: String,
    ): CreatedRoom {
        val roomsCollection = Firebase.firestore.collection(ROOMS_COLLECTION)
        val roomDocument = roomsCollection.document
        val serverTimestamp = Timestamp.ServerTimestamp
        val inviteCode = generateUniqueInviteCode()

        roomDocument.set(
            strategy = RoomDocument.serializer(),
            data =
                RoomDocument(
                    id = roomDocument.id,
                    name = name.trim(),
                    firstMetDate = firstMetDate,
                    ownerUid = ownerUid,
                    partnerUid = null,
                    memberUids = listOf(ownerUid),
                    inviteCode = inviteCode,
                    status = ROOM_STATUS_WAITING,
                    createdAt = serverTimestamp,
                    updatedAt = serverTimestamp,
                    matchedAt = null,
                ),
            encodeDefaults = true,
        )

        return CreatedRoom(
            id = roomDocument.id,
            inviteCode = inviteCode,
        )
    }

    private companion object {
        private const val ROOMS_COLLECTION = "rooms"
        private const val INVITE_CODE_FIELD = "inviteCode"
        private const val INVITE_CODE_LENGTH = 6
        private const val INVITE_CODE_MAX_ATTEMPTS = 10
        private const val INVITE_CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val ROOM_STATUS_WAITING = "waiting"
    }

    @Suppress("DEPRECATION")
    private suspend fun generateUniqueInviteCode(): String {
        repeat(INVITE_CODE_MAX_ATTEMPTS) {
            val inviteCode = generateInviteCode()
            val existingRooms =
                Firebase
                    .firestore
                    .collection(ROOMS_COLLECTION)
                    .where(field = INVITE_CODE_FIELD, equalTo = inviteCode)
                    .get()

            if (existingRooms.documents.isEmpty()) {
                return inviteCode
            }
        }

        error("Failed to generate a unique invite code.")
    }

    private fun generateInviteCode(): String =
        buildString(capacity = INVITE_CODE_LENGTH) {
            repeat(INVITE_CODE_LENGTH) {
                append(INVITE_CODE_CHARACTERS[Random.nextInt(INVITE_CODE_CHARACTERS.length)])
            }
        }
}

data class CreatedRoom(
    val id: String,
    val inviteCode: String,
)
