package com.joon.polapola.data.auth

import com.joon.polapola.domain.auth.AuthenticatedUser
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth

class GoogleAuthRepository {
    private val userProfileRepository = UserProfileRepository()

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
        userProfileRepository.upsertUserProfile(user)

        return user.toAuthenticatedUser()
    }
}
