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

package co.anitrend.arch.recycler.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.SupportAdapterController
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [List]s and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @param differCallback Callback for calculating the diff between two non-null items in a list.
 * @param supportsStableIds Sets [PagedListAdapter.setHasStableIds] which is defaulted to true
 *
 * @since v1.2.0
 */
abstract class SupportListAdapter<T>(
    differCallback: DiffUtil.ItemCallback<T>,
    supportsStableIds: Boolean = false,
) : SupportAdapter<T>, ListAdapter<T, SupportViewHolder>(differCallback) {

    init {
        this.setHasStableIds(supportsStableIds)
    }

    /**
     * Internal use only indicator for checking against the use of a
     * concat adapter for headers and footers, which is in turn used
     * to figure out how to get the view holder id
     *
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     * @see withLoadStateHeaderAndFooter
     */
    override var isUsingConcatAdapter: Boolean = false

    override var lastAnimatedPosition: Int = 0

    override val controller by lazy {
        SupportAdapterController(this)
    }

    override val clickableFlow = MutableStateFlow<ClickableItem>(ClickableItem.None)

    /**
     * Returns the non-nullable item for this adapter
     *
     * @param position The current position of the adapter
     *
     * @throws IllegalArgumentException when the item is null
     * @throws IndexOutOfBoundsException when the [position] is invalid
     */
    override fun requireItem(position: Int): T {
        return requireNotNull(getItem(position))
    }

    /**
     * Return the stable ID for the item at [position]. If [hasStableIds]
     * would return false this method should return [RecyclerView.NO_ID].
     *
     * The default implementation of this method returns [RecyclerView.NO_ID].
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int): Long {
        return when (hasStableIds()) {
            true -> runCatching {
                getStableIdFor(requireItem(position))
            }.onFailure {
                Timber.v(it, "get item id -> position: $position")
            }.getOrDefault(RecyclerView.NO_ID)
            else -> super.getItemId(position)
        }
    }

    /**
     * Overridden implementation [createDefaultViewHolder] calls to resolve the view holder type
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int,
    ): SupportViewHolder {
        val layoutInflater = parent.context.getLayoutInflater()
        return createDefaultViewHolder(parent, viewType, layoutInflater)
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the adapter previously freed any resources in [onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    override fun onViewAttachedToWindow(holder: SupportViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.layoutParams?.also {
            val position = if (isUsingConcatAdapter) {
                holder.bindingAdapterPosition
            } else {
                holder.absoluteAdapterPosition
            }

            when (it is StaggeredGridLayoutManager.LayoutParams) {
                true -> setLayoutSpanSize(it, position)
                else -> {}
            }
        }
    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    override fun onViewDetachedFromWindow(holder: SupportViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
        // disable view holder recycling on detached from window
        // holder.onViewRecycled()
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     *
     * @see [onDetachedFromRecyclerView]
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        when (val layoutManager = recyclerView.layoutManager) {
            is GridLayoutManager -> setLayoutSpanSize(layoutManager)
        }
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        bindViewHolderByType(holder, position)
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(
        holder: SupportViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            bindViewHolderByType(holder, position, payloads)
        }
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     *
     * @see [SupportViewHolder.onViewRecycled]
     */
    override fun onViewRecycled(holder: SupportViewHolder) {
        holder.onViewRecycled()
    }

    /**
     * Return the view type of the item at [position] for the purposes of view recycling.
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * [position]. Type codes need not be contiguous.
     */
    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return ISupportAdapter.DEFAULT_VIEW_TYPE
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun notifyDataSetNeedsRefreshing() {
        notifyDataSetChanged()
    }

    /**
     * Notifies that `ON_PAUSE` event occurred.
     *
     * This method will be called before the [LifecycleOwner]'s `onPause` method
     * is called.
     *
     * @param owner the component, whose state was changed
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        // clear our state flow, when the lifecycle owner parent reaches its onPaused state
        clickableFlow.value = ClickableItem.None
    }
}
