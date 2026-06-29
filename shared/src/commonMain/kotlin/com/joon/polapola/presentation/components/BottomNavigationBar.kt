package com.joon.polapola.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun BottomNavigationBar(
    selectedTab: PolaBottomNavigationTab,
    onTabClick: (PolaBottomNavigationTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .width(350.dp)
                .height(70.dp),
        shape = CircleShape,
        color = Color(0xCCFFFFFF),
        border = BorderStroke(width = 1.dp, color = Color(0xFFE5E7EB)),
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomNavTab(
                label = "홈",
                selected = selectedTab == PolaBottomNavigationTab.HOME,
                modifier = Modifier.weight(1f),
                onClick = { onTabClick(PolaBottomNavigationTab.HOME) },
            ) {
                HomeIcon(color = if (selectedTab == PolaBottomNavigationTab.HOME) Color(0xFFFF50B6) else Color(0xFF9CA3AF))
            }
            BottomNavTab(
                label = "기록",
                selected = selectedTab == PolaBottomNavigationTab.RECORD,
                prominent = true,
                modifier = Modifier.width(78.dp),
                onClick = { onTabClick(PolaBottomNavigationTab.RECORD) },
            ) {
                CameraAddIcon(color = Color.White)
            }
            BottomNavTab(
                label = "마이페이지",
                selected = selectedTab == PolaBottomNavigationTab.MY_PAGE,
                modifier = Modifier.weight(1f),
                onClick = { onTabClick(PolaBottomNavigationTab.MY_PAGE) },
            ) {
                PersonIcon(color = if (selectedTab == PolaBottomNavigationTab.MY_PAGE) Color(0xFFFF50B6) else Color(0xFF9CA3AF))
            }
        }
    }
}

enum class PolaBottomNavigationTab {
    HOME,
    RECORD,
    MY_PAGE,
}

@Composable
private fun BottomNavTab(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    prominent: Boolean = false,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    val contentColor =
        when {
            prominent -> Color.White
            selected -> Color(0xFFFC4DB4)
            else -> Color(0xFF9CA3AF)
        }
    val labelFontSize =
        when {
            prominent -> 16.sp
            else -> 14.sp
        }

    Surface(
        modifier =
            modifier
                .height(if (prominent) 58.dp else 54.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
        shape = CircleShape,
        color = if (prominent) Color(0xFFFF4FB6) else Color.Transparent,
        border = if (prominent) BorderStroke(width = 3.dp, color = Color.White) else null,
        shadowElevation = if (prominent) 6.dp else 0.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(if (prominent) 1.dp else 2.dp, Alignment.CenterVertically),
        ) {
            icon()
            Text(
                text = label,
                color = contentColor,
                fontSize = labelFontSize,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
private fun HomeIcon(color: Color) {
    Canvas(modifier = Modifier.size(21.dp)) {
        val stroke = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val path =
            Path().apply {
                moveTo(point(4f, 10f).x, point(4f, 10f).y)
                lineTo(point(10.5f, 4f).x, point(10.5f, 4f).y)
                lineTo(point(17f, 10f).x, point(17f, 10f).y)
                lineTo(point(17f, 17f).x, point(17f, 17f).y)
                lineTo(point(13f, 17f).x, point(13f, 17f).y)
                lineTo(point(13f, 12.5f).x, point(13f, 12.5f).y)
                lineTo(point(8f, 12.5f).x, point(8f, 12.5f).y)
                lineTo(point(8f, 17f).x, point(8f, 17f).y)
                lineTo(point(4f, 17f).x, point(4f, 17f).y)
                close()
            }
        drawPath(path, color = color, style = stroke)
    }
}

@Composable
private fun CameraAddIcon(color: Color) {
    Canvas(modifier = Modifier.size(25.dp)) {
        val stroke = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRectLine(point(4f, 7f), point(18f, 18f), stroke, color)
        drawCircle(color = color, radius = 3.dp.toPx(), center = point(11f, 12.5f), style = stroke)
        drawIconLine(point(19f, 5f), point(19f, 11f), stroke, color)
        drawIconLine(point(16f, 8f), point(22f, 8f), stroke, color)
    }
}

@Composable
private fun PersonIcon(color: Color) {
    Canvas(modifier = Modifier.size(21.dp)) {
        val stroke = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawCircle(color = color, radius = 3.5.dp.toPx(), center = point(10.5f, 7f), style = stroke)
        val path =
            Path().apply {
                moveTo(point(4f, 18f).x, point(4f, 18f).y)
                cubicTo(
                    point(5f, 13.5f).x,
                    point(5f, 13.5f).y,
                    point(16f, 13.5f).x,
                    point(16f, 13.5f).y,
                    point(17f, 18f).x,
                    point(17f, 18f).y,
                )
            }
        drawPath(path, color = color, style = stroke)
    }
}

private fun DrawScope.point(
    x: Float,
    y: Float,
) = Offset(x = size.width * x / 21f, y = size.height * y / 21f)

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

private fun DrawScope.drawRectLine(
    topLeft: Offset,
    bottomRight: Offset,
    stroke: Stroke,
    color: Color,
) {
    drawRoundRect(
        color = color,
        topLeft = topLeft,
        size =
            Size(
                width = bottomRight.x - topLeft.x,
                height = bottomRight.y - topLeft.y,
            ),
        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
        style = stroke,
    )
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    AppTheme {
        BottomNavigationBar(
            selectedTab = PolaBottomNavigationTab.HOME,
            onTabClick = {},
        )
    }
}
