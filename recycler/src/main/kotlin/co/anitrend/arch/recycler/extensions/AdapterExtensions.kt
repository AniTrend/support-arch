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

package co.anitrend.arch.recycler.extensions

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter

/**
 * Check if adapter is empty
 */
fun ISupportAdapter<*>.isEmpty(): Boolean {
    val count = when (this) {
        is ListAdapter<*, *> -> itemCount
        is PagedListAdapter<*, *> -> itemCount
        is RecyclerView.Adapter<*> -> itemCount
        else -> throw NotImplementedError(
            "Not sure how to request item count from: $this",
        )
    }
    return count < 1
}
