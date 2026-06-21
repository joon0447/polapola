package com.joon.polapola.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.Font
import polapola.shared.generated.resources.Ownglyph
import polapola.shared.generated.resources.Res

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val appFontFamily = FontFamily(Font(Res.font.Ownglyph))

    MaterialTheme(
        typography = Typography().withFontFamily(appFontFamily),
        content = content,
    )
}

private fun Typography.withFontFamily(fontFamily: FontFamily): Typography =
    copy(
        displayLarge = displayLarge.withFontFamily(fontFamily),
        displayMedium = displayMedium.withFontFamily(fontFamily),
        displaySmall = displaySmall.withFontFamily(fontFamily),
        headlineLarge = headlineLarge.withFontFamily(fontFamily),
        headlineMedium = headlineMedium.withFontFamily(fontFamily),
        headlineSmall = headlineSmall.withFontFamily(fontFamily),
        titleLarge = titleLarge.withFontFamily(fontFamily),
        titleMedium = titleMedium.withFontFamily(fontFamily),
        titleSmall = titleSmall.withFontFamily(fontFamily),
        bodyLarge = bodyLarge.withFontFamily(fontFamily),
        bodyMedium = bodyMedium.withFontFamily(fontFamily),
        bodySmall = bodySmall.withFontFamily(fontFamily),
        labelLarge = labelLarge.withFontFamily(fontFamily),
        labelMedium = labelMedium.withFontFamily(fontFamily),
        labelSmall = labelSmall.withFontFamily(fontFamily),
    )

private fun TextStyle.withFontFamily(fontFamily: FontFamily): TextStyle =
    copy(fontFamily = fontFamily)

@Preview
@Composable
private fun AppThemePreview() {
    AppTheme {
        Text(text = "폴라폴라")
    }
}
