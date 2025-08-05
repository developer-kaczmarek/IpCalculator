package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.datastore.SettingsDataStore
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultAppThemeRepository(
    private val settingsDataStore: SettingsDataStore,
): AppThemeRepository {

    private val themeTypeFlow = MutableStateFlow(loadThemeType())

    override fun getSelectedThemeType(): ThemeType {
        return themeTypeFlow.value
    }

    override fun observeThemeType(): StateFlow<ThemeType> = themeTypeFlow

    override fun setSelectedTheme(themeType: ThemeType) {
        settingsDataStore.setSelectedThemeTypeName(themeType.name)
        themeTypeFlow.value = themeType
    }


    private fun loadThemeType(): ThemeType {
        val name = settingsDataStore.getSelectedThemeTypeName()
        return ThemeType.entries.find { it.name == name } ?: ThemeType.System
    }
}