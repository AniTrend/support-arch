package co.anitrend.arch.recycler.adapter.controller.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

interface ISupportAdapterController {

    /**
     * Creates a loading [IRecyclerItem]
     */
    fun getLoadingItem(configuration: IStateLayoutConfig) : IRecyclerItem

    /**
     * Creates an error [IRecyclerItem]
     */
    fun getErrorItem(configuration: IStateLayoutConfig) : IRecyclerItem

    /**
     * Creates a loading [SupportViewHolder] for [getLoadingItem]
     */
    fun getLoadingViewHolder(
        parent: ViewGroup,
        layoutInflater: LayoutInflater,
        stateConfig: IStateLayoutConfig
    ) : SupportViewHolder

    /**
     * Creates an error [SupportViewHolder] for [getErrorItem]
     */
    fun getErrorViewHolder(
        parent: ViewGroup,
        layoutInflater: LayoutInflater,
        stateConfig: IStateLayoutConfig
    ) : SupportViewHolder

    fun onLoadStateChanged(
        previousState: LoadState?,
        currentState: LoadState?,
        hasExtraRow: Boolean,
        hadExtraRow: Boolean
    )

    fun bindViewHolderByType(
        stateConfig: IStateLayoutConfig,
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any>,
    )
}