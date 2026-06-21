package com.joon.polapola.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joon.polapola.presentation.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import polapola.shared.generated.resources.Res
import polapola.shared.generated.resources.apple_button
import polapola.shared.generated.resources.google_button
import polapola.shared.generated.resources.kakao_button

@Composable
fun LoginButtons(
    onKakaoClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onAppleClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.apple_button),
            contentDescription = "Apple login",
            modifier =
                Modifier
                    .size(64.dp)
                    .clickable(onClick = onAppleClick),
        )
        Image(
            painter = painterResource(Res.drawable.kakao_button),
            contentDescription = "Kakao login",
            modifier =
                Modifier
                    .size(64.dp)
                    .clickable(onClick = onKakaoClick),
        )
        Image(
            painter = painterResource(Res.drawable.google_button),
            contentDescription = "Google login",
            modifier =
                Modifier
                    .size(64.dp)
                    .clickable(onClick = onGoogleClick),
        )
    }
}

@Preview
@Composable
private fun LoginButtonsPreview() {
    AppTheme {
        LoginButtons()
    }
}
