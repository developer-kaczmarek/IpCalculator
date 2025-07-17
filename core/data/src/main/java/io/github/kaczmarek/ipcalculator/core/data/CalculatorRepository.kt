package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.Calculation

interface CalculatorRepository {

    suspend fun getCalculationList(
        currentOctets: List<Int>,
        currentCIDR: Int,
    ): List<Calculation>
}