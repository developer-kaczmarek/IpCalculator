package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import androidx.compose.runtime.Immutable
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CalculationUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetUiModel

const val FIRST_OCTET_INDEX = 0
const val SECOND_OCTET_INDEX = 1
const val THIRD_OCTET_INDEX = 2
const val FOURTH_OCTET_INDEX = 3

@Immutable
internal data class CalculatorUiState(
    val octets: List<OctetUiModel> = emptyList(),
    val focusedOctetIndex: Int? = null,
    val cidr: CIDRUiModel? = null,
    val isSharingAvailable: Boolean = false,
    val isSubnetMaskListOpening: Boolean = false,
    val calculations: List<CalculationUiModel> = emptyList(),
)