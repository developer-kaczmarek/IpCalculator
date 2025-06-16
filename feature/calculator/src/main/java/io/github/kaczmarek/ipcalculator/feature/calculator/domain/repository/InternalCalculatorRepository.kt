package io.github.kaczmarek.ipcalculator.feature.calculator.domain.repository

import io.github.kaczmarek.ipcalculator.feature.calculator.domain.model.Calculation

internal interface InternalCalculatorRepository {

    suspend fun getCalculationList(
        currentOctets: List<Int>,
        currentCIDR: Int,
    ): List<Calculation>
}