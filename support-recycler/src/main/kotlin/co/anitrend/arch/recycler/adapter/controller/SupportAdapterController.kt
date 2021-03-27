package co.anitrend.arch.recycler.adapter.controller

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.AdapterController
import co.anitrend.arch.recycler.adapter.controller.contract.StateListener
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.recycler.state.LoadStateManager
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Adapter controller for handling complex adapter logic
 */
class SupportAdapterController<T>(
    private val adapter: ISupportAdapter<T>
) : AdapterController() {

    override val loadStateListeners: MutableList<StateListener> = CopyOnWriteArrayList()

    override val loadStateManager: LoadStateManager = object : LoadStateManager() {
        override fun onStateChanged(state: LoadState) {
            loadStateListeners.forEach {
                it(state.position, state)
            }
        }
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.TOP] [LoadState] as a list item at the end of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    override fun withLoadStateHeader(header: SupportLoadStateAdapter): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            if (loadPosition == LoadState.Position.TOP)
                header.loadState = loadState
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(header, adapter)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.BOTTOM] [LoadState] as a list item at the start of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    override fun withLoadStateFooter(footer: SupportLoadStateAdapter): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            if (loadPosition == LoadState.Position.BOTTOM)
                footer.loadState = loadState
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(adapter , footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.TOP] and [LoadState.Loading.Position.BOTTOM] [LoadState]s
     * as list items at the start and end respectively.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    override fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter
    ): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            if (loadPosition == LoadState.Position.TOP)
                header.loadState = loadState
            if (loadPosition == LoadState.Position.BOTTOM)
                footer.loadState = loadState
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(header, adapter, footer)
    }
}