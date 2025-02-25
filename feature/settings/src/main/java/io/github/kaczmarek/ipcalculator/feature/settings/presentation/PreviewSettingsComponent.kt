package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.core.model.language.Language
import kotlinx.coroutines.flow.MutableStateFlow

internal class PreviewSettingsComponent : SettingsComponent {

    override val uiState = MutableStateFlow(SettingsUiState())

    override fun onLanguageItemClick(newLanguage: Language) = Unit

    override fun onThemeItemClick(newTheme: ThemeType) = Unit
}