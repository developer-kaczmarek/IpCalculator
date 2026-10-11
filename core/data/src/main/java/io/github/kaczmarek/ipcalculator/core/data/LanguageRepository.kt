package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {

    fun observeLanguage(): Flow<Language>

    fun getSelectedLanguage(): Language

    fun updateLanguageConfiguration(language: Language)
}