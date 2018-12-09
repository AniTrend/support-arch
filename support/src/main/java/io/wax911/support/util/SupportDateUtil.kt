package io.wax911.support.util

import com.annimon.stream.Collectors
import com.annimon.stream.IntStream
import io.wax911.support.base.attribute.SeasonType
import io.wax911.support.base.attribute.TimeTargetType
import io.wax911.support.constructListFrom
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object SupportDateUtil {

    private val seasons = arrayOf(SeasonType.WINTER, SeasonType.WINTER,
            SeasonType.SPRING, SeasonType.SPRING, SeasonType.SPRING,
            SeasonType.SUMMER, SeasonType.SUMMER, SeasonType.SUMMER,
            SeasonType.FALL, SeasonType.FALL, SeasonType.FALL,
            SeasonType.WINTER)

    /**
     * Gets current season title
     * <br></br>
     *
     * @return Season name
     */
    val currentSeason: String
        @SeasonType get() {
            val month = Calendar.getInstance().get(Calendar.MONTH)
            return when (month) {
                in 0..1 -> SeasonType.WINTER
                in 2..4 -> SeasonType.SPRING
                in 5..7 -> SeasonType.SUMMER
                in 8..10 -> SeasonType.FALL
                else -> SeasonType.WINTER
            }
        }

    /**
     * Gets the current season title for menu
     * <br></br>
     *
     * @return Season name
     */
    val currentSeasonIndex: Int by lazy {
        SeasonType.Seasons.constructListFrom().indexOf(currentSeason)
    }

    /**
     * Returns the calendar object
     */
    val calendar: Calendar by lazy {
        Calendar.getInstance()
    }

    /**
     * Gets the current year + delta, if the season for the year is winter later in the year
     * then the result would be the current year plus the delta
     * <br></br>
     *
     * @return current year with a given delta
     */
    fun getCurrentYear(delta: Int): Int {
        return if (calendar.get(Calendar.MONTH) >= 11 && currentSeason == SeasonType.WINTER)
            calendar.get(Calendar.YEAR) + delta
        else calendar.get(Calendar.YEAR)
    }

    /**
     * Converts unix time representation into current readable time
     * <br></br>
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
     * @param start Starting year
     * @param endDelta End difference plus or minus the current year
     */
    fun getYearRanges(start: Int, endDelta: Int): List<Int> =
        IntStream.range(start, getCurrentYear(endDelta))
                .boxed().collect(Collectors.toList<Int>())


    /**
     * Checks if the time given has a difference greater than or equal to the target time
     * <br></br>
     * @param conversionTarget type of comparison between the epoch time and target
     * @param epochTime time to compare against the current system clock
     * @param target unit to compare against
     */
    fun timeDifferenceSatisfied(@TimeTargetType conversionTarget: Int, epochTime: Long, target: Int): Boolean {
        val currentTime = System.currentTimeMillis()
        val defaultSystemUnit = TimeUnit.MILLISECONDS
        when (conversionTarget) {
            TimeTargetType.TIME_UNIT_DAYS -> return defaultSystemUnit.toDays(currentTime - epochTime) >= target
            TimeTargetType.TIME_UNIT_HOURS -> return defaultSystemUnit.toHours(currentTime - epochTime) >= target
            TimeTargetType.TIME_UNIT_MINUTES -> return defaultSystemUnit.toMinutes(currentTime - epochTime) >= target
            TimeTargetType.TIME_UNITS_SECONDS -> return defaultSystemUnit.toSeconds(currentTime - epochTime) >= target
        }
        return false
    }
}
