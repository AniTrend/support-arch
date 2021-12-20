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

package co.anitrend.arch.core.analytic.contract

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity

/**
 * Contract for analytics or crash reporting
 *
 * @since v1.0.X
 */
interface ISupportAnalytics {
    /**
     * Handles logging the current state of a visited screen
     */
    fun logCurrentScreen(context: FragmentActivity, tag: String)

    /**
     * Handles logging the current state of a visited screen using an explicit [bundle]
     */
    fun logCurrentState(tag: String, bundle: Bundle?)

    /**
     * Handles logging of exceptions to an analytic service
     */
    fun logException(throwable: Throwable)

    /**
     * Handles logging of an analytic service with the [priority] defaulted to [Log.WARN]
     */
    fun log(priority: Int = Log.WARN, tag: String?, message: String)

    /**
     * Clears any set parameters used for logging
     */
    fun clearCrashAnalyticsSession()

    /**
     * Set unique identifier for crashlytics, this could be a device model
     * associated with a user name or some other identifier
     */
    fun setCrashAnalyticIdentifier(identifier: String)
}
