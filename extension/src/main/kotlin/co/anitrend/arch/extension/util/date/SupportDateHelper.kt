/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.extension.util.date

import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import java.util.Calendar

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
        val calendar = Calendar.getInstance()
        return if (calendar.get(Calendar.MONTH) >= 11 && currentSeason == SeasonType.WINTER) {
            calendar.get(Calendar.YEAR) + delta
        } else {
            calendar.get(Calendar.YEAR)
        }
    }
}
