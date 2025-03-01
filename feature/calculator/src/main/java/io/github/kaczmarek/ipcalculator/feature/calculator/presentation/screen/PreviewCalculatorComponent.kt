package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PreviewCalculatorComponent : CalculatorComponent {

    override val uiState: StateFlow<CalculatorUiState> = MutableStateFlow(CalculatorUiState())

    override fun onCalculateClick() = Unit

    override fun onShareClick() = Unit

    override fun onOctetChange(index: Int, value: TextFieldValue) = Unit

    override fun onOctetDeleteImeClick(index: Int) = Unit

    override fun onOctetNextImeActionClick(index: Int) = Unit

    override fun onOctetFocusChange(index: Int) = Unit

    override fun onCIDRClick() = Unit

    override fun onSubnetMaskItemClick(cidrValue: Int) = Unit

    override fun onSubnetMaskListDialogDismissRequest() = Unit
}