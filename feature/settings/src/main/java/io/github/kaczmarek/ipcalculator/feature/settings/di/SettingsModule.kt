package io.github.kaczmarek.ipcalculator.feature.settings.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.ui.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.DefaultSettingsComponent
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.SettingsComponent
import org.koin.core.component.get

fun ComponentFactory.createSettingsComponent(
    componentContext: ComponentContext,
    onOutput: (SettingsComponent.Output) -> Unit,
): SettingsComponent {
    return DefaultSettingsComponent(
        componentContext = componentContext,
        onOutput = onOutput,
        languageRepository = get(),
        themeRepository = get(),
    )
}