package io.github.kaczmarek.ipcalculator.feature.calculator.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.ui.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.CalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.DefaultCalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.PreviewCalculatorComponent
import org.koin.core.component.get

fun ComponentFactory.createCalculatorComponent(
    componentContext: ComponentContext,
    onOutput: (CalculatorComponent.Output) -> Unit,
): CalculatorComponent {
    return DefaultCalculatorComponent(
        componentContext = componentContext,
        onOutput = onOutput,
        calculatorRepository = get(),
    )
}

fun createPreviewCalculatorComponent(): CalculatorComponent {
    return PreviewCalculatorComponent()
}