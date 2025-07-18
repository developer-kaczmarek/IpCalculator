package io.github.kaczmarek.ipcalculator.feature.calculator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.feature.calculator.screen.CalculatorRoute

const val CALCULATOR_ROUTE = "calculator"

fun NavGraphBuilder.calculatorScreen(
    layoutType: LayoutType,
    onShareText: (String) -> Unit,
) {
    composable(route = CALCULATOR_ROUTE) {
        CalculatorRoute(
            layoutType = layoutType,
            onShareText = onShareText,
        )
    }
}