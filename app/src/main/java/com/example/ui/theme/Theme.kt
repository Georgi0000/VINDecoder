package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = HighlightPrimary,
    onPrimary = Color.White,
    secondary = BrightHighlight,
    onSecondary = Color.White,
    background = DeepBlueDark,
    onBackground = TextPrimary,
    surface = SlateBlueSurface,
    onSurface = TextPrimary,
    surfaceVariant = ButtonNormal,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // Force our beautiful custom dark palette for perfect atmospheric lighting
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
