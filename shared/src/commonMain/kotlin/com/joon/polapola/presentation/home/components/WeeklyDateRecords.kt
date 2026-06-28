package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun WeeklyDateRecords() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(38.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "이번 주 데이트",
                    color = Color(0xFF2A1B24),
                    fontSize = 20.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "우리 둘이 남긴 기록 2개",
                    color = Color(0xFF8B6879),
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        DateRecordCard(
            date = "6.9",
            day = "화",
            title = "꽃집 앞 산책",
            time = "오후 6:20",
            memo = "손잡고 골목을 걷다가 작은 꽃집 앞에서 오래 웃었어요.",
            chips = listOf("산책", "꽃집"),
            backgroundColor = Color.White,
            pillColor = Color(0xFFFFF1F5),
        )
        DateRecordCard(
            date = "6.12",
            day = "금",
            title = "노을 보던 저녁",
            time = "오후 7:10",
            memo = "분홍 노을 보면서 김밥이랑 딸기라떼를 나눠 먹은 저녁.",
            chips = listOf("노을", "저녁"),
            backgroundColor = Color(0xFFFFF7FA),
            pillColor = Color(0xFFFFEAF2),
        )
    }
}

@Composable
private fun DateRecordCard(
    date: String,
    day: String,
    title: String,
    time: String,
    memo: String,
    chips: List<String>,
    backgroundColor: Color,
    pillColor: Color,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(86.dp),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border = BorderStroke(width = 1.dp, color = Color(0xFFF2D7E2)),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier =
                    Modifier
                        .width(52.dp)
                        .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                color = pillColor,
                border = BorderStroke(width = 1.dp, color = Color(0xFFF5C8D7)),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp, Alignment.CenterVertically),
                ) {
                    Text(
                        text = date,
                        color = Color(0xFFD65B87),
                        fontSize = 21.sp,
                        lineHeight = 19.sp,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = day,
                        color = Color(0xFF9B6A7C),
                        fontSize = 15.sp,
                        lineHeight = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        color = Color(0xFF2A1B24),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = time,
                        color = Color(0xFF9B6A7C),
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    text = memo,
                    color = Color(0xFF76646C),
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                    style = MaterialTheme.typography.bodySmall,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    chips.forEach { chip ->
                        DateRecordChip(text = chip)
                    }
                }
            }
        }
    }
}

@Composable
private fun DateRecordChip(text: String) {
    Surface(
        shape = CircleShape,
        color = Color(0xFFFFEEF5),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 3.dp),
            color = Color(0xFFD65B87),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun WeeklyDateRecordsPreview() {
    AppTheme {
        WeeklyDateRecords()
    }
}
