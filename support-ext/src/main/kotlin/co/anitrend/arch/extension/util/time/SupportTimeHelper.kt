package co.anitrend.arch.extension.util.time

import co.anitrend.arch.extension.annotation.SupportExperimental
import java.util.concurrent.TimeUnit


/**
 * Holder for constructing time units
 *
 * @param supportCurrentUnixTime time to convert using the given [SupportDateTimeUnit]
 * @param supportTimeType type of comparison between the epoch time and target
 * @param supportTargetTime unit to compare against [supportCurrentUnixTime] - [SupportTimeInstance]
 * of the last checkpoint passed into [hasElapsed]
 *
 * @since v1.2.0
 */
@SupportExperimental
class SupportTimeHelper(
    val supportCurrentUnixTime: Long = System.currentTimeMillis(),
    val supportTargetTime: Int,
    val supportTimeType: SupportDateTimeUnit
) {
    private val defaultSystemUnit = TimeUnit.MILLISECONDS

    /**
     * Checks if the time unix timestamp has a difference greater than or equal to the
     * [supportTargetTime] which has a measurement unit of [SupportDateTimeUnit]
     */
    fun hasElapsed(unixTimeInstance: SupportTimeInstance) : Boolean {
        return when (supportTimeType) {
            SupportDateTimeUnit.TIME_UNIT_DAYS ->
                defaultSystemUnit.toDays(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            SupportDateTimeUnit.TIME_UNIT_HOURS ->
                defaultSystemUnit.toHours(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            SupportDateTimeUnit.TIME_UNIT_MINUTES ->
                defaultSystemUnit.toMinutes(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            SupportDateTimeUnit.TIME_UNIT_SECONDS ->
                defaultSystemUnit.toSeconds(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
        }
    }
}

typealias SupportTimeInstance = Long