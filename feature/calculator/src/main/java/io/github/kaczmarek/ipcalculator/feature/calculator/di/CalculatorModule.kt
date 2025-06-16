package io.github.kaczmarek.ipcalculator.feature.calculator.di

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.core.factory.component.ComponentFactory
import io.github.kaczmarek.ipcalculator.feature.calculator.data.repository.DefaultCalculatorRepository
import io.github.kaczmarek.ipcalculator.feature.calculator.data.source.CalculatorLocalDataStore
import io.github.kaczmarek.ipcalculator.feature.calculator.domain.repository.InternalCalculatorRepository
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.CalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.DefaultCalculatorComponent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.PreviewCalculatorComponent
import org.koin.core.component.get
import org.koin.dsl.module

val calculatorModule = module {

    single {
        CalculatorLocalDataStore(
            resourceManager = get(),
        )
    }

    single<InternalCalculatorRepository> {
        DefaultCalculatorRepository(
            calculatorLocalDataStore = get(),
        )
    }
}

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