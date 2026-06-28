package com.joon.polapola.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.home.components.HomeActionButtons
import com.joon.polapola.presentation.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import polapola.shared.generated.resources.Res
import polapola.shared.generated.resources.camera_pola
import polapola.shared.generated.resources.logo

@Composable
fun HomeScreen(
    onJoinWithInviteCodeClick: () -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .offset(y = (-1).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.camera_pola),
            contentDescription = null,
            modifier = Modifier.size(width = 156.dp, height = 164.dp),
        )
        Spacer(modifier = Modifier.height(41.dp))
        Text(
            text = "우리의 기록을 시작해요",
            modifier = Modifier.width(297.dp),
            color = Color.Black,
            fontSize = 40.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "초대 코드를 받았다면 바로 참여하고, 아직 방이 없다면 새 커플 방을 만들어 데이트 기록을 남겨보세요.",
            modifier = Modifier.width(297.dp),
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(41.dp))
        HomeActionButtons(
            onJoinWithInviteCodeClick = onJoinWithInviteCodeClick,
            onCreateRoomClick = onCreateRoomClick,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen()
    }
}
