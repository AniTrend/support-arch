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

package co.anitrend.arch.recycler.observer

import androidx.recyclerview.widget.RecyclerView

/**
 * Default implementation of an observer proxy for relying recycler adapter updates
 *
 * @param adapterDataObserver the observer from [RecyclerView.Adapter.registerAdapterDataObserver]
 * @param additionalViewAdapterViewCount number of additional views that could be rendered
 *
 * @since v1.3.0
 * @see RecyclerView.AdapterDataObserver
 * @see RecyclerView.Adapter.registerAdapterDataObserver
 */
class SupportObserverProxy(
    private val adapterDataObserver: RecyclerView.AdapterDataObserver,
    private val additionalViewAdapterViewCount: Int,
) : RecyclerView.AdapterDataObserver() {
    override fun onChanged() {
        adapterDataObserver.onChanged()
    }

    override fun onItemRangeRemoved(
        positionStart: Int,
        itemCount: Int,
    ) {
        adapterDataObserver.onItemRangeRemoved(
            positionStart + additionalViewAdapterViewCount,
            itemCount,
        )
    }

    override fun onItemRangeMoved(
        fromPosition: Int,
        toPosition: Int,
        itemCount: Int,
    ) {
        adapterDataObserver.onItemRangeMoved(
            fromPosition + additionalViewAdapterViewCount,
            toPosition,
            itemCount,
        )
    }

    override fun onItemRangeInserted(
        positionStart: Int,
        itemCount: Int,
    ) {
        adapterDataObserver.onItemRangeInserted(
            positionStart + additionalViewAdapterViewCount,
            itemCount,
        )
    }

    override fun onItemRangeChanged(
        positionStart: Int,
        itemCount: Int,
    ) {
        adapterDataObserver.onItemRangeChanged(
            positionStart + additionalViewAdapterViewCount,
            itemCount,
        )
    }

    override fun onItemRangeChanged(
        positionStart: Int,
        itemCount: Int,
        payload: Any?,
    ) {
        adapterDataObserver.onItemRangeChanged(
            positionStart + additionalViewAdapterViewCount,
            itemCount,
            payload,
        )
    }
}
