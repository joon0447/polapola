package com.joon.polapola.data.record

import com.joon.polapola.data.auth.AuthSessionRepository
import com.joon.polapola.data.room.RoomRepository
import com.joon.polapola.presentation.imagepicker.PickedImage
import com.joon.polapola.presentation.record.DateRecordInput
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import dev.gitlive.firebase.storage.storageMetadata

class DateRecordRepository {
    private val authSessionRepository = AuthSessionRepository()
    private val roomRepository = RoomRepository()

    suspend fun createDateRecord(input: DateRecordInput): String {
        val user = authSessionRepository.getSignedInUser() ?: error("Signed-in user is required.")
        val room = roomRepository.getRoomByMemberUid(user.uid) ?: error("Room is required.")
        val recordDocument =
            Firebase
                .firestore
                .collection(ROOMS_COLLECTION)
                .document(room.id)
                .collection(RECORDS_COLLECTION)
                .document
        val images =
            input.images.mapIndexed { index, image ->
                uploadRecordImage(
                    roomId = room.id,
                    recordId = recordDocument.id,
                    image = image,
                    order = index,
                )
            }
        val serverTimestamp = Timestamp.ServerTimestamp

        recordDocument.set(
            strategy = DateRecordDocument.serializer(),
            data =
                DateRecordDocument(
                    id = recordDocument.id,
                    roomId = room.id,
                    date = input.date,
                    place = input.place?.toDatePlaceDocument(),
                    memo = input.memo,
                    images = images,
                    createdByUid = user.uid,
                    createdAt = serverTimestamp,
                    updatedAt = serverTimestamp,
                ),
            encodeDefaults = true,
        )

        return recordDocument.id
    }

    suspend fun getPhotoAlbumSummary(roomId: String): RecordPhotoAlbumSummary {
        val records =
            Firebase
                .firestore
                .collection(ROOMS_COLLECTION)
                .document(roomId)
                .collection(RECORDS_COLLECTION)
                .orderBy(CREATED_AT_FIELD, Direction.DESCENDING)
                .get()
                .documents
                .mapNotNull { document ->
                    runCatching {
                        document.data(DateRecordDocument.serializer())
                    }.getOrNull()
                }
        val images =
            records.flatMap { record ->
                record.images.sortedBy { image -> image.order }
            }

        return RecordPhotoAlbumSummary(
            totalImageCount = images.size,
            previewImageUrls = images.take(PREVIEW_IMAGE_COUNT).map { image -> image.url },
        )
    }

    private suspend fun uploadRecordImage(
        roomId: String,
        recordId: String,
        image: PickedImage,
        order: Int,
    ): DateRecordImageDocument {
        val storagePath = "$ROOMS_COLLECTION/$roomId/$RECORDS_COLLECTION/$recordId/images/${order.toFileName(image)}"
        val imageReference = Firebase.storage.reference.child(storagePath)
        val metadata =
            storageMetadata {
                image.mimeType?.let { contentType = it }
            }

        imageReference.putData(
            data = image.toStorageData(),
            metadata = metadata,
        )
        image.clearStorageData()

        return DateRecordImageDocument(
            url = imageReference.getDownloadUrl(),
            storagePath = storagePath,
            fileName = image.fileName,
            mimeType = image.mimeType,
            order = order,
        )
    }

    private fun Int.toFileName(image: PickedImage): String {
        val extension = image.mimeType.toImageExtension()

        return "${toString().padStart(2, '0')}.$extension"
    }

    private fun String?.toImageExtension(): String =
        when (this) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }

    private companion object {
        private const val ROOMS_COLLECTION = "rooms"
        private const val RECORDS_COLLECTION = "records"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val PREVIEW_IMAGE_COUNT = 3
    }
}
