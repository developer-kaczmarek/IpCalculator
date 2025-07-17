package io.github.kaczmarek.ipcalculator.feature.calculator.presentation.widget

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
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
import io.github.kaczmarek.ipcalculator.feature.calculator.R
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.model.OctetDvo
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.FIRST_OCTET_INDEX
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.FOURTH_OCTET_INDEX
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.SECOND_OCTET_INDEX
import io.github.kaczmarek.ipcalculator.feature.calculator.presentation.screen.THIRD_OCTET_INDEX

@Composable
internal fun OctetTextFieldsWidget(
    octets: List<OctetDvo>,
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
    octet: OctetDvo,
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
                focusedOctetIndex = null,
                onOctetChange = { index, value ->
                    // Do something
                },
                onOctetDeleteImeClick = {
                    // Do something
                },
                onOctetNextImeActionClick = {
                    // Do something
                },
                onOctetFocusChange = {
                    // Do something
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            )
        }
    }
}