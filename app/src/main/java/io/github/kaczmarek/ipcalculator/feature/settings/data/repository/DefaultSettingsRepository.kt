package io.github.kaczmarek.ipcalculator.feature.settings.data.repository

import io.github.kaczmarek.ipcalculator.feature.settings.data.source.SettingsLocalDataStore
import io.github.kaczmarek.ipcalculator.core.model.language.Language
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository

class DefaultSettingsRepository(
    private val settingsLocalDataStore: SettingsLocalDataStore,
): SettingsRepository {

    override fun isLanguageSelected(): Boolean {
        val code = settingsLocalDataStore.getSelectedLanguageCode()
        return Language.entries.find { it.code == code } != null
    }

    override fun getSelectedLanguage(): Language {
        val code = settingsLocalDataStore.getSelectedLanguageCode()
        return Language.entries.find { it.code == code } ?: Language.English
    }

    override fun getSelectedThemeType(): ThemeType {
        val name = settingsLocalDataStore.getSelectedThemeTypeName()
        return ThemeType.entries.find { it.name == name } ?: ThemeType.System
    }

    override fun setSelectedLanguage(language: Language) {
        settingsLocalDataStore.setSelectedLanguageCode(language.code)
    }

    override fun setSelectedTheme(themeType: ThemeType) {
        settingsLocalDataStore.setSelectedThemeTypeName(themeType.name)
    }
}