package io.github.kaczmarek.ipcalculator.feature.settings.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.settings.data.repository.DefaultSettingsRepository
import io.github.kaczmarek.ipcalculator.feature.settings.data.source.SettingsLocalDataStore
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.DefaultSettingsComponent
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsComponent
import io.github.kaczmarek.ipcalculator.feature.settings.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.component.get

val settingsModule = module {

    single { SettingsLocalDataStore(context = androidContext()) }

    single<SettingsRepository> {
        DefaultSettingsRepository(
            settingsLocalDataStore = get(),
        )
    }
}

fun ComponentFactory.createSettingsComponent(
    componentContext: ComponentContext,
    onOutput: (SettingsComponent.Output) -> Unit,
): SettingsComponent {
    return DefaultSettingsComponent(
        componentContext = componentContext,
        onOutput = onOutput,
        settingsRepository = get(),
        languageManager = get(),
    )
}