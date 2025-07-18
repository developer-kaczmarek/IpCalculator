package io.github.kaczmarek.ipcalculator.feature.settings.di

import io.github.kaczmarek.ipcalculator.feature.settings.screen.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel {
        SettingsViewModel(
            themeRepository = get(),
            languageRepository = get(),
        )
    }
}