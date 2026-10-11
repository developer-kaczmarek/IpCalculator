package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.Language

interface LanguageRepository {

    fun isManualChosen(): Boolean

    fun setManualChosen()

    fun resetManualChosen()

    fun getSelectedLanguage(): Language

    fun setSelectedLanguage(language: Language)

    fun updateLanguageConfiguration(language: Language)

    fun getSystemLocaleOrDefault(): Language

    fun isChosenFromPerAppMenu(): Boolean
}