package io.github.kaczmarek.ipcalculator.core.model

enum class CalculationType {
    IPAddress,
    CIDRPrefix,
    SubnetMask,
    WildcardMask,
    NetworkIPAddress,
    BroadcastIPAddress,
    MaxPossibleHosts,
    UsableHosts,
    FirstHost,
    LastHost,
}