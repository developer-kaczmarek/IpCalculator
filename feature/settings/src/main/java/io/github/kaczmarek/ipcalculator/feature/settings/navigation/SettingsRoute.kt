package io.github.kaczmarek.ipcalculator.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.feature.settings.screen.SettingsRoute

const val SETTINGS_ROUTE = "settings"

fun NavGraphBuilder.settingsScreen(
    layoutType: LayoutType,
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute(
            layoutType = layoutType,
        )
    }
}