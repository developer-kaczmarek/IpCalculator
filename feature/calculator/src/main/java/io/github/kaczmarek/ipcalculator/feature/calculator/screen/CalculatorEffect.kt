package io.github.kaczmarek.ipcalculator.feature.calculator.screen

internal sealed interface CalculatorEffect {

    data class ShareText(val text: String) : CalculatorEffect

    data class ShowSnackbar(val message: String) : CalculatorEffect
}