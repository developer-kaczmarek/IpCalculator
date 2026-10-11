package io.github.kaczmarek.ipcalculator.root.root

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RootViewModel(
    themeRepository: AppThemeRepository,
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    val themeType = themeRepository.observeThemeType()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemeType.System,
        )

    init {
        setLanguagePreferenceIfNeed()
    }

    private fun setLanguagePreferenceIfNeed() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                applyLanguageConsideringPerAppMenu()
            } else {
                applySystemLanguageIfNotManuallyChosen()
            }
        }
    }

    private fun applySystemLanguageIfNotManuallyChosen() {
        val isManualLanguageChosen = languageRepository.isManualChosen()
        if (isManualLanguageChosen) return

        val language = languageRepository.getSystemLocaleOrDefault()
        languageRepository.setSelectedLanguage(language = language)
        languageRepository.updateLanguageConfiguration(language = language)
    }

    private fun applyLanguageConsideringPerAppMenu() {
        val isLanguageChosenFromPerAppMenu = languageRepository.isChosenFromPerAppMenu()
        val systemLanguage = languageRepository.getSystemLocaleOrDefault()
        val appLanguage = languageRepository.getSelectedLanguage()

        if (appLanguage == systemLanguage) return

        languageRepository.setSelectedLanguage(language = systemLanguage)

        if (!isLanguageChosenFromPerAppMenu) {
            languageRepository.updateLanguageConfiguration(language = systemLanguage)
        }
    }
}