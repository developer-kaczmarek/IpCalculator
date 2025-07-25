package io.github.kaczmarek.ipcalculator.core.data.di

import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.CalculatorRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultAppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultCalculatorRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultLanguageRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {

    single<AppThemeRepository> {
        DefaultAppThemeRepository(
            settingsDataStore = get(),
        )
    }

    single<LanguageRepository> {
        DefaultLanguageRepository(
            settingsDataStore = get(),
            context = androidContext(),
        )
    }

    single<CalculatorRepository> {
        DefaultCalculatorRepository()
    }
}
