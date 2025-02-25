package io.github.kaczmarek.ipcalculator.feature.info.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.DefaultInfoComponent
import io.github.kaczmarek.ipcalculator.feature.info.presentation.screen.InfoComponent


fun ComponentFactory.createInfoComponent(
    componentContext: ComponentContext,
    onOutput: (InfoComponent.Output) -> Unit,
): InfoComponent {
    return DefaultInfoComponent(
        componentContext = componentContext,
        onOutput = onOutput,
    )
}