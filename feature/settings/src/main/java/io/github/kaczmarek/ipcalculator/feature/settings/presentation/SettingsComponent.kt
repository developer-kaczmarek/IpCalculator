package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.core.model.Language
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val uiState: StateFlow<SettingsUiState>

    sealed interface Output {

        data object ThemeChanged : Output
    }

    fun onLanguageItemClick(newLanguage: Language)

    fun onThemeItemClick(newTheme: ThemeType)
}