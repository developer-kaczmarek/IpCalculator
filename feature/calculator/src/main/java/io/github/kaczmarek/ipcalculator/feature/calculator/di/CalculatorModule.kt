package io.github.kaczmarek.ipcalculator.feature.calculator.di

import io.github.kaczmarek.ipcalculator.feature.calculator.screen.CalculatorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val calculatorModule = module {

    viewModel {
        CalculatorViewModel(
            calculatorRepository = get(),
        )
    }
}