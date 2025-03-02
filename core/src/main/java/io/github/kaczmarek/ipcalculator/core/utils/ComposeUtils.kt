package io.github.kaczmarek.ipcalculator.core.utils

import android.content.res.Configuration
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.kaczmarek.ipcalculator.core.model.layout.LayoutType

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun ColorScheme.isLight() = this.background.luminance() > 0.5

@Composable
fun isLandscapeOrientation(): Boolean {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    return orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun getLayoutType(adaptiveInfo: WindowAdaptiveInfo): LayoutType {
    return with(adaptiveInfo) {
        val isCompatWindowSize = windowPosture.isTabletop ||
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT

        val isSpaciousWindowSize =
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
                    windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM

        when {
            isLandscapeOrientation() -> LayoutType.SPACIOUS
            isCompatWindowSize -> LayoutType.COMPACT
            isSpaciousWindowSize -> LayoutType.SPACIOUS
            else -> LayoutType.COMPACT
        }
    }
}