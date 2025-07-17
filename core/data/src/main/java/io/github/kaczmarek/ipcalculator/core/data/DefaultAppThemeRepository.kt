package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.datastore.SettingsDataStore
import io.github.kaczmarek.ipcalculator.core.model.ThemeType

internal class DefaultAppThemeRepository(
    private val settingsDataStore: SettingsDataStore,
): AppThemeRepository {

    override fun getSelectedThemeType(): ThemeType {
        val name = settingsDataStore.getSelectedThemeTypeName()

        return ThemeType.entries.find { it.name == name } ?: ThemeType.System
    }

    override fun setSelectedTheme(themeType: ThemeType) {
        settingsDataStore.setSelectedThemeTypeName(themeType.name)
    }
}