package dev.lucasangelo.cgc1

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE0E0E0),
    onPrimary = Color(0xFF1C1B1F),
    primaryContainer = Color(0xFF121212),
    onPrimaryContainer = Color(0xFFF2F2F2),
    secondary = Color(0xFFB8B8B8),
    onSecondary = Color(0xFF1C1B1F),
    secondaryContainer = Color(0xFF3F3F3F),
    onSecondaryContainer = Color(0xFFF2F2F2),
    tertiary = Color(0xFF9E9E9E),
    onTertiary = Color(0xFF1C1B1F),
    tertiaryContainer = Color(0xFF363636),
    onTertiaryContainer = Color(0xFFF2F2F2),
    background = Color(0xFF000000),
    onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF2B2B2B),
    onSurfaceVariant = Color(0xFFC7C7C7),
    outline = Color(0xFF8A8A8A),
)

@Composable
fun CGC1Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}