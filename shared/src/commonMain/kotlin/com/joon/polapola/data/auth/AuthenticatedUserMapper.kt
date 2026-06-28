package com.joon.polapola.data.auth

import com.joon.polapola.domain.auth.AuthenticatedUser
import dev.gitlive.firebase.auth.FirebaseUser

internal fun FirebaseUser.toAuthenticatedUser() =
    AuthenticatedUser(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoURL,
    )
