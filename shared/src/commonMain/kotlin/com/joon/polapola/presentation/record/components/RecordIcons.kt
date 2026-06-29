package com.joon.polapola.presentation.record.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp

@Composable
fun RecordArrowLeftIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF374151),
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(start = point(19f, 12f), end = point(5f, 12f), stroke = stroke, color = color)
        drawIconLine(start = point(12f, 5f), end = point(5f, 12f), stroke = stroke, color = color)
        drawIconLine(start = point(5f, 12f), end = point(12f, 19f), stroke = stroke, color = color)
    }
}

@Composable
fun RecordCameraIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF4FB6),
) {
    Canvas(modifier = modifier.size(42.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = color,
            topLeft = point(4f, 8f),
            size = Size(width = size.width * 16f / 24f, height = size.height * 12f / 24f),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            style = stroke,
        )
        drawIconLine(start = point(8f, 8f), end = point(10f, 5f), stroke = stroke, color = color)
        drawIconLine(start = point(10f, 5f), end = point(14f, 5f), stroke = stroke, color = color)
        drawIconLine(start = point(14f, 5f), end = point(16f, 8f), stroke = stroke, color = color)
        drawCircle(color = color, radius = 3.8.dp.toPx(), center = point(12f, 14f), style = stroke)
    }
}

@Composable
fun RecordCalendarIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF4FB6),
) {
    Canvas(modifier = modifier.size(21.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = color,
            topLeft = point(4f, 5f),
            size = Size(width = size.width * 16f / 24f, height = size.height * 15f / 24f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke,
        )
        drawIconLine(start = point(8f, 3f), end = point(8f, 7f), stroke = stroke, color = color)
        drawIconLine(start = point(16f, 3f), end = point(16f, 7f), stroke = stroke, color = color)
        drawIconLine(start = point(4f, 10f), end = point(20f, 10f), stroke = stroke, color = color)
    }
}

@Composable
fun RecordLocationIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF4FB6),
) {
    Canvas(modifier = modifier.size(21.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val path =
            Path().apply {
                moveTo(point(12f, 21f).x, point(12f, 21f).y)
                cubicTo(
                    point(6f, 15f).x,
                    point(6f, 15f).y,
                    point(6f, 9f).x,
                    point(6f, 9f).y,
                    point(12f, 4f).x,
                    point(12f, 4f).y,
                )
                cubicTo(
                    point(18f, 9f).x,
                    point(18f, 9f).y,
                    point(18f, 15f).x,
                    point(18f, 15f).y,
                    point(12f, 21f).x,
                    point(12f, 21f).y,
                )
            }
        drawPath(path = path, color = color, style = stroke)
        drawCircle(color = color, radius = 2.5.dp.toPx(), center = point(12f, 10f), style = stroke)
    }
}

@Composable
fun RecordHeartIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF4FB6),
) {
    Canvas(modifier = modifier.size(21.dp)) {
        val path =
            Path().apply {
                moveTo(point(12f, 20f).x, point(12f, 20f).y)
                cubicTo(
                    point(5f, 15f).x,
                    point(5f, 15f).y,
                    point(4f, 10f).x,
                    point(4f, 10f).y,
                    point(7f, 7f).x,
                    point(7f, 7f).y,
                )
                cubicTo(
                    point(9f, 5f).x,
                    point(9f, 5f).y,
                    point(12f, 7f).x,
                    point(12f, 7f).y,
                    point(12f, 7f).x,
                    point(12f, 7f).y,
                )
                cubicTo(
                    point(12f, 7f).x,
                    point(12f, 7f).y,
                    point(15f, 5f).x,
                    point(15f, 5f).y,
                    point(17f, 7f).x,
                    point(17f, 7f).y,
                )
                cubicTo(
                    point(20f, 10f).x,
                    point(20f, 10f).y,
                    point(19f, 15f).x,
                    point(19f, 15f).y,
                    point(12f, 20f).x,
                    point(12f, 20f).y,
                )
                close()
            }
        drawPath(path = path, color = color)
    }
}

@Composable
fun RecordChevronRightIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFC4C4C4),
) {
    Canvas(modifier = modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(start = point(9f, 6f), end = point(15f, 12f), stroke = stroke, color = color)
        drawIconLine(start = point(15f, 12f), end = point(9f, 18f), stroke = stroke, color = color)
    }
}

@Composable
fun RecordEditIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFC4C4C4),
) {
    Canvas(modifier = modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawIconLine(start = point(5f, 18f), end = point(6.5f, 13f), stroke = stroke, color = color)
        drawIconLine(start = point(6.5f, 13f), end = point(15f, 4.5f), stroke = stroke, color = color)
        drawIconLine(start = point(15f, 4.5f), end = point(18.5f, 8f), stroke = stroke, color = color)
        drawIconLine(start = point(18.5f, 8f), end = point(10f, 16.5f), stroke = stroke, color = color)
        drawIconLine(start = point(10f, 16.5f), end = point(5f, 18f), stroke = stroke, color = color)
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
