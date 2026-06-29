package com.joon.polapola.presentation.photoalbum

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.joon.polapola.data.record.PhotoAlbumRecord
import com.joon.polapola.presentation.imagepicker.loadRemoteImageBytes
import com.joon.polapola.presentation.imagepicker.toImageBitmapOrNull
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.datetime.LocalDate

@Composable
fun PhotoAlbumScreen(
    records: List<PhotoAlbumRecord> = emptyList(),
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PhotoAlbumTopBar(onBackClick = onBackClick)
        PhotoAlbumHeader()
        if (records.isEmpty()) {
            EmptyPhotoAlbum(
                isLoading = isLoading,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentPadding = PaddingValues(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = records,
                    key = { record -> record.id },
                ) { record ->
                    PhotoAlbumRecordCard(record = record)
                }
            }
        }
    }
}

@Composable
private fun PhotoAlbumTopBar(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(46.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = Color.Transparent,
            onClick = onBackClick,
        ) {
            Box(contentAlignment = Alignment.Center) {
                BackIcon()
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "사진첩",
                color = Color(0xFF1A1A1A),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "우리의 데이트 순간을 모아봤어요",
                color = Color(0xFFFF7ABC),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Box(modifier = Modifier.size(36.dp))
    }
}

@Composable
private fun PhotoAlbumHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = "사진첩",
            color = Color(0xFF2B2025),
            fontSize = 34.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "우리의 데이트 순간을 모아봤어요",
            color = Color(0xFF9A6D7C),
            fontSize = 15.sp,
            lineHeight = 19.sp,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PhotoAlbumRecordCard(record: PhotoAlbumRecord) {
    val image = rememberRemoteImageBitmap(imageUrl = record.previewImageUrl)

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(171f / 126f),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFE6F4),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFFD1E7)),
        shadowElevation = 4.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                )
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops =
                                    arrayOf(
                                        0f to Color.Transparent,
                                        0.55f to Color.Transparent,
                                        1f to Color(0x99000000),
                                    ),
                            ),
                        ),
            )
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = record.memo.ifBlank { "함께한 데이트" },
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = record.date.toMonthDayText(),
                    color = Color(0xFFFFE7F0),
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
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
private fun EmptyPhotoAlbum(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text =
                if (isLoading) {
                    "사진첩을 불러오는 중이에요"
                } else {
                    "아직 저장된 데이트 사진이 없어요"
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
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawIconLine(point(14f, 5f), point(7f, 12f), stroke, Color(0xFF374151))
        drawIconLine(point(7f, 12f), point(14f, 19f), stroke, Color(0xFF374151))
        drawIconLine(point(7f, 12f), point(19f, 12f), stroke, Color(0xFF374151))
    }
}

private fun String.toMonthDayText(): String {
    val date =
        runCatching {
            LocalDate.parse(this)
        }.getOrNull() ?: return this

    return "${date.month.ordinal + 1}.${date.day}"
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
private fun PhotoAlbumScreenPreview() {
    AppTheme {
        PhotoAlbumScreen(
            records =
                listOf(
                    PhotoAlbumRecord(
                        id = "1",
                        date = "2026-06-18",
                        memo = "한강 피크닉",
                        previewImageUrl = "",
                    ),
                    PhotoAlbumRecord(
                        id = "2",
                        date = "2026-06-14",
                        memo = "성수 카페",
                        previewImageUrl = "",
                    ),
                    PhotoAlbumRecord(
                        id = "3",
                        date = "2026-06-09",
                        memo = "밤 산책",
                        previewImageUrl = "",
                    ),
                ),
        )
    }
}
