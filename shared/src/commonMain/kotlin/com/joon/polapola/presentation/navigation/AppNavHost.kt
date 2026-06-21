package com.joon.polapola.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joon.polapola.presentation.login.LoginScreen
import com.joon.polapola.presentation.splash.SplashScreen
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
    ) {
        composable<SplashRoute> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(LoginRoute) {
                        popUpTo<SplashRoute> {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<LoginRoute> {
            LoginScreen()
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
