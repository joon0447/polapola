package com.joon.polapola

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.joon.polapola.data.auth.GoogleAuthRepository
import com.joon.polapola.data.imagecache.initializeLocalImageCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleSignInScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val googleAuthRepository = GoogleAuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initializeLocalImageCache(applicationContext)

        setContent {
            App(
                onGoogleLoginClick = ::requestGoogleIdToken,
            )
        }
    }

    override fun onDestroy() {
        googleSignInScope.cancel()
        super.onDestroy()
    }

    private fun requestGoogleIdToken(onLoginSucceeded: () -> Unit) {
        googleSignInScope.launch {
            runCatching {
                googleAuthRepository.signInWithGoogle(
                    idToken = getGoogleIdToken(),
                )
            }.onSuccess { user ->
                Log.d(TAG, "Firebase sign-in succeeded: uid=${user.uid}, email=${user.email}")
                onLoginSucceeded()
            }.onFailure { throwable ->
                Log.w(TAG, "Google sign-in failed", throwable)
            }
        }
    }

    private suspend fun getGoogleIdToken(): String {
        val googleIdOption =
            GetGoogleIdOption
                .Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(false)
                .build()
        val request =
            GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build()

        val credential =
            CredentialManager
                .create(this)
                .getCredential(
                    context = this,
                    request = request,
                ).credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential
                .createFrom(credential.data)
                .idToken
        }

        throw IllegalStateException("Unexpected credential type: ${credential.javaClass.simpleName}")
    }

    private companion object {
        private const val TAG = "MainActivity"
    }
}

@Preview
@Composable
private fun appAndroidPreview() {
    App()
}
