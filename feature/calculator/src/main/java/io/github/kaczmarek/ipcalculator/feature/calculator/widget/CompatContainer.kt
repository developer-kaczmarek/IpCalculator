package io.github.kaczmarek.ipcalculator.feature.calculator.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.core.ui.widget.PanelButton
import io.github.kaczmarek.ipcalculator.feature.calculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.screen.CalculatorUiState

@Composable
internal fun CompactContainer(
    uiState: CalculatorUiState,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onCIDRClick: () -> Unit,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CalculationsListContentWidget(
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f),
        )

        CompatCalculatorControlPanelWidget(
            uiState = uiState,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            onCIDRClick = onCIDRClick,
            onShareClick = onShareClick,
            onCalculateClick = onCalculateClick,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                )
                .padding(all = 16.dp),
        )
    }
}


@Composable
private fun CompatCalculatorControlPanelWidget(
    uiState: CalculatorUiState,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onCIDRClick: () -> Unit,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        CompatPanelFieldsGroupWidget(
            modifier = Modifier.fillMaxWidth(),
            octets = uiState.octets,
            focusedOctetIndex = uiState.focusedOctetIndex,
            cidr = uiState.cidr,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            onCIDRClick = onCIDRClick,
        )

        CompatPanelButtonsGroupWidget(
            modifier = Modifier.fillMaxWidth(),
            isSharingAvailable = uiState.isSharingAvailable,
            onCalculateClick = onCalculateClick,
            onShareClick = onShareClick,
        )
    }
}

@Composable
private fun CompatPanelFieldsGroupWidget(
    octets: List<OctetDvo>,
    focusedOctetIndex: Int?,
    cidr: CIDRDvo?,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onCIDRClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        OctetTextFieldsWidget(
            octets = octets,
            focusedOctetIndex = focusedOctetIndex,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f),
        )

        cidr?.let {
            LargeText(
                text = stringResource(id = R.string.calculator_slash),
                modifier = Modifier.padding(all = 16.dp),
            )

            CIDRWidget(
                cidrPrefix = it,
                onCIDRClick = onCIDRClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
            )
        }
    }
}


@Composable
private fun CompatPanelButtonsGroupWidget(
    isSharingAvailable: Boolean,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        PanelButton(
            text = stringResource(id = R.string.calculator_calculate_text),
            onClick = onCalculateClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
        )

        PanelButton(
            text = stringResource(id = R.string.calculator_share_text),
            enabled = isSharingAvailable,
            onClick = onShareClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CompactContainerPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            CompactContainer(
                uiState = CalculatorUiState(
                    octets = listOf(
                        OctetDvo(
                            placeholder = "192",
                            value = TextFieldValue(
                                text = String.empty,
                                selection = TextRange.Zero,
                            ),
                        ),
                        OctetDvo(
                            placeholder = "168",
                            value = TextFieldValue(
                                text = String.empty,
                                selection = TextRange.Zero,
                            ),
                        ),
                        OctetDvo(
                            placeholder = "1",
                            value = TextFieldValue(
                                text = String.empty,
                                selection = TextRange.Zero,
                            ),
                        ),
                        OctetDvo(
                            placeholder = "1",
                            value = TextFieldValue(
                                text = String.empty,
                                selection = TextRange.Zero,
                            ),
                        ),
                    ),
                    cidr = CIDRDvo(
                        placeholder = "24",
                        value = String.empty,
                    ),
                ),
                onOctetChange = { _, _ -> },
                onOctetDeleteImeClick = {},
                onOctetNextImeActionClick = {},
                onOctetFocusChange = {},
                onCIDRClick = {},
                onCalculateClick = {},
                onShareClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            )
        }
    }
}
