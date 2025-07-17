package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import io.github.kaczmarek.ipcalculator.core.data.CalculatorRepository
import io.github.kaczmarek.ipcalculator.core.ui.utils.componentCoroutineScope
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.core.ui.utils.persistent
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.model.CIDRDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.model.OctetDvo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

private const val DOT_SYMBOL = "."
private const val ZERO_SYMBOL = "0"
private const val FIRST_OCTET_PLACEHOLDER = "192"
private const val SECOND_OCTET_PLACEHOLDER = "168"
private const val THIRD_AND_FOURTH_OCTETS_PLACEHOLDER = "1"
private const val CIDR_PREFIX_PLACEHOLDER = "24"

internal class DefaultCalculatorComponent(
    componentContext: ComponentContext,
    private val onOutput: (CalculatorComponent.Output) -> Unit,
    private val calculatorRepository: CalculatorRepository,
) : ComponentContext by componentContext, CalculatorComponent {

    override val uiState = MutableStateFlow(CalculatorUiState())

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        // TODO: show snackbar
    }
    private val coroutineScope = componentCoroutineScope(exceptionHandler)

    init {
        persistent(
            serializer = PersistentState.serializer(),
            save = { saveState() },
            restore = { state -> restoreState(state) },
        )

        lifecycle.doOnStart {
            prepareUiState()
        }
    }

    override fun onCalculateClick() {
        coroutineScope.launch {
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

            uiState.update { state ->
                state.copy(
                    calculations = calculatorRepository.getCalculationList(
                        currentOctets = currentOctets,
                        currentCIDR = currentCIDR,
                    ),
                    isSharingAvailable = true,
                )
            }
        }
    }

    override fun onShareClick() {
        coroutineScope.launch {
            var shareText = String.empty
            uiState.value.calculations.forEach { calculation ->
                shareText += "${calculation.name}: ${calculation.value}\n"
            }
            onOutput(CalculatorComponent.Output.ShareText(shareText))
        }
    }

    override fun onOctetChange(index: Int, value: TextFieldValue) {
        coroutineScope.launch {
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

    override fun onOctetDeleteImeClick(index: Int) {
        coroutineScope.launch {
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

    override fun onOctetNextImeActionClick(index: Int) {
        coroutineScope.launch {
            updateFocusedOctetIndexIfCan(
                index = if (index < FOURTH_OCTET_INDEX) {
                    index + 1
                } else {
                    null
                },
            )
        }
    }

    override fun onOctetFocusChange(index: Int) {
        coroutineScope.launch {
            updateFocusedOctetIndexIfCan(index = index)
        }
    }

    override fun onCIDRClick() {
        coroutineScope.launch {
            updateFocusedOctetIndexIfCan(index = null)
            updateCIDRPrefixListOpeningState(isOpening = true)
        }
    }

    override fun onSubnetMaskItemClick(cidrValue: Int) {
        coroutineScope.launch {
            uiState.update { state ->
                state.copy(
                    cidr = state.cidr?.copy(value = cidrValue.toString()),
                    isSubnetMaskListOpening = false,
                )
            }
        }
    }

    override fun onSubnetMaskListDialogDismissRequest() {
        coroutineScope.launch {
            updateCIDRPrefixListOpeningState(isOpening = false)
        }
    }

    private suspend fun updateOctetValue(index: Int, value: TextFieldValue) {
        uiState.update {
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
            uiState.update { state ->
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

    private fun getPreparedOctets(): List<OctetDvo> {
        return listOf(
            OctetDvo(
                placeholder = FIRST_OCTET_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetDvo(
                placeholder = SECOND_OCTET_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetDvo(
                placeholder = THIRD_AND_FOURTH_OCTETS_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
            OctetDvo(
                placeholder = THIRD_AND_FOURTH_OCTETS_PLACEHOLDER,
                value = TextFieldValue(
                    text = String.empty,
                    selection = TextRange.Zero,
                ),
            ),
        )
    }

    private fun prepareUiState() {
        coroutineScope.launch {
            if (uiState.value.octets.isNotEmpty() && uiState.value.cidr != null) return@launch

            uiState.update { state ->
                state.copy(
                    octets = getPreparedOctets(),
                    cidr = CIDRDvo(
                        placeholder = CIDR_PREFIX_PLACEHOLDER,
                        value = String.empty,
                    ),
                )
            }
        }
    }

    private fun updateCIDRPrefixListOpeningState(isOpening: Boolean) {
        uiState.update { state ->
            state.copy(isSubnetMaskListOpening = isOpening)
        }
    }

    private fun saveState(): PersistentState {
        val uiStateValue = uiState.value

        return PersistentState(
            octets = uiStateValue.octets.map { octet -> octet.value.text },
            cidr = uiStateValue.cidr?.value.orEmpty(),
            areCalculationsVisible = uiStateValue.calculations.isNotEmpty(),
        )
    }

    private fun restoreState(persistentState: PersistentState) {
        coroutineScope.launch {
            uiState.update { state ->
                state.copy(
                    octets = getPreparedOctets().mapIndexed { index, octetDvo ->
                        octetDvo.copy(
                            value = TextFieldValue(
                                text = persistentState.octets[index],
                                selection = TextRange.Zero,
                            ),
                        )
                    },
                    cidr = CIDRDvo(
                        placeholder = CIDR_PREFIX_PLACEHOLDER,
                        value = persistentState.cidr,
                    ),
                )
            }

            if (persistentState.areCalculationsVisible) {
                onCalculateClick()
            }
        }
    }

    @Serializable
    private data class PersistentState(
        val octets: List<String>,
        val cidr: String,
        val areCalculationsVisible: Boolean,
    )
}