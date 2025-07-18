package io.github.kaczmarek.ipcalculator.root.di

import io.github.kaczmarek.ipcalculator.root.root.RootViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rootModule = module {

    viewModel {
        RootViewModel(
            themeRepository = get(),
            languageRepository = get(),
        )
    }
}