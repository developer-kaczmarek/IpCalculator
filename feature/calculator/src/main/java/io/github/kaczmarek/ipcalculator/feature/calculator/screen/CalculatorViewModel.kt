package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.CalculatorRepository
import io.github.kaczmarek.ipcalculator.core.ui.R
import io.github.kaczmarek.ipcalculator.core.ui.model.UiText
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.asUiModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DOT_SYMBOL = "."
private const val ZERO_SYMBOL = "0"
private const val FIRST_OCTET_PLACEHOLDER = "192"
private const val SECOND_OCTET_PLACEHOLDER = "168"
private const val THIRD_AND_FOURTH_OCTETS_PLACEHOLDER = "1"
private const val CIDR_PREFIX_PLACEHOLDER = "24"
private const val INVALID_SYMBOL_PATTERN = "[%@*()-,]"

internal class CalculatorViewModel(
    private val calculatorRepository: CalculatorRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CalculatorEffect>()
    val effect: SharedFlow<CalculatorEffect> = _effect.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _effect.emit(
                CalculatorEffect.ShowErrorSnackbar(
                    message = UiText.StringResource(R.string.common_error_text),
                    action = UiText.StringResource(R.string.common_error_action_text),
                )
            )
        }
    }

    init {
        prepareUiState()
    }

    fun onCalculateClick() {
        hideKeyboard()

        viewModelScope.launch(exceptionHandler) {
            updateFocusedOctetIndexIfCan(index = null)

            val currentOctets: List<Int> = uiState.value.octets.map { octet ->
                if (octet.value.text.isEmpty()) {
                    octet.placeholder.toInt()
                } else {
                    octet.value.text.toInt()
                }
            }

            val currentCIDR: Int = if (uiState.value.cidr?.value.isNullOrEmpty()) {
                CIDR_PREFIX_PLACEHOLDER.toInt()
            } else {
                uiState.value.cidr!!.value.toInt()
            }

            _uiState.update { state ->
                state.copy(
                    calculations = calculatorRepository.getCalculationList(
                        currentOctets = currentOctets,
                        currentCIDR = currentCIDR,
                    ).map { it.asUiModel() },
                    isSharingAvailable = true,
                )
            }
        }
    }

    fun onOctetChange(index: Int, value: TextFieldValue) {
        if (value.text.contains(Regex(INVALID_SYMBOL_PATTERN))) return

        val oldText = uiState.value.octets[index].value.text

        when {
            isEndsWithDot(value.text) -> {
                focusOctetAtEnd(index + 1)
            }

            shouldGoToNextOctet(value.text) -> {
                transferLastSymbolToNextOctet(index, value.text)
            }

            value.text.isEmpty() && index > FIRST_OCTET_INDEX -> {
                if (oldText.isNotEmpty()) {
                    clearOctetAndFocusPrevious(index, value)
                } else {
                    onOctetBackspace(index)
                }
            }

            else -> updateOctetValue(index, value)
        }
    }

    fun onOctetNextImeActionClick(index: Int) {
        if (index == FOURTH_OCTET_INDEX) {
            updateFocusedOctetIndexIfCan(index = null)
            hideKeyboard()
        } else {
            focusOctetAtEnd(index + 1)
        }
    }

    fun onOctetFocusChange(index: Int) {
        updateFocusedOctetIndexIfCan(index = index)
    }

    fun onOctetBackspace(index: Int) {
        if (index <= FIRST_OCTET_INDEX) {
            if (uiState.value.octets[index].value.text.isEmpty()) {
                updateFocusedOctetIndexIfCan(index = null)
                hideKeyboard()
            }
            return
        }

        focusOctetAtEnd(index - 1)
    }

    fun onCIDRClick() {
        updateFocusedOctetIndexIfCan(index = null)
        updateCIDRPrefixListOpeningState(isOpening = true)
    }

    fun onSubnetMaskItemClick(cidrValue: Int) {
        _uiState.update { state ->
            state.copy(
                cidr = state.cidr?.copy(value = cidrValue.toString()),
                isSubnetMaskListOpening = false,
            )
        }
    }

    fun onSubnetMaskListDialogDismissRequest() {
        updateCIDRPrefixListOpeningState(isOpening = false)
    }

    private fun updateOctetValue(index: Int, value: TextFieldValue) {
        _uiState.update { state ->
            val newOctets = state.octets.toMutableList()
            newOctets[index] = newOctets[index].copy(value = value)
            state.copy(octets = newOctets)
        }
    }

    private fun updateFocusedOctetIndexIfCan(index: Int?) {
        if (index == null || index in FIRST_OCTET_INDEX..FOURTH_OCTET_INDEX) {
            _uiState.update { state ->
                state.copy(focusedOctetIndex = index)
            }
        }
    }

    private fun focusOctetAtEnd(index: Int) {
        if (index !in FIRST_OCTET_INDEX..FOURTH_OCTET_INDEX) return

        _uiState.update { state ->
            val newOctets = state.octets.toMutableList()
            val octet = newOctets[index]

            newOctets[index] = octet.copy(
                value = octet.value.copy(
                    selection = TextRange(octet.value.text.length),
                ),
            )

            state.copy(
                octets = newOctets,
                focusedOctetIndex = index,
            )
        }
    }

    private fun hasLeadingZero(text: String): Boolean {
        return text.length >= 2 && text.startsWith(ZERO_SYMBOL)
    }

    private fun isMoreThenMaxOctetValue(text: String): Boolean {
        return text.isNotEmpty() && text.toInt() > 0xFF
    }

    private fun isMoreThenMaxOctetLength(text: String): Boolean {
        return text.length > 3
    }

    private fun isEndsWithDot(text: String): Boolean {
        return text.endsWith(DOT_SYMBOL)
    }

    private fun shouldGoToNextOctet(text: String): Boolean {
        return hasLeadingZero(text) ||
                isMoreThenMaxOctetValue(text) ||
                isMoreThenMaxOctetLength(text)
    }

    private fun transferLastSymbolToNextOctet(index: Int, text: String) {
        if (index >= FOURTH_OCTET_INDEX) return

        _uiState.update { state ->
            val newOctets = state.octets.toMutableList()
            val lastChar = text.last().toString()

            newOctets[index] = newOctets[index].copy(
                value = TextFieldValue(
                    text = text.dropLast(1),
                    selection = TextRange(text.length - 1),
                ),
            )

            newOctets[index + 1] = newOctets[index + 1].copy(
                value = TextFieldValue(
                    text = lastChar,
                    selection = TextRange(lastChar.length),
                ),
            )

            state.copy(
                octets = newOctets,
                focusedOctetIndex = index + 1,
            )
        }
    }

    private fun clearOctetAndFocusPrevious(
        index: Int,
        value: TextFieldValue,
    ) {
        val previousIndex = index - 1

        _uiState.update { state ->
            val newOctets = state.octets.toMutableList()

            newOctets[index] = newOctets[index].copy(value = value)

            val previousOctet = newOctets[previousIndex]
            newOctets[previousIndex] = previousOctet.copy(
                value = previousOctet.value.copy(
                    selection = TextRange(previousOctet.value.text.length),
                ),
            )

            state.copy(
                octets = newOctets,
                focusedOctetIndex = previousIndex,
            )
        }
    }

    private fun hideKeyboard() {
        viewModelScope.launch {
            _effect.emit(CalculatorEffect.HideKeyboard)
        }
    }

    private fun getPreparedOctets(): List<OctetUiModel> {
        return listOf(
            OctetUiModel(
                placeholder = FIRST_OCTET_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetUiModel(
                placeholder = SECOND_OCTET_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetUiModel(
                placeholder = THIRD_AND_FOURTH_OCTETS_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetUiModel(
                placeholder = THIRD_AND_FOURTH_OCTETS_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
        )
    }

    private fun prepareUiState() {
        if (_uiState.value.octets.isNotEmpty() && _uiState.value.cidr != null) return

        _uiState.update { state ->
            state.copy(
                octets = getPreparedOctets(),
                cidr = CIDRUiModel(
                    placeholder = CIDR_PREFIX_PLACEHOLDER,
                    value = String.empty,
                ),
            )
        }
    }

    private fun updateCIDRPrefixListOpeningState(isOpening: Boolean) {
        _uiState.update { state ->
            state.copy(isSubnetMaskListOpening = isOpening)
        }
    }
}