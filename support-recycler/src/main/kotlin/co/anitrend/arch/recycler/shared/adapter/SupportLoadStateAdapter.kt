package co.anitrend.arch.recycler.shared.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
import co.anitrend.arch.recycler.shared.model.SupportDefaultItem
import co.anitrend.arch.recycler.shared.model.SupportErrorItem
import co.anitrend.arch.recycler.shared.model.SupportLoadingItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

open class SupportLoadStateAdapter(
    private val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val mapper: (LoadState) -> IRecyclerItem = {
        when (it) {
            is LoadState.Error -> SupportErrorItem(it, stateConfiguration)
            is LoadState.Loading -> SupportLoadingItem(stateConfiguration)
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
                    notifyItemChanged(0)
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

    final override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return when (loadState) {
            is LoadState.Loading -> R.layout.support_layout_state_loading
            is LoadState.Error -> R.layout.support_layout_state_error
            else -> R.layout.support_layout_state_default
        }
    }

    /**
     * Overridden implementation [createDefaultViewHolder] calls to resolve the view holder type
     */
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): SupportViewHolder {
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
    override fun getSpanSizeForItemAt(position: Int, spanCount: Int?): Int {
        return runCatching {
            val item = mapper(loadState)
            val spanSize = spanCount ?: IRecyclerItemSpan.INVALID_SPAN_COUNT
            item.getSpanSize(spanSize, position, resources)
        }.getOrElse {
            Timber.w(it, "Span size: $spanCount")
            // we don't know which span size to use so we use the supplied or default full size
            spanCount ?: ISupportAdapter.FULL_SPAN_SIZE
        }
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun notifyDataSetNeedsRefreshing() {
        notifyItemChanged(0)
    }

    /**
     * Binds view holder by view type at [position]
     */
    override fun bindViewHolderByType(
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        val recyclerItem: IRecyclerItem = mapper(loadState)
        runCatching {
            holder.bind(position, payloads, recyclerItem, clickableFlow, supportAction)
            animateViewHolder(holder, position)
        }.onFailure { throwable ->
            Timber.w(throwable, "bindViewHolderByType(holder: .., position: .., payloads: ..)")
        }
    }

    override fun onPause() {
        super.onPause()
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