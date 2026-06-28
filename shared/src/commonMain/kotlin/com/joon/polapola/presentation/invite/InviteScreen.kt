package com.joon.polapola.presentation.invite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun InviteScreen(
    inviteCode: String,
    onBackClick: () -> Unit = {},
    onCopyCodeClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InviteTopBar(onBackClick = onBackClick)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .width(353.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text =
                        "방을 만들었어요!\n" +
                            "아래 코드를 상대에게 공유해주세요.\n" +
                            "상대가 코드를 입력하면 방에 참가할 수 있어요.\n" +
                            "초대 코드는 홈에서 언제든지 확인할 수 있어요.",
                    modifier =
                        Modifier
                            .width(341.dp)
                            .height(126.dp),
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
                InviteCodeDisplay(inviteCode = inviteCode)
                CopyCodeButton(onClick = { onCopyCodeClick(inviteCode) })
            }
        }
        Surface(
            modifier =
                Modifier
                    .width(297.dp)
                    .height(48.dp)
                    .clickable(onClick = onHomeClick),
            shape = RoundedCornerShape(15.dp),
            color = Color(0xFFFF4FB6),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "홈으로 이동하기",
                    modifier = Modifier.width(297.dp),
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun InviteTopBar(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            ArrowLeftIcon()
        }
        Text(
            text = "초대 코드",
            color = Color(0xFF111827),
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun InviteCodeDisplay(inviteCode: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFDDF1),
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            inviteCode
                .padEnd(length = INVITE_CODE_LENGTH, padChar = ' ')
                .take(INVITE_CODE_LENGTH)
                .forEach { codeCharacter ->
                    InviteCodeCell(codeCharacter = codeCharacter)
                }
        }
    }
}

@Composable
private fun InviteCodeCell(codeCharacter: Char) {
    Surface(
        modifier =
            Modifier
                .width(44.dp)
                .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = codeCharacter.toString(),
                color = Color.Black,
                fontSize = 22.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun CopyCodeButton(onClick: () -> Unit) {
    Surface(
        modifier =
            Modifier
                .width(307.dp)
                .height(48.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(width = 1.dp, color = Color(0xDD808080)),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CopyIcon()
            Text(
                text = "코드 복사하기",
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun ArrowLeftIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(start = point24(19f, 12f), end = point24(5f, 12f), stroke = stroke)
        drawIconLine(start = point24(12f, 5f), end = point24(5f, 12f), stroke = stroke)
        drawIconLine(start = point24(5f, 12f), end = point24(12f, 19f), stroke = stroke)
    }
}

@Composable
private fun CopyIcon() {
    Canvas(modifier = Modifier.size(16.dp)) {
        val stroke = Stroke(width = 1.33.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = Color.Black,
            topLeft = point16(5.33f, 5.33f),
            size = Size(width = size.width * 9.33f / 16f, height = size.height * 9.33f / 16f),
            cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx()),
            style = stroke,
        )
        drawRoundRect(
            color = Color.Black,
            topLeft = point16(1.33f, 1.33f),
            size = Size(width = size.width * 9.33f / 16f, height = size.height * 9.33f / 16f),
            cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx()),
            style = stroke,
        )
    }
}

private fun DrawScope.point24(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 24f, y = size.height * y / 24f)

private fun DrawScope.point16(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 16f, y = size.height * y / 16f)

private fun DrawScope.drawIconLine(
    start: Offset,
    end: Offset,
    stroke: Stroke,
) {
    drawLine(
        color = Color(0xFF374151),
        start = start,
        end = end,
        strokeWidth = stroke.width,
        cap = stroke.cap,
    )
}

private const val INVITE_CODE_LENGTH = 6

@Preview
@Composable
private fun InviteScreenPreview() {
    AppTheme {
        InviteScreen(inviteCode = "AB12C3")
    }
}
