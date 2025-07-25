package io.github.kaczmarek.ipcalculator.feature.calculator.model

import io.github.kaczmarek.ipcalculator.core.model.Calculation
import io.github.kaczmarek.ipcalculator.core.model.CalculationType
import io.github.kaczmarek.ipcalculator.core.ui.model.UiText
import io.github.kaczmarek.ipcalculator.feature.calculator.R

data class CalculationUiModel(
    val name: UiText,
    val value: UiText,
)

fun Calculation.asUiModel() = CalculationUiModel(
    name = when (type) {
        CalculationType.IPAddress -> UiText.StringResource(R.string.calculator_ip_address)
        CalculationType.CIDRPrefix -> UiText.StringResource(R.string.calculator_cidr_prefix)
        CalculationType.SubnetMask -> UiText.StringResource(R.string.calculator_subnet_mask)
        CalculationType.WildcardMask -> UiText.StringResource(R.string.calculator_wildcard_mask)
        CalculationType.NetworkIPAddress -> UiText.StringResource(R.string.calculator_network_ip_address)
        CalculationType.BroadcastIPAddress -> UiText.StringResource(R.string.calculator_broadcast_ip_address)
        CalculationType.MaxPossibleHosts -> UiText.StringResource(R.string.calculator_max_possible_hosts)
        CalculationType.UsableHosts -> UiText.StringResource(R.string.calculator_usable_hosts)
        CalculationType.FirstHost -> UiText.StringResource(R.string.calculator_first_host)
        CalculationType.LastHost -> UiText.StringResource(R.string.calculator_last_host)
    },
    value = if (value == Calculation.NO_DATA) {
        UiText.StringResource(R.string.calculator_no_data)
    } else {
        UiText.DynamicString(value)
    }
)