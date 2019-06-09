package io.wax911.support.extension.util

import androidx.annotation.IntDef
import io.wax911.support.extension.util.attribute.SeasonType
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object SupportDateUtil {

    /**
     * Gets the current season title for menu
     *
     * @return Season name
     */
    val currentSeasonIndex
        get() =  SeasonType.ALL.indexOf(currentSeason)


    /**
     * Returns the calendar object
     */
    private val calendar
        get() =  Calendar.getInstance()


    /**
     * Gets current season title
     *
     * @return Season name
     */
    val currentSeason: String
        @SeasonType get() {
            val month = calendar.get(Calendar.MONTH)
            return when (month) {
                in 0..1 -> SeasonType.WINTER
                in 2..4 -> SeasonType.SPRING
                in 5..7 -> SeasonType.SUMMER
                in 8..10 -> SeasonType.FALL
                else -> SeasonType.WINTER
            }
        }

    /**
     * Gets the current year + delta, if the season for the year is winter later in the year
     * then the result would be the current year plus the delta
     *
     * @return current year with a given delta
     */
    fun getCurrentYear(delta: Int = 0): Int {
        return if (calendar.get(Calendar.MONTH) >= 11 && currentSeason == SeasonType.WINTER)
            calendar.get(Calendar.YEAR) + delta
        else calendar.get(Calendar.YEAR)
    }

    /**
     * Converts unix time representation into current readable time
     *
     * @return A time format of dd MMM yyyy
     */
    fun convertUnixTimeToShortDate(unixTimeStamp: Long): String? {
        if (unixTimeStamp != 0L) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return dateFormat.format(Date(unixTimeStamp * 1000L))
        }
        return null
    }

    /**
     * Creates a range of years from the given begin year to the end delta
     *
     * @param start Starting year
     * @param endDelta End difference plus or minus the current year
     */
    fun generateYearRanges(start: Int, endDelta: Int) = IntRange(
        start = start,
        endInclusive = getCurrentYear(endDelta)
    ).map { value -> value }


    /**
     * Checks if the time given has a difference greater than or equal to the target time
     *
     * @param supportTimeType type of comparison between the epoch time and target
     * @param epochTime time to compare against the current system clock
     * @param target unit to compare against
     */
    fun timeStampHasElapsed(supportTimeType: SupportTimeUnit, epochTime: Long, target: Int): Boolean {
        val currentTime = System.currentTimeMillis()
        val defaultSystemUnit = TimeUnit.MILLISECONDS
        return when (supportTimeType) {
            SupportDateTimeUnit.TIME_UNIT_DAYS ->
                defaultSystemUnit.toDays(currentTime - epochTime) >= target
            SupportDateTimeUnit.TIME_UNIT_HOURS ->
                defaultSystemUnit.toHours(currentTime - epochTime) >= target
            SupportDateTimeUnit.TIME_UNIT_MINUTES ->
                defaultSystemUnit.toMinutes(currentTime - epochTime) >= target
            SupportDateTimeUnit.TIME_UNITS_SECONDS ->
                defaultSystemUnit.toSeconds(currentTime - epochTime) >= target
            else -> false
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @Target(
        AnnotationTarget.TYPEALIAS,
        AnnotationTarget.VALUE_PARAMETER
    )
    @IntDef(
        SupportDateTimeUnit.TIME_UNIT_DAYS,
        SupportDateTimeUnit.TIME_UNIT_HOURS,
        SupportDateTimeUnit.TIME_UNIT_MINUTES,
        SupportDateTimeUnit.TIME_UNITS_SECONDS
    )
    annotation class SupportDateTimeUnit {
        companion object {

            const val TIME_UNIT_DAYS = 0
            const val TIME_UNIT_HOURS = 1
            const val TIME_UNIT_MINUTES = 2
            const val TIME_UNITS_SECONDS = 3
        }
    }
}

@SupportDateUtil.SupportDateTimeUnit
typealias SupportTimeUnit = Int
