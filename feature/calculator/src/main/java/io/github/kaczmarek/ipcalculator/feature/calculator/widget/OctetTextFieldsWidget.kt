package io.github.kaczmarek.ipcalculator.feature.calculator.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.theme.robotoMonoFamily
import io.github.kaczmarek.ipcalculator.core.ui.utils.empty
import io.github.kaczmarek.ipcalculator.core.ui.utils.testTagAsId
import io.github.kaczmarek.ipcalculator.feature.calculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.model.OctetUiModel
import io.github.kaczmarek.ipcalculator.feature.calculator.screen.FOURTH_OCTET_INDEX

@Composable
internal fun OctetTextFieldsWidget(
    octets: List<OctetUiModel>,
    focusedOctetIndex: Int?,
    onOctetChange: (Int, TextFieldValue) -> Unit,
    onOctetNextImeActionClick: (Int) -> Unit,
    onOctetFocusChange: (Int) -> Unit,
    onOctetBackspace: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .testTagAsId("OctetTextFieldsWidget")
            .border(
                width = 1.dp,
                color = if (focusedOctetIndex != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                shape = CircleShape,
            )
            .focusable(),
        verticalAlignment = Alignment.Bottom,
    ) {
        val focusRequesters = remember { List(4) { FocusRequester() } }

        LaunchedEffect(focusedOctetIndex) {
           focusedOctetIndex?.let { index ->
                focusRequesters.getOrNull(index)?.requestFocus()
            }
        }

        octets.forEachIndexed { index, octet ->
            OctetTextField(
                octet = octet,
                onOctetChange = { onOctetChange(index, it) },
                onNextImeActionClick = { onOctetNextImeActionClick(index) },
                onFocusChange = { onOctetFocusChange(index) },
                onBackspace = {
                    onOctetBackspace(index)
                },
                modifier = Modifier
                    .testTagAsId("OctetTextField+$index")
                    .fillMaxWidth()
                    .weight(1.0f)
                    .focusRequester(focusRequesters[index])
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
private fun OctetDelimiterText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = stringResource(id = R.string.calculator_dot),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
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
private fun OctetTextField(
    octet: OctetUiModel,
    onOctetChange: (TextFieldValue) -> Unit,
    onNextImeActionClick: () -> Unit,
    onFocusChange: () -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

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
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocusChange()
                }
            }
            .onKeyEvent {
                if (it.type == KeyEventType.KeyUp &&
                    it.key == Key.Backspace &&
                    octet.value.selection.collapsed &&
                    octet.value.selection.start == 0
                ) {
                    onBackspace()
                    true
                } else {
                    false
                }
            },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onNext = { onNextImeActionClick() },
        ),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (octet.value.text.isEmpty() && !isFocused) {
                    PlaceholderText(
                        text = octet.placeholder,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                innerTextField()
            }
        },
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OctetTextFieldsWidgetPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            OctetTextFieldsWidget(
                octets = listOf(
                    OctetUiModel(
                        placeholder = "192",
                        value = TextFieldValue(
                            text = String.empty,
                            selection = TextRange.Zero,
                        ),
                    ),
                    OctetUiModel(
                        placeholder = "168",
                        value = TextFieldValue(
                            text = String.empty,
                            selection = TextRange.Zero,
                        ),
                    ),
                    OctetUiModel(
                        placeholder = "1",
                        value = TextFieldValue(
                            text = String.empty,
                            selection = TextRange.Zero,
                        ),
                    ),
                    OctetUiModel(
                        placeholder = "1",
                        value = TextFieldValue(
                            text = String.empty,
                            selection = TextRange.Zero,
                        ),
                    ),
                ),
                focusedOctetIndex = null,
                onOctetChange = { _, _ ->
                    // Do something
                },
                onOctetNextImeActionClick = {
                    // Do something
                },
                onOctetFocusChange = {
                    // Do something
                },
                onOctetBackspace = {
                    // Do something
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            )
        }
    }
}