package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.Language

interface LanguageRepository {

    fun isLanguageSelected(): Boolean

    fun getSelectedLanguage(): Language

    fun setSelectedLanguage(language: Language)

    fun getSystemLocaleOrDefault(): Language
}