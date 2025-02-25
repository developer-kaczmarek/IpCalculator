package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import io.github.kaczmarek.ipcalculator.core.manager.locale.LanguageManager
import io.github.kaczmarek.ipcalculator.core.model.language.Language
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.core.utils.componentCoroutineScope
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val onOutput: (SettingsComponent.Output) -> Unit,
    private val settingsRepository: SettingsRepository,
    private val languageManager: LanguageManager,
) : ComponentContext by componentContext, SettingsComponent {

    override val uiState = MutableStateFlow(SettingsUiState())

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        // TODO: show snackbar
    }
    private val coroutineScope = componentCoroutineScope(exceptionHandler)

    init {
        lifecycle.doOnStart {
            prepareUiState()
        }
    }

    override fun onLanguageItemClick(newLanguage: Language) {
        coroutineScope.launch {
            settingsRepository.setSelectedLanguage(newLanguage)
            uiState.update { uiState.value.copy(selectedLanguage = newLanguage) }
            languageManager.updateAppLocale(newLanguage)
        }
    }

    override fun onThemeItemClick(newTheme: ThemeType) {
        coroutineScope.launch {
            settingsRepository.setSelectedTheme(newTheme)
            uiState.update { uiState.value.copy(selectedThemeType = newTheme) }
            onOutput.invoke(SettingsComponent.Output.ThemeChanged)
        }
    }

    private fun prepareUiState() {
        coroutineScope.launch {
            val selectedLanguage = settingsRepository.getSelectedLanguage()
            val selectedTheme = settingsRepository.getSelectedThemeType()

            uiState.update {
                uiState.value.copy(
                    selectedLanguage = selectedLanguage,
                    selectedThemeType = selectedTheme,
                )
            }
        }
    }
}