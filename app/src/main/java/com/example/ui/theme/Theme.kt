package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HetashreeDarkColorScheme = darkColorScheme(
    primary = HetashreeGoldPrimary,
    onPrimary = HetashreeBlack,
    primaryContainer = HetashreeGoldDark,
    onPrimaryContainer = HetashreeGoldBright,
    secondary = HetashreeGoldBright,
    onSecondary = HetashreeBlack,
    secondaryContainer = HetashreeCardElevated,
    onSecondaryContainer = HetashreeGoldLight,
    tertiary = HetashreeGoldAmber,
    onTertiary = HetashreeBlack,
    background = HetashreeBlack,
    onBackground = HetashreeTextPrimary,
    surface = HetashreeDarkSurface,
    onSurface = HetashreeTextPrimary,
    surfaceVariant = HetashreeCard,
    onSurfaceVariant = HetashreeTextSecondary,
    outline = HetashreeBorder,
    outlineVariant = HetashreeTextMuted,
    error = HetashreeError,
    onError = Color.White
)

@Composable
fun HetashreeStarTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HetashreeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
