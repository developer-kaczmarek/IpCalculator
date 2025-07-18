package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import androidx.compose.runtime.Immutable
import io.github.kaczmarek.ipcalculator.core.model.Calculation
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetDvo

const val FIRST_OCTET_INDEX = 0
const val SECOND_OCTET_INDEX = 1
const val THIRD_OCTET_INDEX = 2
const val FOURTH_OCTET_INDEX = 3

@Immutable
data class CalculatorUiState(
    val octets: List<OctetDvo> = emptyList(),
    val focusedOctetIndex: Int? = null,
    val cidr: CIDRDvo? = null,
    val isSharingAvailable: Boolean = false,
    val isSubnetMaskListOpening: Boolean = false,
    val calculations: List<Calculation> = emptyList(),
)