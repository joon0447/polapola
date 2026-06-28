package com.joon.polapola.data.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

class UserProfileRepository {
    @Suppress("DEPRECATION")
    suspend fun upsertUserProfile(user: FirebaseUser) {
        val userDocument = Firebase.firestore.collection(USERS_COLLECTION).document(user.uid)
        val serverTimestamp = Timestamp.ServerTimestamp

        if (userDocument.get().exists) {
            userDocument.set(
                strategy = UserProfileLoginUpdateDocument.serializer(),
                data =
                    UserProfileLoginUpdateDocument(
                        uid = user.uid,
                        email = user.email,
                        photoUrl = user.photoURL,
                        provider = GOOGLE_PROVIDER,
                        updatedAt = serverTimestamp,
                        lastLoginAt = serverTimestamp,
                    ),
                encodeDefaults = true,
                merge = true,
            )
        } else {
            userDocument.set(
                strategy = UserProfileDocument.serializer(),
                data =
                    UserProfileDocument(
                        uid = user.uid,
                        email = user.email,
                        nickname = null,
                        photoUrl = user.photoURL,
                        provider = GOOGLE_PROVIDER,
                        createdAt = serverTimestamp,
                        updatedAt = serverTimestamp,
                        lastLoginAt = serverTimestamp,
                        onboardingCompleted = false,
                    ),
                encodeDefaults = true,
            )
        }
    }

    private companion object {
        private const val USERS_COLLECTION = "users"
        private const val GOOGLE_PROVIDER = "google.com"
    }
}
