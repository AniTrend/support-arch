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

package co.anitrend.arch.recycler.model.contract

import android.content.res.Resources

/**
 * Contract for recycler item with span query support
 *
 * @since v1.3.0
 */
interface IRecyclerItemSpan {

    /**
     * Provides a preferred span size for the item
     *
     * @param spanCount current span count which may also be [IRecyclerItemSpan.INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ): Int

    companion object {
        const val INVALID_SPAN_COUNT = -1
    }
}
