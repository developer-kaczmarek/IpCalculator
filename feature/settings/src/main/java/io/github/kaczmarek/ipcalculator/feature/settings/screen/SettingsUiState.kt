package io.github.kaczmarek.ipcalculator.feature.settings.screen

import io.github.kaczmarek.ipcalculator.core.model.Language
import io.github.kaczmarek.ipcalculator.core.model.ThemeType

internal data class SettingsUiState(
    val selectedLanguage: Language = Language.English,
    val selectedThemeType: ThemeType = ThemeType.System,
)