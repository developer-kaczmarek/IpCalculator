package io.github.kaczmarek.ipcalculator.core.ui.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun SnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier.testTag("SnackbarHost"),
        snackbar = {
            Snackbar(
                snackbarData = it,
                modifier = Modifier.testTag("Snackbar"),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                actionColor = MaterialTheme.colorScheme.primary,
                actionOnNewLine = true,
            )
        }
    )
}