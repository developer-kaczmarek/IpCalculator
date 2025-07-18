package io.github.kaczmarek.ipcalculator.feature.calculator.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.core.ui.widget.PanelButton
import io.github.kaczmarek.ipcalculator.feature.calculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.model.CIDRDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.screen.CalculatorUiState

@Composable
internal fun SpaciousContainer(
    uiState: CalculatorUiState,
    layoutType: LayoutType,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onCIDRClick: () -> Unit,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
) {
    Row(
        modifier = modifier
            .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Start))
            .fillMaxSize(),

        ) {
        CalculationsListContentWidget(
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f),
        )

        SpaciousCalculatorControlPanelWidget(
            uiState = uiState,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            onCIDRClick = onCIDRClick,
            onCalculateClick = onCalculateClick,
            onShareClick = onShareClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(if (layoutType == LayoutType.Compat) 0.8f else 0.4f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
                )
                .padding(all = 16.dp),
        )
    }
}

@Composable
private fun SpaciousCalculatorControlPanelWidget(
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
        SpaciousPanelFieldsGroupWidget(
            octets = uiState.octets,
            focusedOctetIndex = uiState.focusedOctetIndex,
            cidr = uiState.cidr,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            onCIDRClick = onCIDRClick,
        )

        SpaciousPanelButtonsGroupWidget(
            modifier = Modifier.fillMaxWidth(),
            isSharingAvailable = uiState.isSharingAvailable,
            onCalculateClick = onCalculateClick,
            onShareClick = onShareClick,
        )
    }
}

@Composable
private fun SpaciousPanelFieldsGroupWidget(
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        LargeText(text = stringResource(id = R.string.calculator_ip_address))
        OctetTextFieldsWidget(
            octets = octets,
            focusedOctetIndex = focusedOctetIndex,
            onOctetChange = onOctetChange,
            onOctetDeleteImeClick = onOctetDeleteImeClick,
            onOctetNextImeActionClick = onOctetNextImeActionClick,
            onOctetFocusChange = onOctetFocusChange,
            modifier = Modifier.fillMaxWidth(),
        )

        cidr?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                LargeText(text = stringResource(id = R.string.calculator_cidr_prefix))

                CIDRWidget(
                    cidrPrefix = it,
                    onCIDRClick = onCIDRClick,
                )
            }
        }
    }
}

@Composable
private fun SpaciousPanelButtonsGroupWidget(
    isSharingAvailable: Boolean,
    onCalculateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        PanelButton(
            text = stringResource(id = R.string.calculator_calculate_text),
            onClick = onCalculateClick,
            modifier = Modifier.fillMaxWidth(),
        )

        PanelButton(
            text = stringResource(id = R.string.calculator_share_text),
            enabled = isSharingAvailable,
            onClick = onShareClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(
    widthDp = 640,
    heightDp = 360,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 640,
    heightDp = 360,
)
@Composable
private fun SpaciousContainerPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            SpaciousContainer(
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
                layoutType = LayoutType.Spacious,
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
