package io.github.kaczmarek.ipcalculator.root.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.feature.calculator.navigation.CALCULATOR_ROUTE
import io.github.kaczmarek.ipcalculator.feature.calculator.navigation.calculatorScreen
import io.github.kaczmarek.ipcalculator.feature.info.navigation.infoScreen
import io.github.kaczmarek.ipcalculator.feature.settings.navigation.settingsScreen

@Composable
fun IpCalculatorNavGraph(
    layoutType: LayoutType,
    onShareText: (String) -> Unit,
    onOpenLink: (AppLinkType) -> Unit,
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = CALCULATOR_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = modifier,
    ) {
        calculatorScreen(
            layoutType = layoutType,
            onShareText = onShareText,
            onOpenLink = onOpenLink,
        )

        infoScreen(
            layoutType = layoutType,
            onOpenLink = onOpenLink,
            onShareText = onShareText,
            onOpenStore = onOpenStore,
        )

        settingsScreen(
            layoutType = layoutType,
            onOpenLink = onOpenLink,
        )
    }
}