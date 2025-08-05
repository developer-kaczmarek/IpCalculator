package io.github.kaczmarek.ipcalculator.feature.settings.screen

import io.github.kaczmarek.ipcalculator.core.ui.model.UiText

internal sealed interface SettingsEffect {

    data class ShowErrorSnackbar(
        val message: UiText,
        val action: UiText,
    ) : SettingsEffect

    data object RecreateActivity: SettingsEffect
}