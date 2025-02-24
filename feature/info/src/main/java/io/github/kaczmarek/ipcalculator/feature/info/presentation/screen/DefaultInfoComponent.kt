package io.github.kaczmarek.ipcalculator.feature.info.presentation.screen

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.feature.info.domain.model.AppLinkType

internal class DefaultInfoComponent(
    componentContext: ComponentContext,
    private val onOutput: (InfoComponent.Output) -> Unit,
) : ComponentContext by componentContext, InfoComponent {

    override fun onOpenGithubPageClick() {
        onOutput(InfoComponent.Output.OpenLink(AppLinkType.Github))
    }

    override fun onReadPrivacyPolicyClick() {
        onOutput(InfoComponent.Output.OpenLink(AppLinkType.PrivacyPolicy))
    }

    override fun onContactDeveloperClick() {
        onOutput(InfoComponent.Output.OpenLink(AppLinkType.Support))
    }

    override fun onRateAppClick() {
        onOutput(InfoComponent.Output.RateApp)
    }

    override fun onShareAppClick(text: String) {
        onOutput(InfoComponent.Output.ShareText(text))
    }
}