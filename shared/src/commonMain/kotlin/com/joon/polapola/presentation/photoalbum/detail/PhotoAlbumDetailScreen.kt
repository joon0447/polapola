package com.joon.polapola.presentation.photoalbum.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.data.record.PhotoAlbumDetailRecord
import com.joon.polapola.presentation.imagepicker.loadRemoteImageBytes
import com.joon.polapola.presentation.imagepicker.toImageBitmapOrNull
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.LocalDate

@Composable
fun PhotoAlbumDetailScreen(
    record: PhotoAlbumDetailRecord? = null,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8FB))
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        PhotoAlbumDetailHeader(onBackClick = onBackClick)
        if (record == null) {
            EmptyPhotoAlbumDetail(
                isLoading = isLoading,
                modifier = Modifier.weight(1f),
            )
        } else {
            PhotoAlbumDetailContent(
                record = record,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PhotoAlbumDetailHeader(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(42.dp),
            onClick = onBackClick,
        ) {
            Box(contentAlignment = Alignment.Center) {
                BackIcon()
            }
        }
        Text(
            text = "사진 상세",
            color = Color(0xFF1A1A1A),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge,
        )
        Surface(
            modifier = Modifier.size(42.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
//                MoreIcon()
            }
        }
    }
}

@Composable
private fun PhotoAlbumDetailContent(
    record: PhotoAlbumDetailRecord,
    modifier: Modifier = Modifier,
) {
    val imageUrls = record.imageUrls
    val pagerState = rememberPagerState(pageCount = { imageUrls.size.coerceAtLeast(1) })
    val currentPhotoNumber = (pagerState.currentPage + 1).toString().padStart(2, '0')
    val totalPhotoCount =
        imageUrls
            .size
            .coerceAtLeast(1)
            .toString()
            .padStart(2, '0')

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(318.dp),
                shape = RoundedCornerShape(30.dp),
                color = Color(0xFFFFEAF2),
                shadowElevation = 10.dp,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (imageUrls.isEmpty()) {
                        PhotoFallback()
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
                            RemotePhoto(
                                imageUrl = imageUrls[page],
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                    Surface(
                        modifier =
                            Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 18.dp, start = 18.dp),
                        shape = CircleShape,
                        color = Color(0xCCFFFFFF),
                    ) {
                        Text(
                            text = record.date.toFullDateText(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = Color(0xFFB45B78),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
            Text(
                text = "$currentPhotoNumber / $totalPhotoCount",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFFB45B78),
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(119.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(width = 1.dp, color = Color(0xFFF4D8E7)),
            shadowElevation = 1.dp,
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(76.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF8FB),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text(
                            text = record.placeName ?: "우리의 한 줄 메모",
                            color = Color(0xFF9CA3AF),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = record.memo.ifBlank { "함께한 순간을 기록했어요" },
                            color = Color(0xFF1A1A1A),
                            fontSize = 15.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RemotePhoto(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val image = rememberRemoteImageBitmap(imageUrl = imageUrl)

    Box(modifier = modifier.background(PhotoDetailGradient)) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun rememberRemoteImageBitmap(imageUrl: String): ImageBitmap? {
    var image by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        image =
            imageUrl
                .takeIf { url -> url.isNotBlank() }
                ?.let { url -> loadRemoteImageBytes(url)?.toImageBitmapOrNull() }
    }

    return image
}

@Composable
private fun PhotoFallback() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(PhotoDetailGradient),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "사진을 불러올 수 없어요",
            color = Color(0xFFB45B78),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun EmptyPhotoAlbumDetail(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text =
                if (isLoading) {
                    "사진을 불러오는 중이에요"
                } else {
                    "사진 기록을 찾을 수 없어요"
                },
            color = Color(0xFF9A6D7C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun BackIcon() {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawIconLine(point(14f, 5f), point(7f, 12f), stroke, Color(0xFF4B5563))
        drawIconLine(point(7f, 12f), point(14f, 19f), stroke, Color(0xFF4B5563))
        drawIconLine(point(7f, 12f), point(19f, 12f), stroke, Color(0xFF4B5563))
    }
}

@Composable
private fun MoreIcon() {
    Canvas(modifier = Modifier.size(22.dp)) {
        repeat(3) { index ->
            drawCircle(
                color = Color(0xFF9CA3AF),
                radius = 1.7.dp.toPx(),
                center = point(8f + index * 4f, 12f),
            )
        }
    }
}

private fun String.toFullDateText(): String {
    val date =
        runCatching {
            LocalDate.parse(this)
        }.getOrNull() ?: return this

    return "${date.year}.${(date.month.ordinal + 1).toString().padStart(2, '0')}.${date.day.toString().padStart(2, '0')}"
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

private val PhotoDetailGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFFFEAF2),
                Color(0xFFFFFDF8),
                Color(0xFFEAF7FF),
            ),
    )

@Preview
@Composable
private fun PhotoAlbumDetailScreenPreview() {
    AppTheme {
        PhotoAlbumDetailScreen(
            record =
                PhotoAlbumDetailRecord(
                    id = "1",
                    date = "2026-05-18",
                    memo = "둘이 같이 고른 딸기 케이크가 정말 맛있었던 날",
                    placeName = null,
                    imageUrls = listOf("", "", ""),
                ),
        )
    }
}
