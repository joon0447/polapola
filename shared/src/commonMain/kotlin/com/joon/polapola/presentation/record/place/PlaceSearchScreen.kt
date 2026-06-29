package com.joon.polapola.presentation.record.place

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.data.place.DatePlace
import com.joon.polapola.data.place.KakaoLocalApiClient
import com.joon.polapola.data.place.KakaoLocalApiConfig
import com.joon.polapola.presentation.record.components.RecordArrowLeftIcon
import com.joon.polapola.presentation.record.components.RecordLocationIcon
import com.joon.polapola.presentation.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun PlaceSearchScreen(
    onBackClick: () -> Unit,
    onPlaceSelected: (DatePlace) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val kakaoLocalApiClient = remember { KakaoLocalApiClient() }
    var query by remember { mutableStateOf("") }
    var places by remember { mutableStateOf(emptyList<DatePlace>()) }
    var isSearching by remember { mutableStateOf(false) }
    var searched by remember { mutableStateOf(false) }
    val isKakaoApiKeyMissing = KakaoLocalApiConfig.restApiKey.isBlank()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        PlaceSearchHeader(onBackClick = onBackClick)
        PlaceSearchInput(
            value = query,
            enabled = !isKakaoApiKeyMissing,
            onValueChange = { query = it },
            onSearchClick = {
                if (query.isBlank() || isSearching) return@PlaceSearchInput

                coroutineScope.launch {
                    isSearching = true
                    searched = true
                    places = kakaoLocalApiClient.searchPlaces(query = query.trim())
                    isSearching = false
                }
            },
        )
        when {
            isKakaoApiKeyMissing -> PlaceSearchEmptyMessage(text = "Kakao REST API 키를 설정하면 장소를 검색할 수 있어요.")
            isSearching -> PlaceSearchEmptyMessage(text = "장소를 찾고 있어요")
            searched && places.isEmpty() -> PlaceSearchEmptyMessage(text = "검색 결과가 없어요")
            !searched -> PlaceSearchEmptyMessage(text = "데이트 장소를 검색해 주세요")
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(places) { place ->
                        PlaceSearchResultItem(
                            place = place,
                            onClick = { onPlaceSelected(place) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceSearchHeader(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(46.dp),
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
            RecordArrowLeftIcon()
        }
        Text(
            text = "장소 검색",
            color = Color(0xFF1A1A1A),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun PlaceSearchInput(
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier =
                Modifier
                    .weight(1f)
                    .height(54.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFFFF9FC),
            border = BorderStroke(width = 1.dp, color = Color(0xFFFCE1EE)),
        ) {
            BasicTextField(
                value = value,
                enabled = enabled,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                textStyle =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF1A1A1A),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = "장소 이름을 입력해 주세요",
                                color = Color(0xFF9CA3AF),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
        Surface(
            modifier =
                Modifier
                    .height(54.dp)
                    .clickable(enabled = enabled, onClick = onSearchClick),
            shape = CircleShape,
            color = if (enabled) Color(0xFFFF4FB6) else Color(0xFFD1D5DB),
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "검색",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun PlaceSearchResultItem(
    place: DatePlace,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFF9FC),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFCE1EE)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = Color.White,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    RecordLocationIcon()
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = place.name,
                    color = Color(0xFF1A1A1A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = place.roadAddress ?: place.address.orEmpty(),
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun PlaceSearchEmptyMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color(0xFF9CA3AF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun PlaceSearchScreenPreview() {
    AppTheme {
        PlaceSearchScreen(
            onBackClick = {},
            onPlaceSelected = {},
        )
    }
}
