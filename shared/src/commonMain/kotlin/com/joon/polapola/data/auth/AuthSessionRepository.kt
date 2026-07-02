package com.joon.polapola.data.auth

import com.joon.polapola.domain.auth.AuthenticatedUser
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

class AuthSessionRepository {
    private val userProfileRepository = UserProfileRepository()

    suspend fun getSignedInUser(): AuthenticatedUser? {
        val user = Firebase.auth.currentUser ?: return null
        userProfileRepository.upsertUserProfile(user)

        return user.toAuthenticatedUser()
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }
}
