package io.github.kaczmarek.ipcalculator.feature.settings.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import io.github.kaczmarek.ipcalculator.core.model.Language
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val themeRepository: AppThemeRepository,
    private val languageRepository: LanguageRepository,
) : ViewModel() {
    
    val uiState = MutableStateFlow(SettingsUiState())

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        // TODO: show snackbar
    }

    init {
        prepareUiState()
    }

     fun onLanguageItemClick(newLanguage: Language) {
        viewModelScope.launch {
            languageRepository.setSelectedLanguage(newLanguage)
            uiState.update { uiState.value.copy(selectedLanguage = newLanguage) }
        }
    }

     fun onThemeItemClick(newTheme: ThemeType) {
        viewModelScope.launch {
            themeRepository.setSelectedTheme(newTheme)
            uiState.update { uiState.value.copy(selectedThemeType = newTheme) }
        }
    }

    private fun prepareUiState() {
        viewModelScope.launch {
            val selectedLanguage = languageRepository.getSelectedLanguage()
            val selectedTheme = themeRepository.getSelectedThemeType()

            uiState.update {
                uiState.value.copy(
                    selectedLanguage = selectedLanguage,
                    selectedThemeType = selectedTheme,
                )
            }
        }
    }
}