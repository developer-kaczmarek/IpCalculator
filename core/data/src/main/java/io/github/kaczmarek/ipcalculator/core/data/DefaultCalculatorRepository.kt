package io.github.kaczmarek.ipcalculator.core.data

import io.github.kaczmarek.ipcalculator.core.model.Calculation
import io.github.kaczmarek.ipcalculator.core.model.CalculationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.pow

private const val FIRST_OCTET_INDEX = 0
private const val SECOND_OCTET_INDEX = 1
private const val THIRD_OCTET_INDEX = 2
private const val FOURTH_OCTET_INDEX = 3
private const val DECIMAL_FORMAT_PATTERN = "###,###"
private const val GROUPING_SEPARATOR = ' '

internal class DefaultCalculatorRepository : CalculatorRepository {

    override suspend fun getCalculationList(
        currentOctets: List<Int>,
        currentCIDR: Int,
    ): List<Calculation> = withContext(Dispatchers.Default) {
        val majorIpAddress = getMajorIpAddress(currentOctets, currentCIDR)
        val usableHostCount = getUsableHostCount(currentCIDR)
        val subnetMask = currentCIDR.toSubnetMask()
        val wildcardMask = subnetMask.inv()
        val calculations = arrayListOf<Calculation>()

        calculations.add(
            Calculation(
                type = CalculationType.IPAddress,
                value = currentOctets.toBinary().toLong().fromBinary(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.CIDRPrefix,
                value = currentCIDR.toString(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.SubnetMask,
                value = subnetMask.toLong().fromBinary(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.WildcardMask,
                value = wildcardMask.toLong().fromBinary(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.NetworkIPAddress,
                value = majorIpAddress.toLong().fromBinary(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.BroadcastIPAddress,
                value = (majorIpAddress or wildcardMask).toLong().fromBinary(),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.MaxPossibleHosts,
                value = getFormattedNumber(
                    number = getMaxPossibleHostCount(currentCIDR)
                ),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.UsableHosts,
                value = getFormattedNumber(usableHostCount),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.FirstHost,
                value = getFirstUsableHost(
                    cidr = currentCIDR,
                    majorIpAddress = majorIpAddress,
                ),
            )
        )
        calculations.add(
            Calculation(
                type = CalculationType.LastHost,
                value = getLastUsableHost(
                    cidr = currentCIDR,
                    majorIpAddress = majorIpAddress,
                    usableHostsCount = usableHostCount,
                ),
            )
        )

        calculations
    }

    private fun Long.fromBinary(): String {
        val firstOctet = this shr 24 and 0xFF
        val secondOctet = this shr 16 and 0xFF
        val thirdOctet = this shr 8 and 0xFF
        val fourthOctet = this and 0xFF

        return "$firstOctet.$secondOctet.$thirdOctet.$fourthOctet"
    }

    private fun List<Int>.toBinary(): Int {
        var output = this[FIRST_OCTET_INDEX]
        output = (output shl 8) + this[SECOND_OCTET_INDEX]
        output = (output shl 8) + this[THIRD_OCTET_INDEX]
        output = (output shl 8) + this[FOURTH_OCTET_INDEX]

        return output
    }

    private fun Int.toSubnetMask(): Int {
        if (this == 0) return 0

        return -1 shl Integer.SIZE - this
    }

    private fun getMajorIpAddress(octets: List<Int>, cidr: Int): Int {
        val offset = Integer.SIZE - cidr
        val majorAddress = if (cidr == 0) {
            listOf(0, 0, 0, 0).toBinary()
        } else {
            octets.toBinary()
        }

        return majorAddress shr offset shl offset
    }

    private fun getUsableHostCount(cidr: Int): Long {
        val count = 2.0.pow((Integer.SIZE - cidr).toDouble()).toLong() - 2

        return if (count < 0) 0 else count
    }

    private fun getMaxPossibleHostCount(cidr: Int): Long {
        val count = 2.0.pow((Integer.SIZE - cidr).toDouble()).toLong()

        return if (count < 1) 0 else count
    }

    private fun getFirstUsableHost(cidr: Int, majorIpAddress: Int): String {
        return if (cidr > 30) {
            Calculation.NO_DATA
        } else {
            (majorIpAddress + 1).toLong().fromBinary()
        }
    }

    private fun getLastUsableHost(cidr: Int, majorIpAddress: Int, usableHostsCount: Long): String {
        return if (cidr > 30) {
            Calculation.NO_DATA
        } else {
            (majorIpAddress + usableHostsCount).fromBinary()
        }
    }

    private fun getFormattedNumber(number: Long): String {
        val symbols = DecimalFormatSymbols()
        symbols.groupingSeparator = GROUPING_SEPARATOR

        return DecimalFormat(DECIMAL_FORMAT_PATTERN, symbols).format(number)
    }
}