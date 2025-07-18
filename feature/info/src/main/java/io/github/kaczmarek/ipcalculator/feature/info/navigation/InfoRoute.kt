package io.github.kaczmarek.ipcalculator.feature.info.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.feature.info.screen.InfoRoute

const val INFO_ROUTE = "info"

fun NavGraphBuilder.infoScreen(
    layoutType: LayoutType,
    onOpenLink: (AppLinkType) -> Unit,
    onShareText: (String) -> Unit,
    onOpenStore: () -> Unit,
) {
    composable(route = INFO_ROUTE) {
        InfoRoute(
            layoutType = layoutType,
            onShareText = onShareText,
            onOpenLink = onOpenLink,
            onOpenStore = onOpenStore,
        )
    }
}