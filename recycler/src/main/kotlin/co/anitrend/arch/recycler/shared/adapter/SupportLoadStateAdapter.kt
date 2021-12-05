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

package co.anitrend.arch.recycler.shared.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.shared.model.SupportDefaultItem
import co.anitrend.arch.recycler.shared.model.SupportErrorItem
import co.anitrend.arch.recycler.shared.model.SupportLoadingItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import kotlinx.coroutines.flow.MutableStateFlow

open class SupportLoadStateAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val mapper: (LoadState) -> IRecyclerItem = {
        when (it) {
            is LoadState.Loading -> SupportLoadingItem(stateConfiguration)
            is LoadState.Error -> SupportErrorItem(it, stateConfiguration)
            else -> SupportDefaultItem(it, stateConfiguration)
        }
    },
    override val customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val supportAction: ISupportSelectionMode<Long>? = null
) : ISupportAdapter<LoadState>, ListAdapter<LoadState, SupportViewHolder>(DIFF_CALLBACK) {

    var loadState: LoadState = LoadState.Idle()
        set(loadState) {
            if (field != loadState) {
                val oldItem = displayLoadStateAsItem(field)
                val newItem = displayLoadStateAsItem(loadState)

                if (oldItem && !newItem) {
                    notifyItemRemoved(0)
                } else if (newItem && !oldItem) {
                    notifyItemInserted(0)
                } else if (oldItem && newItem) {
                    notifyDataSetNeedsRefreshing()
                }
                field = loadState
            }
        }

    override var lastAnimatedPosition: Int = 0

    override val clickableFlow = MutableStateFlow<ClickableItem>(ClickableItem.None)

    /**
     * Returns true if the LoadState should be displayed as a list item when active.
     *
     * By default, [LoadState.Loading] and [LoadState.Error] present as list items, others do not.
     */
    open fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error
    }

    /**
     * Returns the non-nullable item for this adapter
     *
     * @param position The current position of the adapter
     *
     * @throws IllegalArgumentException when the item is null
     * @throws IndexOutOfBoundsException when the [position] is invalid
     */
    override fun requireItem(position: Int) = loadState

    final override fun getItemCount(): Int {
        val state = requireItem(RecyclerView.NO_POSITION)
        val isLoadStateItem = displayLoadStateAsItem(state)
        return if (isLoadStateItem) 1 else 0
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int): Int {
        return when (requireItem(position)) {
            is LoadState.Loading -> R.layout.support_layout_state_loading
            is LoadState.Error -> R.layout.support_layout_state_error
            else -> R.layout.support_layout_state_default
        }
    }

    /**
     * Overridden implementation [createDefaultViewHolder] calls to resolve the view holder type
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): SupportViewHolder {
        val layoutInflater = parent.context.getLayoutInflater()
        return createDefaultViewHolder(parent, viewType, layoutInflater)
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
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = when (viewType) {
        R.layout.support_layout_state_loading ->
            SupportLoadingItem.createViewHolder(parent, layoutInflater)
        R.layout.support_layout_state_error ->
            SupportErrorItem.createViewHolder(parent, layoutInflater)
        else -> SupportDefaultItem.createViewHolder(parent, layoutInflater)
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun notifyDataSetNeedsRefreshing() {
        notifyItemChanged(0)
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
        clickableFlow.value = ClickableItem.None
    }

    internal companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LoadState>() {
            override fun areItemsTheSame(
                oldItem: LoadState,
                newItem: LoadState
            ) = oldItem.position == newItem.position

            override fun areContentsTheSame(
                oldItem: LoadState,
                newItem: LoadState
            ) = oldItem.hashCode() == newItem.hashCode()
        }
    }
}
