package co.anitrend.arch.recycler.adapter.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.annotation.SupportExperimental
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.ISupportAdapterController
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.shared.SupportErrorItem
import co.anitrend.arch.recycler.shared.SupportLoadingItem
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

open class SupportAdapterController<T>(
    private val recyclerAdapter: RecyclerView.Adapter<*>,
    private val adapter: ISupportAdapter<T>
) : ISupportAdapterController {

    /**
     * Dispatches clicks from parent views
     */
    internal val actionStateFlow =
        MutableStateFlow<ClickableItem?>(null)

    /**
     * Creates a loading [IRecyclerItem]
     */
    override fun getLoadingItem(
        configuration: IStateLayoutConfig
    ) : IRecyclerItem = SupportLoadingItem(configuration)

    /**
     * Creates an error [IRecyclerItem]
     */
    override fun getErrorItem(
        configuration: IStateLayoutConfig
    ) : IRecyclerItem = SupportErrorItem(adapter.loadState, configuration)

    /**
     * Creates a loading [SupportViewHolder] for [getLoadingItem]
     */
    override fun getLoadingViewHolder(
        parent: ViewGroup,
        layoutInflater: LayoutInflater,
        stateConfig: IStateLayoutConfig
    ) : SupportViewHolder {
        val viewHolder = SupportLoadingItem.createViewHolder(parent, layoutInflater)
        viewHolder.bind(RecyclerView.NO_POSITION, emptyList(), getLoadingItem(stateConfig), actionStateFlow)
        return viewHolder
    }

    /**
     * Creates an error [SupportViewHolder] for [getErrorItem]
     */
    override fun getErrorViewHolder(
        parent: ViewGroup,
        layoutInflater: LayoutInflater,
        stateConfig: IStateLayoutConfig
    ) : SupportViewHolder {
        val viewHolder = SupportErrorItem.createViewHolder(parent, layoutInflater)
        viewHolder.bind(RecyclerView.NO_POSITION, emptyList(), getErrorItem(stateConfig), actionStateFlow)
        return viewHolder
    }

    @SupportExperimental
    private fun getPositionForStateChanged(
        previousState: LoadState?,
        currentState: LoadState?,
        offset: Int = 0
    ): Int {
        // This is a work in progress
        if (previousState == null && currentState is LoadState.Loading) {
            return if (currentState.position == LoadState.Loading.Position.TOP)
                1 - offset
            else recyclerAdapter.itemCount
        }

        if (
            previousState is LoadState.Loading &&
            previousState.position == LoadState.Loading.Position.TOP
        ) return 1 - offset

        return recyclerAdapter.itemCount - offset
    }

    override fun onLoadStateChanged(
        previousState: LoadState?,
        currentState: LoadState?,
        hasExtraRow: Boolean,
        hadExtraRow: Boolean
    ) {
        Timber.d(
            "onLoadStateChanged(...) -> Previous state: $previousState | Current state: $currentState"
        )
        val rowHasChanged = hadExtraRow != hasExtraRow
        val stateHasChanged = previousState != currentState
        val itemCount = recyclerAdapter.itemCount
        if (rowHasChanged) {
            if (hadExtraRow)
                recyclerAdapter.notifyItemRemoved(itemCount)
            else
                recyclerAdapter.notifyItemInserted(itemCount)
        }
        else if (hasExtraRow && stateHasChanged) {
            recyclerAdapter.notifyItemChanged(itemCount - 1)
        }
    }

    override fun bindViewHolderByType(
        stateConfig: IStateLayoutConfig,
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        val recyclerItem: IRecyclerItem = when (recyclerAdapter.getItemViewType(position)) {
            R.layout.support_layout_state_loading -> getLoadingItem(stateConfig)
            R.layout.support_layout_state_error -> getErrorItem(stateConfig)
            else -> adapter.mapper(adapter.requireItem(position))
        }

        runCatching {
            holder.bind(
                position,
                payloads,
                recyclerItem,
                actionStateFlow,
                adapter.supportAction
            )
            adapter.animateViewHolder(holder, position)
        }.onFailure { throwable ->
            Timber.tag(adapter.moduleTag).w(
                throwable,
                "bindViewHolderByType(holder: .., position: .., payloads: ..)"
            )
        }
    }
}