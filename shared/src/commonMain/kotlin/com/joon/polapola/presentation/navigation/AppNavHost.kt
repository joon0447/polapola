package com.joon.polapola.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joon.polapola.data.auth.AuthSessionRepository
import com.joon.polapola.data.room.RoomRepository
import com.joon.polapola.presentation.createroom.CreateRoomScreen
import com.joon.polapola.presentation.invite.InviteScreen
import com.joon.polapola.presentation.login.LoginScreen
import com.joon.polapola.presentation.splash.SplashScreen
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(onGoogleLoginClick: (() -> Unit) -> Unit = { onLoginSucceeded -> onLoginSucceeded() }) {
    val navController = rememberNavController()
    val authSessionRepository = remember { AuthSessionRepository() }
    val roomRepository = remember { RoomRepository() }
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
                                MainRoute
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
                        navController.navigate(MainRoute) {
                            popUpTo<LoginRoute> {
                                inclusive = true
                            }
                        }
                    }
                },
            )
        }
        composable<MainRoute> {
            MainScaffold(
                onCreateRoomClick = {
                    navController.navigate(CreateRoomRoute)
                },
            )
        }
        composable<CreateRoomRoute> {
            var isCreating by remember { mutableStateOf(false) }

            CreateRoomScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCreateRoomClick = { roomName, firstDate ->
                    if (isCreating) return@CreateRoomScreen

                    coroutineScope.launch {
                        isCreating = true
                        runCatching {
                            val user = authSessionRepository.getSignedInUser() ?: error("Signed-in user is required.")
                            roomRepository.createRoom(
                                name = roomName,
                                firstMetDate = firstDate,
                                ownerUid = user.uid,
                            )
                        }.onSuccess { createdRoom ->
                            navController.navigate(InviteRoute(inviteCode = createdRoom.inviteCode)) {
                                popUpTo<CreateRoomRoute> {
                                    inclusive = true
                                }
                            }
                        }
                        isCreating = false
                    }
                },
                isCreating = isCreating,
            )
        }
        composable<InviteRoute> { backStackEntry ->
            val inviteRoute = backStackEntry.toRoute<InviteRoute>()

            InviteScreen(
                inviteCode = inviteRoute.inviteCode,
                onBackClick = {
                    navController.popBackStack()
                },
                onHomeClick = {
                    navController.navigate(MainRoute) {
                        popUpTo<MainRoute> {
                            inclusive = true
                        }
                    }
                },
            )
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
