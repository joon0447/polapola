package com.joon.polapola.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joon.polapola.presentation.login.components.LoginButtons
import com.joon.polapola.presentation.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import polapola.shared.generated.resources.Res
import polapola.shared.generated.resources.logo

@Composable
fun LoginScreen() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .offset(y = (-44).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(260.dp),
        )
        Spacer(modifier = Modifier.height(38.dp))
        Text(
            text = "우리만의 추억 간직하기",
            color = Color.Black,
            fontSize = 32.sp,
            lineHeight = 31.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "폴라폴라",
            color = Color(0xFF131A24),
            fontSize = 60.sp,
            lineHeight = 49.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(modifier = Modifier.height(32.dp))
        LoginButtons()
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}
