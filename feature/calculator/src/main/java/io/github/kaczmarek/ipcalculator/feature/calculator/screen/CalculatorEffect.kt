package io.github.kaczmarek.ipcalculator.feature.calculator.screen

sealed interface CalculatorEffect {

    data class ShareText(val text: String) : CalculatorEffect

    data class ShowSnackbar(val message: String) : CalculatorEffect
}