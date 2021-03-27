package co.anitrend.arch.extension.util.date

import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import java.util.*

/**
 * Date helper utility
 *
 * @since v1.1.0
 */
class SupportDateHelper : AbstractSupportDateHelper() {

    /**
     * @return current seasons name
     */
    override val currentSeason: SeasonType
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
    override fun getCurrentYear(delta: Int): Int {
        val calendar =  Calendar.getInstance()
        return if (calendar.get(Calendar.MONTH) >= 11 && currentSeason == SeasonType.WINTER)
            calendar.get(Calendar.YEAR) + delta
        else calendar.get(Calendar.YEAR)
    }
}