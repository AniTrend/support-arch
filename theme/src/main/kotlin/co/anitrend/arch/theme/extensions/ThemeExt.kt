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

package co.anitrend.arch.theme.extensions

import android.content.Context
import co.anitrend.arch.theme.R

/**
 * Check if the system is in night mode
 */
fun Context.isEnvironmentNightMode() =
    resources.getBoolean(R.bool.isNightMode)

/**
 * Check if the system should use light status bar
 */
fun Context.isLightStatusBar() =
    resources.getBoolean(R.bool.isLightStatusBar)

/**
 * Check if the system should use light navigation bar
 */
fun Context.isLightNavigationBar() =
    resources.getBoolean(R.bool.isLightNavigationBar)
