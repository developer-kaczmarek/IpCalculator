package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue

const val FIRST_OCTET_INDEX = 0
const val SECOND_OCTET_INDEX = 1
const val THIRD_OCTET_INDEX = 2
const val FOURTH_OCTET_INDEX = 3

@Immutable
data class CalculatorUiState(
    val octets: List<Octet> = emptyList(),
    val focusedOctetIndex: Int? = null,
    val cidr: CIDR? = null,
    val isSharingAvailable: Boolean = false,
    val isSubnetMaskListOpening: Boolean = false,
) {
    data class Octet(
        val placeholder: String,
        val value: TextFieldValue,
    )

    data class CIDR(
        val placeholder: String,
        val value: String,
    )
}