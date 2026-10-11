package io.github.kaczmarek.ipcalculator.root.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.model.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class RootViewModel(
    themeRepository: AppThemeRepository,
) : ViewModel() {

    val themeType = themeRepository.observeThemeType()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemeType.System,
        )
}