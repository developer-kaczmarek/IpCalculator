package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.StateFlow

interface CalculatorComponent {

    val uiState: StateFlow<CalculatorUiState>

    fun onCalculateClick()

    fun onShareClick()

    fun onOctetChange(index: Int, value: TextFieldValue)

    fun onOctetDeleteImeClick(index: Int)

    fun onOctetNextImeActionClick(index: Int)

    fun onOctetFocusChange(index: Int)

    fun onCIDRClick()

    fun onSubnetMaskItemClick(cidrValue: Int)

    fun onSubnetMaskListDialogDismissRequest()
}