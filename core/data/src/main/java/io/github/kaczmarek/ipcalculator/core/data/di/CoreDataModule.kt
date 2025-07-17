package io.github.kaczmarek.ipcalculator.core.data.di

import android.content.Context
import android.content.res.Resources
import io.github.kaczmarek.ipcalculator.core.data.AppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.CalculatorRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultAppThemeRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultCalculatorRepository
import io.github.kaczmarek.ipcalculator.core.data.DefaultLanguageRepository
import io.github.kaczmarek.ipcalculator.core.data.LanguageRepository
import io.github.kaczmarek.ipcalculator.core.data.ResourceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.Locale

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
        DefaultCalculatorRepository(
            resourceManager = get(),
        )
    }

    factory {
        getResourceManager(
            context = androidContext(),
            settingsRepository = get(),
        )
    }
}

private fun getResourceManager(
    context: Context,
    settingsRepository: LanguageRepository,
): ResourceManager {
    val languageCode = settingsRepository.getSelectedLanguage().code

    return ResourceManager(context.getResources(Locale(languageCode)))
}

private fun Context.getResources(locale: Locale): Resources {
    val configuration = android.content.res.Configuration(resources.configuration)
    configuration.setLocale(locale)

    return createConfigurationContext(configuration).resources
}
