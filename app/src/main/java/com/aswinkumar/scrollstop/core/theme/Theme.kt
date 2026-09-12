package com.aswinkumar.scrollstop.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SageGreenPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = SageGreenContainer,
    onPrimaryContainer = OnSageGreenContainer,
    secondary = SageGreenLight,
    onSecondary = SurfaceLight,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = CharcoalMedium,
    tertiary = AccentCoral,
    background = WarmOffWhite,
    onBackground = CharcoalDark,
    surface = SurfaceLight,
    onSurface = CharcoalDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = CharcoalMedium,
    outline = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = SageGreenLight,
    onPrimary = BackgroundDark,
    primaryContainer = SageGreenDark,
    onPrimaryContainer = OnBackgroundDark,
    secondary = SageGreenPrimary,
    onSecondary = OnBackgroundDark,
    secondaryContainer = SurfaceVariantDark,
    onSecondaryContainer = OnBackgroundDark,
    tertiary = AccentCoral,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceDark,
    outline = OutlineDark
)

@Composable
fun ScrollStopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand Sage Green theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ScrollStopTypography,
        shapes = ScrollStopShapes,
        content = content
    )
}
