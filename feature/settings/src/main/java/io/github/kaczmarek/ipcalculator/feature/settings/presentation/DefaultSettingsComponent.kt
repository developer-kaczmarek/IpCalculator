package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import io.github.kaczmarek.ipcalculator.core.model.Language
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import io.github.kaczmarek.ipcalculator.core.ui.utils.componentCoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val onOutput: (SettingsComponent.Output) -> Unit,
    private val themeRepository: AppThemeRepository,
    private val languageRepository: LanguageRepository,
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
            languageRepository.setSelectedLanguage(newLanguage)
            uiState.update { uiState.value.copy(selectedLanguage = newLanguage) }
        }
    }

    override fun onThemeItemClick(newTheme: ThemeType) {
        coroutineScope.launch {
            themeRepository.setSelectedTheme(newTheme)
            uiState.update { uiState.value.copy(selectedThemeType = newTheme) }
            onOutput.invoke(SettingsComponent.Output.ThemeChanged)
        }
    }

    private fun prepareUiState() {
        coroutineScope.launch {
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