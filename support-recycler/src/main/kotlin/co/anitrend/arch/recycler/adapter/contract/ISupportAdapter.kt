package co.anitrend.arch.recycler.adapter.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for recycler view adapters
 */
interface ISupportAdapter<T> : SupportLifecycle {

    var lastAnimatedPosition: Int

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
    val clickableStateFlow: StateFlow<ClickableItem?>

    /**
     * Configuration for the state based footer
     */
    val stateConfiguration: IStateLayoutConfig

    /**
     * Assigned if the current adapter supports needs to supports action mode
     */
    val supportAction: ISupportSelectionMode<Long>?

    /**
     * Network state which will be used by [co.anitrend.arch.recycler.shared.SupportErrorItem]
     */
    var networkState: NetworkState?

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    fun getStableIdFor(item: T?): Long {
        //Stable Ids are no longer supported by default prior to paging v3.0 migration
        return RecyclerView.NO_ID
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    fun createDefaultViewHolder(
        parent: ViewGroup, @LayoutRes viewType: Int, layoutInflater: LayoutInflater
    ): SupportViewHolder

    /**
     * Returns a boolean indicating whether or not the adapter had data, and caters for [hasExtraRow]
     *
     * @return [Boolean]
     * @see hasExtraRow
     */
    fun isEmpty(): Boolean

    /**
     * Fetches the non-nullable item of the underlying list with-in the adapter
     *
     * @param position Index of the item to get
     *
     * @throws IllegalStateException
     */
    fun requireItem(position: Int): T

    /**
     * Informs us if the given [position] is within bounds of our underlying collection
     */
    fun isWithinIndexBounds(position: Int): Boolean

    /**
     * Checks if current network state represents an additional row of data
     */
    fun hasExtraRow() = networkState != null &&
            (networkState is NetworkState.Loading || networkState is NetworkState.Error)

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
    fun getSpanSizeForItemAt(position: Int, spanCount: Int?): Int?

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
        val spanCount = getSpanSizeForItemAt(
            position, layoutParams.spanIndex
        ) ?: 0
        layoutParams.isFullSpan = spanCount == FULL_SPAN_SIZE
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    fun updateSelection()

    /**
     * Binds view holder by view type at [position]
     */
    fun bindViewHolderByType(
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any> = emptyList()
    )

    companion object {
        const val FULL_SPAN_SIZE = 1
        const val DEFAULT_VIEW_TYPE = 0
    }
}