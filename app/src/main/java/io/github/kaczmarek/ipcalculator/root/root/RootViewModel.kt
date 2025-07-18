package io.github.kaczmarek.ipcalculator.root.root

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
            if (languageRepository.isLanguageSelected()) return@launch

            languageRepository.setSelectedLanguage(
                language = languageRepository.getSystemLocaleOrDefault(),
            )
        }
    }
}