package com.joon.polapola.data.auth

import com.joon.polapola.domain.auth.AuthenticatedUser
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

class GoogleAuthRepository {
    suspend fun signInWithGoogle(
        idToken: String,
        accessToken: String? = null,
    ): AuthenticatedUser {
        val credential =
            GoogleAuthProvider.credential(
                idToken = idToken,
                accessToken = accessToken,
            )
        val user =
            Firebase
                .auth
                .signInWithCredential(credential)
                .user ?: error("Firebase sign-in succeeded without a user.")
        upsertUserProfile(user)

        return AuthenticatedUser(
            uid = user.uid,
            displayName = user.displayName,
            email = user.email,
            photoUrl = user.photoURL,
        )
    }

    private suspend fun upsertUserProfile(user: FirebaseUser) {
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
