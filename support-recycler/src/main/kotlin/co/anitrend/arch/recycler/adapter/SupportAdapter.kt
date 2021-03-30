package co.anitrend.arch.recycler.adapter

import androidx.recyclerview.widget.ConcatAdapter
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.AdapterController
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter

/**
 * Additional contract for common adapter behaviour
 */
interface SupportAdapter<T> : ISupportAdapter<T> {

    /**
     * Internal use only indicator for checking against the use of a
     * concat adapter for headers and footers, which is in turn used
     * to figure out how to get the view holder id
     *
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     * @see withLoadStateHeaderAndFooter
     */
    var isUsingConcatAdapter: Boolean

    /**
     * Special dispatcher controller for custom logic handling
     */
    val controller: AdapterController

    /**
     * Sets loading state
     */
    fun setLoadState(state: LoadState) {
        controller.onLoadStateChanged(state)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateHeader
     */
    fun withLoadStateHeader(
        header: SupportLoadStateAdapter
    ): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateHeader(header)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateFooter
     */
    fun withLoadStateFooter(
        footer: SupportLoadStateAdapter
    ): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateFooter(footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateHeaderAndFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter
    ): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateHeaderAndFooter(header, footer)
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onPause() {
        super.onPause()
        controller.onPause()
    }
}