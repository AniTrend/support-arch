package co.anitrend.arch.extension.util.date

import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.contract.ISupportDateHelper
import java.util.*

/**
 * Date helper utility
 *
 * @param context application context
 * @since v1.1.0
 */
class SupportDateHelper : ISupportDateHelper {

    /**
     * @return current seasons name
     */
    val currentSeason: SeasonType
         get() {
            return when (Calendar.getInstance().get(Calendar.MONTH)) {
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
        val calendar =  Calendar.getInstance()
        return if (calendar.get(Calendar.MONTH) >= 11 && currentSeason == SeasonType.WINTER)
            calendar.get(Calendar.YEAR) + delta
        else calendar.get(Calendar.YEAR)
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
    )
}