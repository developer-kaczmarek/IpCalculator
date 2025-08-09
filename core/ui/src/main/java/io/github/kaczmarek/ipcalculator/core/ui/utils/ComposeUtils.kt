package io.github.kaczmarek.ipcalculator.core.ui.utils

import android.content.res.Configuration
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.Dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.kaczmarek.ipcalculator.core.model.LayoutType

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
            isCompatWindowSize -> LayoutType.Compat
            isSpaciousWindowSize -> LayoutType.Spacious
            else -> LayoutType.Compat
        }
    }
}

@Stable
fun Modifier.testTagAsId(tag: String) = semantics(
    properties = {
        testTag = tag
        testTagsAsResourceId = true
    },
)