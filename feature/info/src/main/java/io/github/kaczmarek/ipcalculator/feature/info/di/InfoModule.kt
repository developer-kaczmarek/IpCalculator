package io.github.kaczmarek.ipcalculator.feature.info.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.DefaultInfoComponent
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoComponent
import org.koin.core.component.KoinComponent


fun KoinComponent.createInfoComponent(
    componentContext: ComponentContext,
    onOutput: (InfoComponent.Output) -> Unit,
): InfoComponent {
    return DefaultInfoComponent(
        componentContext = componentContext,
        onOutput = onOutput,
    )
}