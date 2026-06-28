package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun HomeActionButtons(
    onJoinWithInviteCodeClick: () -> Unit = {},
    onCreateRoomClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.width(297.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        HomeActionButton(
            text = "초대 코드로 가입하기",
            backgroundColor = Color(0xFFFF4FB6),
            contentColor = Color.White,
            onClick = onJoinWithInviteCodeClick,
        )
        HomeActionButton(
            text = "새로운 방 만들기",
            backgroundColor = Color.White,
            contentColor = Color.Black,
            border = BorderStroke(width = 1.dp, color = Color(0xFFBDBDBD)),
            onClick = onCreateRoomClick,
        )
    }
}

@Composable
private fun HomeActionButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    border: BorderStroke? = null,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(49.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        color = backgroundColor,
        border = border,
    ) {
        Column(
            modifier = Modifier.background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = text,
                color = contentColor,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun HomeActionButtonsPreview() {
    AppTheme {
        HomeActionButtons()
    }
}
