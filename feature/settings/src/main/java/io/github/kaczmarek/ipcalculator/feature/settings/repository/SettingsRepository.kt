package io.github.kaczmarek.ipcalculator.feature.settings.repository

import io.github.kaczmarek.ipcalculator.core.model.language.Language
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType

interface SettingsRepository {

    fun isLanguageSelected(): Boolean

    fun getSelectedLanguage(): Language

    fun getSelectedThemeType(): ThemeType

    fun setSelectedLanguage(language: Language)

    fun setSelectedTheme(themeType: ThemeType)
}