package io.github.kaczmarek.ipcalculator.feature.info.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaczmarek.ipcalculator.core.model.AppLinkType
import io.github.kaczmarek.ipcalculator.core.model.LayoutType
import io.github.kaczmarek.ipcalculator.core.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.core.ui.widget.CardWrapper
import io.github.kaczmarek.ipcalculator.core.ui.widget.LargeText
import io.github.kaczmarek.ipcalculator.feature.info.R

@Composable
internal fun InfoRoute(
    layoutType: LayoutType,
    onOpenLink: (AppLinkType) -> Unit,
    onShareText: (String) -> Unit,
    onOpenStore: () -> Unit,
) {
    InfoScreen(
        layoutType = layoutType,
        onOpenLink = onOpenLink,
        onShareText = onShareText,
        onOpenStore = onOpenStore,
    )
}

@Composable
private fun InfoScreen(
    layoutType: LayoutType,
    onOpenLink: (AppLinkType) -> Unit,
    onShareText: (String) -> Unit,
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .fillMaxWidth(fraction = if (layoutType == LayoutType.Compat) 1.0f else 0.6f),
        ) {
            LargeText(
                text = stringResource(id = R.string.info_go_to_github),
                modifier = Modifier
                    .testTag("GithubLargeText")
                    .fillMaxWidth()
                    .clickable(onClick = { onOpenLink(AppLinkType.Github) })
                    .padding(all = 16.dp),
            )
        }

        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(fraction = if (layoutType == LayoutType.Compat) 1.0f else 0.6f),
        ) {
            LargeText(
                text = stringResource(id = R.string.info_privacy_policy),
                modifier = Modifier
                    .testTag("PrivacyPolicyLargeText")
                    .fillMaxWidth()
                    .clickable(onClick = { onOpenLink(AppLinkType.PrivacyPolicy) })
                    .padding(all = 16.dp),
            )
        }

        CardWrapper(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(fraction = if (layoutType == LayoutType.Compat) 1.0f else 0.6f),
        ) {
            val context = LocalContext.current

            Column(modifier = Modifier.fillMaxWidth()) {
                LargeText(
                    text = stringResource(id = R.string.info_send_email),
                    modifier = Modifier
                        .testTag("ContactLargeText")
                        .fillMaxWidth()
                        .clickable(onClick = { onOpenLink(AppLinkType.Support) })
                        .padding(all = 16.dp),
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                LargeText(
                    text = stringResource(id = R.string.info_app_rate),
                    modifier = Modifier
                        .testTag("RateTheAppLargeText")
                        .fillMaxWidth()
                        .clickable(onClick = onOpenStore)
                        .padding(all = 16.dp),
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                LargeText(
                    text = stringResource(id = R.string.info_share_app),
                    modifier = Modifier
                        .testTag("ShareTheAppLargeText")
                        .fillMaxWidth()
                        .clickable { onShareText(context.getString(R.string.share_app_text)) }
                        .padding(all = 16.dp),
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun InfoScreenPreview() {
    AppTheme {
        InfoScreen(
            layoutType = LayoutType.Compat,
            onOpenLink = {},
            onOpenStore = {},
            onShareText = {},
        )
    }
}
