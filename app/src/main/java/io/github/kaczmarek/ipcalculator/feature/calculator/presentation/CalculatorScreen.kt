package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.common.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.common.ui.theme.robotoMonoFamily
import io.github.kaczmarek.ipcalculator.common.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.common.utils.toPx

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    component: CalculatorComponent,
    modifier: Modifier = Modifier,
) {
    val uiState: CalculatorUiState by component.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1.0f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        ) {

        }

        CalculatorControlPanelWidget(
            uiState = uiState,
            component = component,
        )

        val scrollState = rememberScrollState()
        val shadowHeightInPx = 8.dp.toPx()
        val isShadowVisible: Boolean by remember {
            derivedStateOf { scrollState.value.toFloat() > shadowHeightInPx }
        }

        if (uiState.isSubnetMaskListOpening) {
            BasicAlertDialog(
                onDismissRequest = component::onSubnetMaskListDialogDismissRequest,
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(24.dp),
                        )
                        .clip(shape = RoundedCornerShape(24.dp)),
                ) {
                    Text(
                        text = stringResource(id = R.string.calculator_subnet_mask_dialog_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 16.dp)
                            .padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val subnetMaskList =
                                stringArrayResource(id = R.array.calculator_subnet_masks)

                            subnetMaskList.forEachIndexed { cidrValue, subnetMask ->
                                LargeText(
                                    text = subnetMask,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            component.onSubnetMaskItemClick(cidrValue = cidrValue)
                                        },
                                )

                                if (cidrValue < subnetMaskList.lastIndex) {
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }

                        if (isShadowVisible) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(0.1f),
                                                Color.Transparent,
                                            ),
                                        ),
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorControlPanelWidget(
    uiState: CalculatorUiState,
    component: CalculatorComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        PanelFieldsGroupWidget(
            modifier = Modifier.fillMaxWidth(),
            octets = uiState.octets,
            focusedOctetIndex = uiState.focusedOctetIndex,
            cidr = uiState.cidr,
            onOctetChange = component::onOctetChange,
            onOctetDeleteImeClick = component::onOctetDeleteImeClick,
            onOctetNextImeActionClick = component::onOctetNextImeActionClick,
            onOctetFocusChange = component::onOctetFocusChange,
            onCIDRClick = component::onCIDRClick,
        )

        PanelButtonsGroupWidget(
            modifier = Modifier.fillMaxWidth(),
            isSharingAvailable = uiState.isSharingAvailable,
            onCalculateClick = component::onCalculateClick,
            onShareClick = component::onShareClick,
        )
    }
}

@Composable
private fun PanelFieldsGroupWidget(
    octets: List<CalculatorUiState.Octet>,
    focusedOctetIndex: Int?,
    cidr: CalculatorUiState.CIDR?,
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
            LargeText(text = stringResource(id = R.string.common_slash))

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
private fun PanelButtonsGroupWidget(
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

@Composable
private fun OctetTextFieldsWidget(
    octets: List<CalculatorUiState.Octet>,
    focusedOctetIndex: Int?,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetDeleteImeClick: (Int) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.border(
            width = 1.dp,
            color = if (focusedOctetIndex != null) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            shape = CircleShape,
        ),
        verticalAlignment = Alignment.Bottom,
    ) {
        val focusManager = LocalFocusManager.current
        val firstOctetFocusRequester = remember { FocusRequester() }
        val secondOctetFocusRequester = remember { FocusRequester() }
        val thirdOctetFocusRequester = remember { FocusRequester() }
        val fourthOctetFocusRequester = remember { FocusRequester() }

        LaunchedEffect(focusedOctetIndex) {
            focusManager.clearFocus()
            when (focusedOctetIndex) {
                FIRST_OCTET_INDEX -> firstOctetFocusRequester.requestFocus()
                SECOND_OCTET_INDEX -> secondOctetFocusRequester.requestFocus()
                THIRD_OCTET_INDEX -> thirdOctetFocusRequester.requestFocus()
                FOURTH_OCTET_INDEX -> fourthOctetFocusRequester.requestFocus()
                else -> Unit
            }
        }

        octets.forEachIndexed { index, octet ->
            OctetTextField(
                octet = octet,
                onOctetChange = { onOctetChange(index, it) },
                onNextImeActionClick = { onOctetNextImeActionClick(index) },
                onFocusChange = { onOctetFocusChange(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .focusRequester(
                        when (index) {
                            FIRST_OCTET_INDEX -> firstOctetFocusRequester
                            SECOND_OCTET_INDEX -> secondOctetFocusRequester
                            THIRD_OCTET_INDEX -> thirdOctetFocusRequester
                            else -> fourthOctetFocusRequester
                        }
                    )
                    .onKeyEvent {
                        if (it.key == Key.Backspace) {
                            onOctetDeleteImeClick(index)
                        }
                        false
                    },
            )

            if (index < FOURTH_OCTET_INDEX) {
                OctetDelimiterText(
                    onClick = { onOctetFocusChange(index + 1) },
                )
            }
        }
    }
}

@Composable
private fun OctetTextField(
    octet: CalculatorUiState.Octet,
    onOctetChange: (TextFieldValue) -> Unit,
    onNextImeActionClick: () -> Unit,
    onFocusChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(focused) {
        if (focused) {
            onFocusChange()
        }
    }

    BasicTextField(
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        value = octet.value,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontFamily = robotoMonoFamily,
        ),
        onValueChange = { newValue ->
            onOctetChange(newValue)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
        ),
        modifier = modifier,
        singleLine = true,
        keyboardActions = KeyboardActions(
            onNext = { onNextImeActionClick() },
        ),
        decorationBox = @Composable { innerTextField ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (octet.value.text.isEmpty() && !focused) {
                    PlaceholderText(
                        text = octet.placeholder,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    innerTextField()
                }
            }
        },
    )
}

@Composable
private fun OctetDelimiterText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = stringResource(id = R.string.common_dot),
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(bottom = 4.dp),
    )
}

@Composable
private fun PlaceholderText(
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        textAlign = textAlign,
        fontFamily = robotoMonoFamily,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier,
    )
}

@Composable
private fun CIDRWidget(
    cidrPrefix: CalculatorUiState.CIDR,
    onCIDRClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .clickable(onClick = onCIDRClick)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cidrPrefix.value.isEmpty()) {
            PlaceholderText(
                text = cidrPrefix.placeholder,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp),
            )
        } else {
            Text(
                text = cidrPrefix.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontFamily = robotoMonoFamily,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
        )
    }
}

@Composable
private fun PanelButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(text = text)
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculatorScreenPreview() {
    AppTheme {
        CalculatorScreen(PreviewCalculatorComponent())
    }
}
