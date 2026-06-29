package com.joon.polapola.presentation.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.theme.AppTheme

@Composable
fun RecordDateField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RecordFieldContainer(
        modifier = modifier.clickable(onClick = onClick),
        height = 64.dp,
        leadingIcon = { RecordCalendarIcon() },
        trailingIcon = { RecordChevronRightIcon() },
    ) {
        RecordFieldTexts(
            label = "데이트 날짜",
            value = value,
            placeholder = "날짜를 선택해 주세요",
        )
    }
}

@Composable
fun RecordTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 64.dp,
    singleLine: Boolean = true,
) {
    RecordFieldContainer(
        modifier = modifier,
        height = height,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = label,
                color = Color(0xFFFF7ABC),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelSmall,
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = singleLine,
                textStyle =
                    MaterialTheme.typography.bodyMedium.merge(
                        TextStyle(
                            color = Color(0xFFBDBDBD),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(0xFFBDBDBD),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

@Composable
private fun RecordFieldContainer(
    height: Dp,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(height),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFFFF9FC),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFCE1EE)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = Color.White,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    leadingIcon()
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
            trailingIcon()
        }
    }
}

@Composable
private fun RecordFieldTexts(
    label: String,
    value: String,
    placeholder: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = label,
            color = Color(0xFFFF7ABC),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = value.ifEmpty { placeholder },
            color = Color(0xFF1A1A1A),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun RecordDateFieldPreview() {
    AppTheme {
        RecordDateField(
            value = "2026. 06. 21 일요일",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun RecordTextFieldPreview() {
    AppTheme {
        RecordTextField(
            label = "장소",
            value = "성수동 작은 카페",
            placeholder = "장소를 입력해 주세요",
            onValueChange = {},
            leadingIcon = { RecordLocationIcon() },
            trailingIcon = { RecordChevronRightIcon() },
        )
    }
}
