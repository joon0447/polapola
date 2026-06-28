package com.joon.polapola.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joon.polapola.data.auth.AuthSessionRepository
import com.joon.polapola.presentation.home.HomeScreen
import com.joon.polapola.presentation.login.LoginScreen
import com.joon.polapola.presentation.splash.SplashScreen
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(onGoogleLoginClick: (() -> Unit) -> Unit = { onLoginSucceeded -> onLoginSucceeded() }) {
    val navController = rememberNavController()
    val authSessionRepository = remember { AuthSessionRepository() }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
    ) {
        composable<SplashRoute> {
            SplashScreen(
                onSplashFinished = {
                    coroutineScope.launch {
                        val route =
                            if (authSessionRepository.getSignedInUser() == null) {
                                LoginRoute
                            } else {
                                HomeRoute
                            }

                        navController.navigate(route) {
                            popUpTo<SplashRoute> {
                                inclusive = true
                            }
                        }
                    }
                },
            )
        }
        composable<LoginRoute> {
            LoginScreen(
                onGoogleLoginClick = {
                    onGoogleLoginClick {
                        navController.navigate(HomeRoute) {
                            popUpTo<LoginRoute> {
                                inclusive = true
                            }
                        }
                    }
                },
            )
        }
        composable<HomeRoute> {
            HomeScreen()
        }
    }
}

@Preview
@Composable
private fun AppNavHostPreview() {
    AppTheme {
        AppNavHost()
    }
}
