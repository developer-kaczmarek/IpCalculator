package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.flow.StateFlow

interface AppThemeRepository {

    fun getSelectedThemeType(): ThemeType

    fun observeThemeType(): StateFlow<ThemeType>

    fun setSelectedTheme(themeType: ThemeType)
}