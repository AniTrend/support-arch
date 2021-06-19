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

package co.anitrend.arch.recycler.adapter.contract

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/**
 * Contract for recycler view adapters
 */
interface ISupportAdapter<T> : SupportLifecycle {

    var lastAnimatedPosition: Int

    val resources: Resources

    /**
     * Mapper for adapters to converting models to recycler items
     */
    val mapper: (T) -> IRecyclerItem

    /**
     * Get currently set animation type for recycler view holder items
     *
     * @see [AbstractAnimator]
     */
    val customSupportAnimator: AbstractAnimator?

    /**
     * An observer to listen for clicks on clickable items
     */
    val clickableFlow: Flow<ClickableItem>

    /**
     * Configuration for the state based footer
     */
    val stateConfiguration: IStateLayoutConfig

    /**
     * Assigned if the current adapter supports needs to supports action mode
     */
    val supportAction: ISupportSelectionMode<Long>?

    /**
     * Returns the non-nullable item for this adapter
     *
     * @param position The current position of the adapter
     *
     * @throws IllegalArgumentException when the item is null
     * @throws IndexOutOfBoundsException when the [position] is invalid
     */
    @Throws(IllegalArgumentException::class, IndexOutOfBoundsException::class)
    fun requireItem(position: Int): T

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    fun getStableIdFor(item: T?): Long {
        // Stable Ids are no longer supported by default prior to paging v3.0 migration
        return RecyclerView.NO_ID
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    fun createDefaultViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder

    /**
     * Should return the span size for the item at [position], when called from
     * [androidx.recyclerview.widget.GridLayoutManager] [spanCount] will be the span
     * size for the item at the [position].
     *
     * Otherwise if called from [androidx.recyclerview.widget.StaggeredGridLayoutManager]
     * then [spanCount] may be null
     *
     * @param position item position in the adapter
     * @param spanCount current span count for the layout manager
     *
     * @see co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
     */
    fun getSpanSizeForItemAt(position: Int, spanCount: Int?): Int? {
        return try {
            val item = mapper(requireItem(position))
            val spanSize = spanCount ?: IRecyclerItemSpan.INVALID_SPAN_COUNT
            item.getSpanSize(spanSize, position, resources)
        } catch (exception: Exception) {
            Timber.w(exception, "span size for -> position: $position | spanCount: $spanCount")
            // We don't know which span size to use, so we'll just take up the entire span size
            spanCount ?: FULL_SPAN_SIZE
        }
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutManager grid layout manage for your recycler
     */
    fun setLayoutSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                getSpanSizeForItemAt(
                    position, layoutManager.spanCount
                ) ?: layoutManager.spanCount
        }
    }

    /**
     * Applies an animation on a [SupportViewHolder] when it is seen for the first time
     */
    fun animateViewHolder(holder: SupportViewHolder?, position: Int) {
        holder?.apply {
            when (position > lastAnimatedPosition) {
                true -> customSupportAnimator?.also { supportAnimator ->
                    supportAnimator.getAnimators(itemView).forEach { animator ->
                        with(animator) {
                            duration = supportAnimator.animationDuration.runtime
                            interpolator = supportAnimator.interpolator
                            start()
                        }
                    }
                }
            }
            lastAnimatedPosition = position
        }
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutParams StaggeredGridLayoutManager.LayoutParams for your recycler
     */
    fun setLayoutSpanSize(layoutParams: StaggeredGridLayoutManager.LayoutParams, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val spanCount = getSpanSizeForItemAt(
                position, layoutParams.spanIndex
            ) ?: 0
            layoutParams.isFullSpan = spanCount == FULL_SPAN_SIZE
        } else {
            Timber.d("Adapter position RecyclerView.NO_POSITION")
        }
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    fun notifyDataSetNeedsRefreshing()

    /**
     * Binds view holder by view type at [position]
     */
    fun bindViewHolderByType(
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any> = emptyList()
    ) {
        runCatching {
            if (position != RecyclerView.NO_POSITION) {
                val recyclerItem: IRecyclerItem = mapper(requireItem(position))
                val mutableFlow = clickableFlow as MutableStateFlow
                holder.bind(
                    position = position,
                    payloads = payloads,
                    model = recyclerItem,
                    stateFlow = mutableFlow,
                    selectionMode = supportAction
                )
                if (payloads.isEmpty())
                    animateViewHolder(holder, position)
            }
        }.onFailure { throwable ->
            Timber.w(
                throwable,
                "holder: $holder, position: $position, payloads: [${payloads.joinToString()}]"
            )
        }
    }

    companion object {
        const val FULL_SPAN_SIZE = 1
        const val DEFAULT_VIEW_TYPE = 0
    }
}
