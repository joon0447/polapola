package com.joon.polapola.presentation.record.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun RecordSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "기록 저장하기",
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(enabled = enabled, onClick = onClick),
        shape = CircleShape,
        color = if (enabled) Color(0xFFFF4FB6) else Color(0xFFD1D5DB),
        shadowElevation = if (enabled) 10.dp else 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun RecordSaveButtonPreview() {
    AppTheme {
        RecordSaveButton(onClick = {})
    }
}
