package io.github.kaczmarek.ipcalculator.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    secondary = SecondaryDarkColor,
    onSecondary = Color.White,
    background = BackgroundDarkColor,
    surface = SurfaceDarkColor,
    onSurface = Color.White,
    outlineVariant = OutlineVariantDarkColor,
    secondaryContainer = SurfaceDarkColor,
    onSecondaryContainer = Color.White,
    onSurfaceVariant = OnSurfaceVariantDarkColor,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    secondary = SecondaryLightColor,
    onSecondary = Color.White,
    background = BackgroundLightColor,
    surface = SurfaceLightColor,
    onSurface = Color.Black,
    outlineVariant = OutlineVariantLightColor,
    onSecondaryContainer = Color.Black,
    secondaryContainer = SurfaceLightColor,
    onSurfaceVariant = OnSurfaceVariantLightColor,
)

@Composable
fun AppTheme(
    themeType: ThemeType = ThemeType.System,
    content: @Composable () -> Unit
) {
    val colorScheme = when(themeType) {
        ThemeType.System -> {
            if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        }
        ThemeType.Dark -> DarkColorScheme

        ThemeType.Light -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val isAppearanceLightBars = colorScheme == LightColorScheme
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = isAppearanceLightBars
            insetsController.isAppearanceLightNavigationBars = isAppearanceLightBars
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}