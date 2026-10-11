package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import io.github.kaczmarek.ipcalculator.core.ui.model.UiText

internal sealed interface CalculatorEffect {

    data class ShowErrorSnackbar(
        val message: UiText,
        val action: UiText,
    ) : CalculatorEffect

    data object HideKeyboard : CalculatorEffect
}