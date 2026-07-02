package com.joon.polapola.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun MyPageScreen(
    email: String = "",
    appVersion: String = "v1.0.0",
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        Spacer(modifier = Modifier.height(19.dp))
        AccountCard(email = email.ifBlank { "로그인 정보를 불러오는 중..." })
        Spacer(modifier = Modifier.height(30.dp))
        SettingsMenu(
            appVersion = appVersion,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onTermsClick = onTermsClick,
        )
        Spacer(modifier = Modifier.height(26.dp))
        AccountActions(
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick,
        )
    }
}

@Composable
private fun AccountCard(email: String) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(73.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFFFF7FB),
        border = BorderStroke(width = 1.dp, color = Color(0xFFE5E7EB)),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "로그인한 이메일",
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = email,
                color = Color(0xFF1A1A1A),
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SettingsMenu(
    appVersion: String,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(244.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFFFF8FA),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
        ) {
            SettingsRow(
                label = "개인정보처리방침",
                icon = { ShieldCheckIcon() },
                shape =
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp,
                    ),
                trailing = { ChevronRightIcon(color = Color(0xFFD2A2B1), modifier = Modifier.size(18.dp)) },
                onClick = onPrivacyPolicyClick,
            )
            SettingsRow(
                label = "서비스 이용약관",
                icon = { FileTextIcon() },
                trailing = { ChevronRightIcon(color = Color(0xFFD2A2B1), modifier = Modifier.size(18.dp)) },
                onClick = onTermsClick,
            )
            SettingsRow(
                label = "앱 버전",
                icon = { SmartphoneIcon() },
                shape =
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp,
                    ),
                trailing = {
                    Text(
                        text = appVersion,
                        color = Color(0xFFB98598),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = null,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(73.dp)
                .background(color = Color(0xCCFFFFFF), shape = shape)
                .padding(horizontal = 14.dp)
                .then(
                    if (onClick == null) {
                        Modifier
                    } else {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick,
                        )
                    },
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconBubble(icon = icon)
        Text(
            text = label,
            color = Color(0xFF4A3038),
            fontSize = 20.sp,
            lineHeight = 22.sp,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        trailing()
    }
}

@Composable
private fun IconBubble(icon: @Composable () -> Unit) {
    Box(
        modifier =
            Modifier
                .size(38.dp)
                .background(color = Color(0xFFFFF0F5), shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        icon()
    }
}

@Composable
private fun AccountActions(
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(118.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(width = 1.dp, color = Color(0xFFE5E7EB)),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AccountActionRow(
                label = "로그아웃",
                backgroundColor = Color(0xFFF3F4F6),
                contentColor = Color(0xFF1A1A1A),
                iconColor = Color(0xFF4B5563),
                modifier = Modifier.weight(1f),
                onClick = onLogoutClick,
            ) {
                LogoutIcon(color = Color(0xFF4B5563))
            }
            AccountActionRow(
                label = "회원탈퇴",
                backgroundColor = Color(0xFFFFF4F4),
                contentColor = Color(0xFFD94A4A),
                iconColor = Color(0xFFD94A4A),
                modifier = Modifier.weight(1f),
                onClick = onWithdrawClick,
            ) {
                PersonRemoveIcon(color = Color(0xFFD94A4A))
            }
        }
    }
}

@Composable
private fun AccountActionRow(
    label: String,
    backgroundColor: Color,
    contentColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = backgroundColor, shape = RoundedCornerShape(14.dp))
                .padding(horizontal = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Text(
                text = label,
                color = contentColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        ChevronRightIcon(color = iconColor.copy(alpha = if (label == "로그아웃") 0.55f else 1f), modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun ShieldCheckIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(19.dp)) {
        val stroke = Stroke(width = 1.7.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val path =
            Path().apply {
                moveTo(point(12f, 3f).x, point(12f, 3f).y)
                lineTo(point(19f, 6f).x, point(19f, 6f).y)
                lineTo(point(17f, 15f).x, point(17f, 15f).y)
                lineTo(point(12f, 21f).x, point(12f, 21f).y)
                lineTo(point(7f, 15f).x, point(7f, 15f).y)
                lineTo(point(5f, 6f).x, point(5f, 6f).y)
                close()
            }
        drawPath(path = path, color = Color(0xFFD98AA2), style = stroke)
        drawIconLine(point(8.5f, 12f), point(11f, 14.5f), stroke, Color(0xFFD98AA2))
        drawIconLine(point(11f, 14.5f), point(16f, 9f), stroke, Color(0xFFD98AA2))
    }
}

@Composable
private fun FileTextIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(19.dp)) {
        val stroke = Stroke(width = 1.7.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = Color(0xFFD98AA2),
            topLeft = point(6f, 3f),
            size = Size(width = size.width * 12f / 24f, height = size.height * 18f / 24f),
            style = stroke,
        )
        drawIconLine(point(9f, 9f), point(15f, 9f), stroke, Color(0xFFD98AA2))
        drawIconLine(point(9f, 13f), point(15f, 13f), stroke, Color(0xFFD98AA2))
        drawIconLine(point(9f, 17f), point(13f, 17f), stroke, Color(0xFFD98AA2))
    }
}

@Composable
private fun SmartphoneIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(19.dp)) {
        val stroke = Stroke(width = 1.7.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = Color(0xFFD98AA2),
            topLeft = point(8f, 3f),
            size = Size(width = size.width * 8f / 24f, height = size.height * 18f / 24f),
            style = stroke,
        )
        drawCircle(color = Color(0xFFD98AA2), radius = 0.8.dp.toPx(), center = point(12f, 18f))
    }
}

@Composable
private fun LogoutIcon(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Canvas(modifier = modifier.size(20.dp)) {
        val stroke = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(point(10f, 6f), point(6f, 6f), stroke, color)
        drawIconLine(point(6f, 6f), point(6f, 18f), stroke, color)
        drawIconLine(point(6f, 18f), point(10f, 18f), stroke, color)
        drawIconLine(point(11f, 12f), point(20f, 12f), stroke, color)
        drawIconLine(point(17f, 9f), point(20f, 12f), stroke, color)
        drawIconLine(point(20f, 12f), point(17f, 15f), stroke, color)
    }
}

@Composable
private fun PersonRemoveIcon(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Canvas(modifier = modifier.size(20.dp)) {
        val stroke = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawCircle(color = color, radius = 3.5.dp.toPx(), center = point(9f, 7f), style = stroke)
        val path =
            Path().apply {
                moveTo(point(3f, 19f).x, point(3f, 19f).y)
                cubicTo(
                    point(4.5f, 14.5f).x,
                    point(4.5f, 14.5f).y,
                    point(13.5f, 14.5f).x,
                    point(13.5f, 14.5f).y,
                    point(15f, 19f).x,
                    point(15f, 19f).y,
                )
            }
        drawPath(path = path, color = color, style = stroke)
        drawIconLine(point(16f, 11f), point(22f, 11f), stroke, color)
    }
}

@Composable
private fun ChevronRightIcon(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Canvas(modifier = modifier.size(18.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(point(9f, 6f), point(15f, 12f), stroke, color)
        drawIconLine(point(15f, 12f), point(9f, 18f), stroke, color)
    }
}

private fun DrawScope.point(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 24f, y = size.height * y / 24f)

private fun DrawScope.drawIconLine(
    start: Offset,
    end: Offset,
    stroke: Stroke,
    color: Color,
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = stroke.width,
        cap = stroke.cap,
    )
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    AppTheme {
        Box(modifier = Modifier.padding(horizontal = 25.dp)) {
            MyPageScreen(email = "minseo.love@example.com")
        }
    }
}
