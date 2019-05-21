package io.wax911.support.extension.util

import androidx.annotation.IntDef
import io.wax911.support.extension.LAZY_MODE_UNSAFE
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
    val currentSeasonIndex = SeasonType.ALL.indexOf(currentSeason)


    /**
     * Returns the calendar object
     */
    private val calendar by lazy(LAZY_MODE_UNSAFE) {
        Calendar.getInstance()
    }

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
    fun convertDate(unixTimeStamp: Long): String? {
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
    fun getYearRanges(start: Int, endDelta: Int): List<Int> =
        IntRange(
            start = start,
            endInclusive = getCurrentYear(endDelta)
        ).map { value -> value }


    /**
     * Checks if the time given has a difference greater than or equal to the target time
     *
     * @param conversionType type of comparison between the epoch time and target
     * @param epochTime time to compare against the current system clock
     * @param target unit to compare against
     */
    fun timeDifferenceSatisfied(@SupportDateUtilTimeType conversionType: Int, epochTime: Long, target: Int): Boolean {
        val currentTime = System.currentTimeMillis()
        val defaultSystemUnit = TimeUnit.MILLISECONDS
        return when (conversionType) {
            SupportDateUtilTimeType.TIME_UNIT_DAYS ->
                defaultSystemUnit.toDays(currentTime - epochTime) >= target
            SupportDateUtilTimeType.TIME_UNIT_HOURS ->
                defaultSystemUnit.toHours(currentTime - epochTime) >= target
            SupportDateUtilTimeType.TIME_UNIT_MINUTES ->
                defaultSystemUnit.toMinutes(currentTime - epochTime) >= target
            SupportDateUtilTimeType.TIME_UNITS_SECONDS ->
                defaultSystemUnit.toSeconds(currentTime - epochTime) >= target
            else -> false
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SupportDateUtilTimeType.TIME_UNIT_DAYS, SupportDateUtilTimeType.TIME_UNIT_HOURS,
            SupportDateUtilTimeType.TIME_UNIT_MINUTES, SupportDateUtilTimeType.TIME_UNITS_SECONDS)
    annotation class SupportDateUtilTimeType {
        companion object {

            const val TIME_UNIT_DAYS = 0
            const val TIME_UNIT_HOURS = 1
            const val TIME_UNIT_MINUTES = 2
            const val TIME_UNITS_SECONDS = 3
        }
    }
}
