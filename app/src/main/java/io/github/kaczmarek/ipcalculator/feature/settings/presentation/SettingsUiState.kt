package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.core.model.language.Language
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType

data class SettingsUiState(
    val selectedLanguage: Language = Language.English,
    val selectedThemeType: ThemeType = ThemeType.System,
)