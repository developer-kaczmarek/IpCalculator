package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.ThemeType

interface AppThemeRepository {

    fun getSelectedThemeType(): ThemeType

    fun setSelectedTheme(themeType: ThemeType)
}