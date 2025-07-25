package io.github.kaczmarek.ipcalculator.core.model

data class Calculation(
    val type: CalculationType,
    val value: String,
) {
    companion object {
        const val NO_DATA = "-"
    }
}