package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kaczmarek.ipcalculator.core.model.language.Language
import io.github.kaczmarek.ipcalculator.core.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.widget.CardWrapper
import io.github.kaczmarek.ipcalculator.core.ui.widget.HeadlineItem
import io.github.kaczmarek.ipcalculator.feature.settings.R

@Composable
fun SettingsScreen(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        val uiState: SettingsUiState by component.uiState.collectAsStateWithLifecycle()

        HeadlineItem(
            text = stringResource(id = R.string.settings_theme),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )

        ThemeRadioGroup(
            selectedThemeType = uiState.selectedThemeType,
            onClick = { component.onThemeItemClick(it) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        HeadlineItem(
            text = stringResource(id = R.string.settings_language),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )

        LocaleRadioGroup(
            selectedLanguage = uiState.selectedLanguage,
            onClick = { component.onLanguageItemClick(it) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun RadioOption(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )

        RadioButton(
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = MaterialTheme.colorScheme.secondary,
            ),
            selected = selected,
            onClick = null,
        )
    }
}

@Composable
private fun LocaleRadioGroup(
    selectedLanguage: Language,
    onClick: (language: Language) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWrapper(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            RadioOption(
                selected = selectedLanguage == Language.English,
                text = stringResource(id = R.string.settings_locale_en),
                onClick = { onClick(Language.English) },
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            RadioOption(
                selected = selectedLanguage == Language.Russian,
                text = stringResource(id = R.string.settings_locale_ru),
                onClick = { onClick(Language.Russian) },
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            RadioOption(
                selected = selectedLanguage == Language.Kazakh,
                text = stringResource(id = R.string.settings_locale_kk),
                onClick = { onClick(Language.Kazakh) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ThemeRadioGroup(
    selectedThemeType: ThemeType,
    onClick: (themeType: ThemeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWrapper(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            RadioOption(
                selected = selectedThemeType == ThemeType.System,
                text = stringResource(id = R.string.settings_theme_system),
                onClick = { onClick(ThemeType.System) },
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            RadioOption(
                selected = selectedThemeType == ThemeType.Dark,
                text = stringResource(id = R.string.settings_theme_dark),
                onClick = { onClick(ThemeType.Dark) },
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            RadioOption(
                selected = selectedThemeType == ThemeType.Light,
                text = stringResource(id = R.string.settings_theme_light),
                onClick = { onClick(ThemeType.Light) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(PreviewSettingsComponent())
    }
}
