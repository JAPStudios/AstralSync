package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AstralDarkColorScheme = darkColorScheme(
    primary = SolarGold,
    onPrimary = CosmicBackground,
    primaryContainer = CosmicSurfaceVariant,
    onPrimaryContainer = SolarGoldLight,
    secondary = CelestialCyan,
    onSecondary = CosmicBackground,
    secondaryContainer = CosmicSurfaceVariant,
    onSecondaryContainer = CelestialCyan,
    tertiary = SolarAmber,
    onTertiary = CosmicBackground,
    background = CosmicBackground,
    onBackground = TextPrimary,
    surface = CosmicSurface,
    onSurface = TextPrimary,
    surfaceVariant = CosmicSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CosmicBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep immersive celestial palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AstralDarkColorScheme,
        typography = Typography,
        content = content
    )
}
