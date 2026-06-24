package com.sayanthrock.freeairock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CharcoalDark = Color(0xFF121212)
private val CharcoalLight = Color(0xFF1A1A1A)
private val PureWhite = Color(0xFFFFFFFF)
private val OffWhite = Color(0xFFF5F5F5)

private val DarkColors = darkColorScheme(
    primary = OffWhite,
    background = CharcoalDark,
    surface = CharcoalLight,
    onPrimary = CharcoalDark,
    onBackground = OffWhite,
    onSurface = OffWhite
)

private val LightColors = lightColorScheme(
    primary = CharcoalDark,
    background = PureWhite,
    surface = OffWhite,
    onPrimary = PureWhite,
    onBackground = CharcoalDark,
    onSurface = CharcoalDark
)

@Composable
fun FreeAiRockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
