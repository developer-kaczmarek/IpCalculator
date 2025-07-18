package io.github.kaczmarek.ipcalculator.root.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.navigation.CALCULATOR_ROUTE
import io.github.kaczmarek.ipcalculator.feature.info.navigation.INFO_ROUTE
import io.github.kaczmarek.ipcalculator.feature.settings.navigation.SETTINGS_ROUTE

enum class IpCalculatorDestination(
    val icon: ImageVector,
    @StringRes val titleTextId: Int,
    val route: String,
) {
    Calculator(
        icon = Icons.AutoMirrored.Filled.List,
        titleTextId = R.string.root_nav_calculator,
        route = CALCULATOR_ROUTE,
    ),
    Info(
        icon = Icons.Default.Info,
        titleTextId = R.string.root_nav_info,
        route = INFO_ROUTE,
    ),
    Settings(
        icon = Icons.Default.Settings,
        titleTextId = R.string.root_nav_settings,
        route = SETTINGS_ROUTE,
    ),
}

val ipCalculatorTabScreens = IpCalculatorDestination.entries
