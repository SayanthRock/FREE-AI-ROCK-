package com.sayanthrock.freeairock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CharcoalDark = Color(0xFF121212)
val CharcoalLight = Color(0xFF1E1E1E)
val PureWhite = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFF5F5F5)

// Glassmorphic translucent colors
val GlassDarkSurface = Color(0x661E1E1E) // 40% opacity
val GlassLightSurface = Color(0x66F5F5F5) // 40% opacity

private val DarkColors = darkColorScheme(
    primary = PureWhite,
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
