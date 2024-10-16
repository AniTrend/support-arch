/**
 * Copyright 2023 AniTrend
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

package co.anitrend.arch.recycler.paging.legacy.extensions

import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter

/**
 * Check if [ISupportAdapter] -> [PagedListAdapter] contains any list items
 *
 * @param emptyCount numerical representation of what an empty adapter should be
 *
 * @return [Boolean] indicating if internal list is empty
 */
fun ISupportAdapter<*>.isEmpty(emptyCount: Int = 0): Boolean =
    when (this) {
        is PagedListAdapter<*, *> -> itemCount == emptyCount
        is PagingDataAdapter<*, *> -> itemCount == emptyCount
        else -> throw NotImplementedError(
            "Not sure how to request item count from: $this",
        )
    }
