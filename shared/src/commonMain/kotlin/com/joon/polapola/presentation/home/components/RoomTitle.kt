package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun RoomTitle(roomName: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
    ) {
        Text(
            text = roomName,
            color = Color.Black,
            fontSize = 32.sp,
            lineHeight = 20.sp,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoomTitlePreview() {
    AppTheme {
        RoomTitle(roomName = "민서의 공부방")
    }
}
