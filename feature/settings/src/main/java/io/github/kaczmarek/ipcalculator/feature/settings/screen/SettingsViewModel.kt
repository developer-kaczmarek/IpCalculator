package io.github.kaczmarek.ipcalculator.feature.settings.screen

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import io.github.kaczmarek.ipcalculator.core.model.Language
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import io.github.kaczmarek.ipcalculator.core.ui.R
import io.github.kaczmarek.ipcalculator.core.ui.model.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val themeRepository: AppThemeRepository,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    val uiState = MutableStateFlow(SettingsUiState())

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()


    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _effect.emit(
                SettingsEffect.ShowErrorSnackbar(
                    message = UiText.StringResource(R.string.common_error_text),
                    action = UiText.StringResource(R.string.common_error_action_text),
                )
            )
        }
    }

    init {
        prepareUiState()
    }

    fun onLanguageItemClick(newLanguage: Language) {
        viewModelScope.launch(exceptionHandler) {
            uiState.update { uiState.value.copy(selectedLanguage = newLanguage) }
            languageRepository.setSelectedLanguage(newLanguage)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                _effect.emit(SettingsEffect.RecreateActivity)
            }
        }
    }

    fun onThemeItemClick(newTheme: ThemeType) {
        viewModelScope.launch(exceptionHandler) {
            themeRepository.setSelectedTheme(newTheme)
            uiState.update { uiState.value.copy(selectedThemeType = newTheme) }
        }
    }

    private fun prepareUiState() {
        viewModelScope.launch(exceptionHandler) {
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