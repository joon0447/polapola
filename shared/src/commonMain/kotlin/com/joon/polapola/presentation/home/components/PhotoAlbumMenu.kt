package com.joon.polapola.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
fun PhotoAlbumMenu(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(76.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFF7FB),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFFD1E7)),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                color = Color.White,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    PhotoLibraryIcon()
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "사진첩",
                    color = Color(0xFF1A1A1A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "우리의 데이트 사진이 아직 없어요!",
                    color = Color(0xFF4B5563),
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            ChevronRightIcon()
        }
    }
}

@Composable
private fun PhotoLibraryIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 1.9.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRectLine(point(5f, 6f), point(19f, 18f), stroke, Color(0xFFFF4FB6))
        drawCircle(color = Color(0xFFFF4FB6), radius = 2.dp.toPx(), center = point(9f, 10f))
        val mountain =
            Path().apply {
                moveTo(point(7f, 17f).x, point(7f, 17f).y)
                lineTo(point(11f, 13f).x, point(11f, 13f).y)
                lineTo(point(14f, 16f).x, point(14f, 16f).y)
                lineTo(point(16f, 14f).x, point(16f, 14f).y)
                lineTo(point(19f, 17f).x, point(19f, 17f).y)
            }
        drawPath(mountain, color = Color(0xFFFF4FB6), style = stroke)
    }
}

@Composable
private fun ChevronRightIcon() {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(point(8f, 5f), point(14f, 11f), stroke, Color(0xFF9CA3AF))
        drawIconLine(point(14f, 11f), point(8f, 17f), stroke, Color(0xFF9CA3AF))
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
private fun PhotoAlbumMenuPreview() {
    AppTheme {
        PhotoAlbumMenu()
    }
}
