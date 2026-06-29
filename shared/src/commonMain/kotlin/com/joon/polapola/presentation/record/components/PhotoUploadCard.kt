package com.joon.polapola.presentation.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.imagepicker.PickedImage
import com.joon.polapola.presentation.imagepicker.toImageBitmapOrNull
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun PhotoUploadCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pickedImages: List<PickedImage> = emptyList(),
    maxImageCount: Int = 10,
) {
    val pagerState = rememberPagerState(pageCount = { pickedImages.size })

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(258.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFFFF4FA),
        border = BorderStroke(width = 1.5.dp, color = Color(0xFFFFD1E7)),
        shadowElevation = 8.dp,
    ) {
        if (pickedImages.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    val previewImage = pickedImages[page].bytes.toImageBitmapOrNull()

                    if (previewImage != null) {
                        Image(
                            bitmap = previewImage,
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(28.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        PhotoUploadPlaceholder(
                            title = "사진이 선택되었어요",
                            hint = pickedImages[page].fileName ?: "다시 선택하려면 눌러주세요",
                        )
                    }
                }
                PhotoCountBadge(
                    currentPage = pagerState.currentPage + 1,
                    imageCount = pickedImages.size,
                    maxImageCount = maxImageCount,
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 14.dp, end = 14.dp),
                )
                PhotoPageIndicators(
                    currentPage = pagerState.currentPage,
                    imageCount = pickedImages.size,
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp),
                )
            }
        } else {
            PhotoUploadPlaceholder(
                title = "사진을 추가해 주세요",
                hint = "앨범에서 최대 ${maxImageCount}장까지 선택하기",
            )
        }
    }
}

@Composable
private fun PhotoUploadPlaceholder(
    title: String,
    hint: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Surface(
            modifier = Modifier.size(88.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                RecordCameraIcon()
            }
        }
        Text(
            text = title,
            color = Color(0xFF1A1A1A),
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = hint,
            color = Color(0xFF9CA3AF),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun PhotoCountBadge(
    currentPage: Int,
    imageCount: Int,
    maxImageCount: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = Color(0x99000000),
    ) {
        Text(
            text = "$currentPage/$imageCount · 최대 $maxImageCount",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun PhotoPageIndicators(
    currentPage: Int,
    imageCount: Int,
    modifier: Modifier = Modifier,
) {
    if (imageCount <= 1) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(imageCount) { index ->
            Box(
                modifier =
                    Modifier
                        .size(if (index == currentPage) 7.dp else 5.dp)
                        .clip(CircleShape)
                        .background(if (index == currentPage) Color.White else Color(0x99FFFFFF)),
            )
        }
    }
}

@Preview
@Composable
private fun PhotoUploadCardPreview() {
    AppTheme {
        PhotoUploadCard(onClick = {})
    }
}
