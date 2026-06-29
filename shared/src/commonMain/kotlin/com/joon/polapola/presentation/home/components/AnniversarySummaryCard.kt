package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import polapola.shared.generated.resources.Res
import polapola.shared.generated.resources.home_room_image

@Composable
fun AnniversarySummaryCard(
    relationshipDayCount: Int?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(155.dp),
        shape = RoundedCornerShape(15.dp),
        color = Color.White,
        border = BorderStroke(width = 1.dp, color = Color(0xFFBDBDBD)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp,
                        horizontal = 15.dp,
                    ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.width(208.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Column(
                    modifier =
                        Modifier.width(169.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "처음 만난 날로부터",
                        modifier = Modifier.width(243.dp),
                        color = Color(0xFF808080),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = relationshipDayCount?.let { "D+ $it" } ?: "D+ -",
                        color = Color.Black,
                        fontSize = 32.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Text(
                    text = "이번 주도 함께 기록을 쌓아봐요 ",
                    modifier = Modifier.width(342.dp),
                    color = Color(0xFF808080),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Image(
                painter = painterResource(Res.drawable.home_room_image),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(width = 120.dp, height = 125.dp)
                        .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
private fun AnniversarySummaryCardPreview() {
    AppTheme {
        AnniversarySummaryCard(relationshipDayCount = 1000)
    }
}
