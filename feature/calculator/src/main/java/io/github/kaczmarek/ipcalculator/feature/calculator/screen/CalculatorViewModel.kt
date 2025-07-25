package io.github.kaczmarek.ipcalculator.feature.calculator.screen

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.ipcalculator.core.data.CalculatorRepository
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.model.asUiModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DOT_SYMBOL = "."
private const val ZERO_SYMBOL = "0"
private const val FIRST_OCTET_PLACEHOLDER = "192"
private const val SECOND_OCTET_PLACEHOLDER = "168"
private const val THIRD_AND_FOURTH_OCTETS_PLACEHOLDER = "1"
private const val CIDR_PREFIX_PLACEHOLDER = "24"

internal class CalculatorViewModel(
    private val calculatorRepository: CalculatorRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CalculatorEffect>()
    val effect: SharedFlow<CalculatorEffect> = _effect.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        // TODO: show snackbar
    }

    init {
        prepareUiState()
    }

    fun onCalculateClick() {
        viewModelScope.launch {
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
                uiState.value.cidr?.value!!.toInt()
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

    fun onShareClick() {
        viewModelScope.launch {
            var shareText = String.empty
            uiState.value.calculations.forEach { calculation ->
                shareText += "${calculation.name}: ${calculation.value}\n"
            }

            _effect.emit(CalculatorEffect.ShareText(shareText))
        }
    }

    fun onOctetChange(index: Int, value: TextFieldValue) {
        viewModelScope.launch {
            when {
                isEndsWithDot(value.text) -> updateFocusedOctetIndexIfCan(index = index + 1)

                shouldGoToNextOctet(value.text) -> transferLastSymbolToNextOctetIfNeed(
                    index = index,
                    text = value.text,
                )

                else -> updateOctetValue(index, value)
            }
        }
    }

    fun onOctetDeleteImeClick(index: Int) {
        viewModelScope.launch {
            val octet = uiState.value.octets[index]
            if (octet.value.text.isNotEmpty() || index == FIRST_OCTET_INDEX) return@launch

            val previousOctetIndex = index - 1
            val previousOctet = uiState.value.octets[previousOctetIndex]

            onOctetChange(
                index = previousOctetIndex, value = TextFieldValue(
                    text = previousOctet.value.text,
                    selection = TextRange(previousOctet.value.text.length),
                )
            )
            updateFocusedOctetIndexIfCan(index = previousOctetIndex)
        }
    }

    fun onOctetNextImeActionClick(index: Int) {
        viewModelScope.launch {
            updateFocusedOctetIndexIfCan(
                index = if (index < FOURTH_OCTET_INDEX) {
                    index + 1
                } else {
                    null
                },
            )
        }
    }

    fun onOctetFocusChange(index: Int) {
        viewModelScope.launch {
            updateFocusedOctetIndexIfCan(index = index)
        }
    }

    fun onCIDRClick() {
        viewModelScope.launch {
            updateFocusedOctetIndexIfCan(index = null)
            updateCIDRPrefixListOpeningState(isOpening = true)
        }
    }

    fun onSubnetMaskItemClick(cidrValue: Int) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    cidr = state.cidr?.copy(value = cidrValue.toString()),
                    isSubnetMaskListOpening = false,
                )
            }
        }
    }

    fun onSubnetMaskListDialogDismissRequest() {
        viewModelScope.launch {
            updateCIDRPrefixListOpeningState(isOpening = false)
        }
    }

    private suspend fun updateOctetValue(index: Int, value: TextFieldValue) {
        _uiState.update {
            uiState.value.copy(
                octets = withContext(Dispatchers.Default) {
                    uiState.value.octets.mapIndexed { currentIndex, octet ->
                        if (currentIndex == index) {
                            octet.copy(value = value)
                        } else {
                            octet.copy(
                                value = TextFieldValue(
                                    text = octet.value.text,
                                    selection = TextRange.Zero,
                                )
                            )
                        }
                    }
                },
            )
        }
    }

    private fun updateFocusedOctetIndexIfCan(index: Int?) {
        if (index == null || index <= FOURTH_OCTET_INDEX) {
            _uiState.update { state ->
                state.copy(focusedOctetIndex = index)
            }
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
        return hasLeadingZero(text) || isMoreThenMaxOctetValue(text)
                || isMoreThenMaxOctetLength(text)
    }

    private suspend fun transferLastSymbolToNextOctetIfNeed(index: Int, text: String) {
        if (index < FOURTH_OCTET_INDEX) {
            updateOctetValue(
                index = index + 1, value = TextFieldValue(
                    text = text.last().toString(),
                    selection = TextRange(text.length),
                )
            )
        }
        updateFocusedOctetIndexIfCan(index = index + 1)
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
        viewModelScope.launch {
            if (uiState.value.octets.isNotEmpty() && uiState.value.cidr != null) return@launch

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
    }

    private fun updateCIDRPrefixListOpeningState(isOpening: Boolean) {
        _uiState.update { state ->
            state.copy(isSubnetMaskListOpening = isOpening)
        }
    }
}