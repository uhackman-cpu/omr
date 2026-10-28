package com.dev.omr.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BluePrimary = Color(0xFF1565C0)
val GreenRight = Color(0xFF2E7D32)
val RedWrong = Color(0xFFC62828)
val GreyPending = Color(0xFF9E9E9E)
val InkDark = Color(0xFF1A1A1A)
val PaperWhite = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    background = PaperWhite,
    onBackground = InkDark,
    surface = PaperWhite,
    onSurface = InkDark
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0)
)

@Composable
fun OMRPracticeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
