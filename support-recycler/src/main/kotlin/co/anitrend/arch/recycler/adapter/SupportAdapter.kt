package co.anitrend.arch.recycler.adapter

import android.content.res.Resources
import androidx.recyclerview.widget.ConcatAdapter
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.AdapterController
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter

/**
 * Additional contract for common adapter behaviour
 */
interface SupportAdapter<T> : ISupportAdapter<T> {

    val resources: Resources

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
    ): ConcatAdapter = controller.withLoadStateHeader(header)

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateFooter
     */
    fun withLoadStateFooter(
        footer: SupportLoadStateAdapter
    ): ConcatAdapter = controller.withLoadStateFooter(footer)

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateHeaderAndFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter
    ): ConcatAdapter = controller.withLoadStateHeaderAndFooter(header, footer)

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