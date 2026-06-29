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
import androidx.compose.runtime.LaunchedEffect
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
import com.joon.polapola.data.auth.AuthSessionRepository
import com.joon.polapola.data.record.DateRecordRepository
import com.joon.polapola.data.record.RecordPhotoAlbumSummary
import com.joon.polapola.data.room.RoomRepository
import com.joon.polapola.presentation.components.BottomNavigationBar
import com.joon.polapola.presentation.components.PolaBottomNavigationTab
import com.joon.polapola.presentation.home.HomeScreen
import com.joon.polapola.presentation.navigation.route.HomeRoute
import com.joon.polapola.presentation.navigation.route.MyPageRoute
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun MainScaffold(
    photoAlbumRefreshKey: Int = 0,
    onCreateRoomClick: () -> Unit = {},
    onJoinWithInviteCodeClick: () -> Unit = {},
    onRecordClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    val authSessionRepository = remember { AuthSessionRepository() }
    val roomRepository = remember { RoomRepository() }
    val dateRecordRepository = remember { DateRecordRepository() }
    var selectedTab by remember { mutableStateOf(PolaBottomNavigationTab.HOME) }
    var roomName by remember { mutableStateOf<String?>(null) }
    var relationshipDayCount by remember { mutableStateOf<Int?>(null) }
    var photoAlbumSummary by remember { mutableStateOf(RecordPhotoAlbumSummary(totalImageCount = 0, previewImageUrls = emptyList())) }

    LaunchedEffect(photoAlbumRefreshKey) {
        runCatching {
            val user = authSessionRepository.getSignedInUser() ?: return@runCatching null
            roomRepository.getRoomByMemberUid(uid = user.uid)
        }.onSuccess { room ->
            roomName = room?.name
            relationshipDayCount = room?.firstMetDate?.toRelationshipDayCount()
            photoAlbumSummary =
                room
                    ?.id
                    ?.let { id -> dateRecordRepository.getPhotoAlbumSummary(roomId = id) }
                    ?: RecordPhotoAlbumSummary(totalImageCount = 0, previewImageUrls = emptyList())
        }
    }

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
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabClick = { tab ->
                        if (tab == PolaBottomNavigationTab.RECORD) {
                            onRecordClick()
                        } else {
                            val route = tab.mainRoute ?: return@BottomNavigationBar

                            selectedTab = tab
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                    hasRoom = roomName != null,
                    roomName = roomName.orEmpty(),
                    relationshipDayCount = relationshipDayCount,
                    photoCount = photoAlbumSummary.totalImageCount,
                    photoPreviewUrls = photoAlbumSummary.previewImageUrls,
                    onJoinWithInviteCodeClick = onJoinWithInviteCodeClick,
                    onCreateRoomClick = onCreateRoomClick,
                )
            }
            composable<MyPageRoute> {
                MainPlaceholderContent(text = "마이페이지")
            }
        }
    }
}

private val PolaBottomNavigationTab.mainRoute: Any?
    get() =
        when (this) {
            PolaBottomNavigationTab.HOME -> HomeRoute
            PolaBottomNavigationTab.RECORD -> null
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

private fun String.toRelationshipDayCount(): Int? {
    val firstMetDate =
        runCatching {
            LocalDate.parse(this)
        }.getOrNull() ?: return null
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    return (firstMetDate.daysUntil(today) + 1).coerceAtLeast(1)
}

@Preview
@Composable
private fun MainScaffoldPreview() {
    AppTheme {
        MainScaffold()
    }
}
