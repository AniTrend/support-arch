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

package co.anitrend.arch.extension.util.time

import co.anitrend.arch.extension.annotation.SupportExperimental
import java.util.concurrent.TimeUnit

/**
 * Holder for constructing time units
 *
 * @param currentTimeInstant time to convert using the given [SupportDateTimeUnit]
 * @param referenceTimeInstant type of comparison between itself and the [currentTimeInstant] time
 *
 * @since v1.2.0
 */
@SupportExperimental
class SupportTimeHelper(
    private val currentTimeInstant: SupportTimeInstant,
    private val referenceTimeInstant: SupportTime
) {
    private val defaultSystemUnit = TimeUnit.MILLISECONDS

    /**
     * Checks if the time unix timestamp has a difference greater than or equal to the
     * [referenceTimeInstant] which has a measurement unit of [SupportDateTimeUnit]
     */
    fun hasElapsed(timeInstant: SupportTimeInstant): Boolean {
        return when (referenceTimeInstant.unit) {
            SupportDateTimeUnit.TIME_UNIT_DAYS ->
                defaultSystemUnit.toDays(
                    currentTimeInstant - timeInstant
                ) >= referenceTimeInstant.instant
            SupportDateTimeUnit.TIME_UNIT_HOURS ->
                defaultSystemUnit.toHours(
                    currentTimeInstant - timeInstant
                ) >= referenceTimeInstant.instant
            SupportDateTimeUnit.TIME_UNIT_MINUTES ->
                defaultSystemUnit.toMinutes(
                    currentTimeInstant - timeInstant
                ) >= referenceTimeInstant.instant
            SupportDateTimeUnit.TIME_UNIT_SECONDS ->
                defaultSystemUnit.toSeconds(
                    currentTimeInstant - timeInstant
                ) >= referenceTimeInstant.instant
        }
    }

    data class SupportTime(
        val instant: Int,
        val unit: SupportDateTimeUnit
    )
}

typealias SupportTimeInstant = Long
