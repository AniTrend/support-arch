package co.anitrend.arch.extension.util

import androidx.annotation.StringDef
import co.anitrend.arch.extension.annotation.SupportExperimental
import co.anitrend.arch.extension.util.SupportTimeHelper.Companion.SupportDateTimeUnit
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
    @SupportDateTimeUnit
    val supportTimeType: String
) {
    private val defaultSystemUnit = TimeUnit.MILLISECONDS

    /**
     * Checks if the time unix timestamp has a difference greater than or equal to the
     * [supportTargetTime] which has a measurement unit of [SupportDateTimeUnit]
     */
    fun hasElapsed(unixTimeInstance: SupportTimeInstance) : Boolean {
        return when (supportTimeType) {
            TIME_UNIT_DAYS ->
                defaultSystemUnit.toDays(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            TIME_UNIT_HOURS ->
                defaultSystemUnit.toHours(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            TIME_UNIT_MINUTES ->
                defaultSystemUnit.toMinutes(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            TIME_UNIT_SECONDS ->
                defaultSystemUnit.toSeconds(
                    supportCurrentUnixTime - unixTimeInstance
                ) >= supportTargetTime
            else -> false
        }
    }

    companion object {
        const val TIME_UNIT_DAYS = "TIME_UNIT_DAYS"
        const val TIME_UNIT_HOURS = "TIME_UNIT_HOURS"
        const val TIME_UNIT_MINUTES = "TIME_UNIT_MINUTES"
        const val TIME_UNIT_SECONDS = "TIME_UNIT_SECONDS"

        @StringDef(
            TIME_UNIT_DAYS,
            TIME_UNIT_HOURS,
            TIME_UNIT_MINUTES,
            TIME_UNIT_SECONDS
        )
        @Retention(AnnotationRetention.SOURCE)
        @Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.VALUE_PARAMETER)
        annotation class SupportDateTimeUnit
    }
}

typealias SupportTimeInstance = Long