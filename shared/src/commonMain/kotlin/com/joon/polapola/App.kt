package com.joon.polapola

import androidx.compose.runtime.Composable
import com.joon.polapola.presentation.navigation.AppNavHost
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        AppNavHost()
    }
}
