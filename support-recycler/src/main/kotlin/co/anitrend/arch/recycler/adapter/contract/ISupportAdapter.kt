package co.anitrend.arch.recycler.adapter.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.theme.animator.contract.ISupportAnimator
import kotlinx.coroutines.flow.Flow

/**
 * Contract for recycler view adapters
 */
interface ISupportAdapter<T> : SupportLifecycle {

    var lastAnimatedPosition: Int

    /**
     * Get currently set animation type for recycler view holder items
     *
     * @see [ISupportAnimator]
     */
    var customSupportAnimator: ISupportAnimator?

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
    var supportAction: ISupportSelectionMode<Long>?

    /**
     * Network state which will be used by [co.anitrend.arch.recycler.shared.SupportFooterErrorItem]
     */
    var networkState: NetworkState?

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    fun getStableIdFor(item: T?): Long

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
     * Checks if current network state represents an additional row of data
     */
    fun hasExtraRow() = networkState != null &&
            (networkState is NetworkState.Loading || networkState is NetworkState.Error)

    /**
     * Returns a boolean to instruct the [GridLayoutManager] if an item at the position should
     * use a span size count of [FULL_SPAN_SIZE] otherwise defaults to the intended size
     *
     * @param position recycler position being rendered
     * @param spanCount current size of the span count from the layout manager
     *
     * @see setLayoutSpanSize
     */
    fun isFullSpanItem(position: Int, spanCount: Int): Boolean

    /**
     * Should return the span size for the item at [position]
     *
     * @return span size or null if you want to use the layout manager span count
     *
     * @see co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
     */
    fun getSpanSizeForItemAt(position: Int): Int?

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutManager grid layout manage for your recycler
     */
    fun setLayoutSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when {
                isFullSpanItem(position, layoutManager.spanCount) -> FULL_SPAN_SIZE
                else -> getSpanSizeForItemAt(position) ?: layoutManager.spanCount
            }
        }
    }


    fun animateViewHolder(holder: SupportViewHolder?, position: Int) {
        holder?.apply {
            when (position > lastAnimatedPosition) {
                true -> customSupportAnimator?.also { supportAnimator ->
                    supportAnimator.getAnimators(itemView).forEach { animator ->
                        with(animator) {
                            duration = supportAnimator.getAnimationDuration().duration
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
        if (isFullSpanItem(position, layoutParams.spanIndex))
            layoutParams.isFullSpan = true
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

    /**
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onDestroy() {
        super.onDestroy()
        supportAction = null
    }

    companion object {
        const val FULL_SPAN_SIZE = 1
    }
}