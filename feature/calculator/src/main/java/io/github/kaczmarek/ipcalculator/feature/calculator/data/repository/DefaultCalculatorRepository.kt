package io.github.kaczmarek.ipcalculator.feature.calculator.data.repository

import io.github.kaczmarek.ipcalculator.feature.calculator.data.source.CalculatorLocalDataStore
import io.github.kaczmarek.ipcalculator.feature.calculator.domain.model.Calculation
import io.github.kaczmarek.ipcalculator.feature.calculator.domain.repository.InternalCalculatorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DefaultCalculatorRepository(
    private val calculatorLocalDataStore: CalculatorLocalDataStore,
) : InternalCalculatorRepository {

    override suspend fun getCalculationList(
        currentOctets: List<Int>,
        currentCIDR: Int,
    ): List<Calculation> = withContext(Dispatchers.Default) {
        calculatorLocalDataStore.getCalculationList(currentOctets, currentCIDR)
    }
}