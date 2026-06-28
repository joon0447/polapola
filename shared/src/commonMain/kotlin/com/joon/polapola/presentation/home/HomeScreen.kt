package com.joon.polapola.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joon.polapola.presentation.home.components.EmptyHomeContent
import com.joon.polapola.presentation.home.components.RoomHomeContent
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun HomeScreen(
    hasRoom: Boolean = true,
    onJoinWithInviteCodeClick: () -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
) {
    if (hasRoom) {
        RoomHomeContent()
    } else {
        EmptyHomeContent(
            onJoinWithInviteCodeClick = onJoinWithInviteCodeClick,
            onCreateRoomClick = onCreateRoomClick,
        )
    }
}

@Preview
@Composable
private fun RoomHomeScreenPreview() {
    AppTheme {
        HomeScreen(hasRoom = true)
    }
}

@Preview
@Composable
private fun EmptyHomeScreenPreview() {
    AppTheme {
        HomeScreen(hasRoom = false)
    }
}
