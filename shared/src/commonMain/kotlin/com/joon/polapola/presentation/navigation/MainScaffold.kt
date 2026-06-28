package com.joon.polapola.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joon.polapola.presentation.components.PolaBottomNavigationBar
import com.joon.polapola.presentation.components.PolaBottomNavigationTab
import com.joon.polapola.presentation.home.HomeScreen
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun MainScaffold(
    onCreateRoomClick: () -> Unit = {},
    onJoinWithInviteCodeClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(PolaBottomNavigationTab.HOME) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                PolaBottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabClick = { tab ->
                        selectedTab = tab
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 25.dp),
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    onJoinWithInviteCodeClick = onJoinWithInviteCodeClick,
                    onCreateRoomClick = onCreateRoomClick,
                )
            }
            composable<RecordRoute> {
                MainPlaceholderContent(text = "기록")
            }
            composable<MyPageRoute> {
                MainPlaceholderContent(text = "마이페이지")
            }
        }
    }
}

private val PolaBottomNavigationTab.route: Any
    get() =
        when (this) {
            PolaBottomNavigationTab.HOME -> HomeRoute
            PolaBottomNavigationTab.RECORD -> RecordRoute
            PolaBottomNavigationTab.MY_PAGE -> MyPageRoute
        }

@Composable
private fun MainPlaceholderContent(text: String) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview
@Composable
private fun MainScaffoldPreview() {
    AppTheme {
        MainScaffold()
    }
}
