package io.github.kaczmarek.ipcalculator.feature.calculator.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.CalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.DefaultCalculatorComponent
import org.koin.core.component.get


fun ComponentFactory.createCalculatorComponent(
    componentContext: ComponentContext,
    onOutput: (CalculatorComponent.Output) -> Unit,
): CalculatorComponent {
    return DefaultCalculatorComponent(
        componentContext = componentContext,
        onOutput = onOutput,
        resourceManager = get(),
    )
}