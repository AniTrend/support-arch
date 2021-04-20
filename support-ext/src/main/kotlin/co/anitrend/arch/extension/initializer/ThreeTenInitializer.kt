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

package co.anitrend.arch.extension.initializer

import android.content.Context
import androidx.startup.Initializer
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Initialization of [Three Ten Android Backport](https://github.com/JakeWharton/ThreeTenABP)
 */
class ThreeTenInitializer : Initializer<Unit> {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        AndroidThreeTen.init(context)
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * e.g. if a [Initializer] `B` defines another
     * [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
