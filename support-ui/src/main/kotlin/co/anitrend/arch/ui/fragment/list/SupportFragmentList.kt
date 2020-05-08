package co.anitrend.arch.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.common.FooterClickableItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.extension.configureWidgetBehaviorWith
import co.anitrend.arch.ui.extension.onResponseResetStates
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

/**
 * Core implementation for fragments that rely on paginated/non-paginated data sets
 * using standard [List] collections
 *
 * @since v1.2.0
 * @see SupportFragment
 * @see ISupportFragmentList
 */
abstract class SupportFragmentList<M: IRecyclerItem>(
    override val inflateLayout: Int = R.layout.support_list
) : SupportFragment(), ISupportFragmentList<M> {

    protected var supportStateLayout: SupportStateLayout? = null
    protected var supportRefreshLayout: SwipeRefreshLayout? = null
    protected var supportRecyclerView: SupportRecyclerView? = null

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    abstract fun onFetchDataInitialize()

    private fun onStateObserverChanged(networkState: NetworkState) {
        when (!supportViewAdapter.isEmpty()) {
            true -> {
                // to assure that the state layout is not blocking the current view
                supportStateLayout?.setNetworkState(
                    NetworkState.Success
                )
                supportViewAdapter.networkState = networkState
            }
            false -> changeLayoutState(networkState)
        }
    }

    override val onRefreshObserver = Observer<NetworkState> {
        onStateObserverChanged(it)
    }

    override val onNetworkObserver = Observer<NetworkState> {
        onStateObserverChanged(it)
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    protected fun resetWidgetStates() =
        supportRefreshLayout?.onResponseResetStates()

    /**
     * Sets a layout manager to the recycler view
     */
    override fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView) {
        if (recyclerView.layoutManager == null)
            recyclerView.layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.single_list_size),
                StaggeredGridLayoutManager.VERTICAL
            )
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = supportViewAdapter.let {
                it as RecyclerView.Adapter<*>
                it.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
                it
            }
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    @FlowPreview
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            supportViewAdapter.clickableFlow
                .debounce(16)
                .filterIsInstance<FooterClickableItem>()
                .collect {
                    if (supportStateLayout?.isLoading != true)
                        viewModelState()?.retry()
                    else
                        Timber.tag(moduleTag).d(
                            "retryFooterAction -> supportStateLayout is currently loading"
                        )
                }

            supportStateLayout?.interactionFlow
                ?.debounce(16)
                ?.collect {
                    if (supportStateLayout?.isLoading != true)
                        viewModelState()?.retry()
                    else
                        Timber.tag(moduleTag).d(
                            "onStateLayoutObserver -> supportStateLayout is currently loading"
                        )
                }
        }
        lifecycleScope.launchWhenStarted {
            if (supportViewAdapter.isEmpty())
                onFetchDataInitialize()
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation). This will be called between
     * [onCreate] and [onActivityCreated].
     *
     * If you return a View from here, you will later be called in
     * [onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)?.apply {
            supportStateLayout = findViewById(R.id.supportStateLayout)
            supportRefreshLayout = findViewById(R.id.supportRefreshLayout)
            supportRecyclerView = findViewById(R.id.supportRecyclerView)
        }
        supportStateLayout?.stateConfig = stateConfig

        supportRefreshLayout?.apply {
            configureWidgetBehaviorWith()
            setOnRefreshListener(this@SupportFragmentList)
        }

        supportRecyclerView?.apply {
            setHasFixedSize(true)
            setRecyclerAdapter(this)
            setRecyclerLayoutManager(this)
            isNestedScrollingEnabled = true
        }

        return view
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created. The fragment's view hierarchy
     * is not however attached to its parent at this point.
     *
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelObserver()
        viewModelState()?.networkState?.observe(viewLifecycleOwner, onNetworkObserver)
        viewModelState()?.refreshState?.observe(viewLifecycleOwner, onRefreshObserver)
    }

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [NetworkState]
     *
     * @param networkState New state from the application
     */
    override fun changeLayoutState(networkState: NetworkState?) {
        if (supportViewAdapter.hasExtraRow() || networkState !is NetworkState.Error) {
            supportStateLayout?.setNetworkState(NetworkState.Success)
            supportViewAdapter.networkState = networkState
        }
        else {
            supportStateLayout?.setNetworkState(
                networkState
            )
        }

        resetWidgetStates()
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        viewModelState()?.refresh()
    }

    protected fun afterPostModelChange(data: Collection<*>?) {
        /**
         * TODO: We may need to re-work this segment
         *
         * We are assuming that if no data exists in the first pass we might have more data
         * coming through from a network source for example. As such we react by setting our
         * adapters network state to loading
         */
        if (!data.isNullOrEmpty())
            supportStateLayout?.setNetworkState(NetworkState.Success)
        else if (supportViewAdapter.hasExtraRow()) {
            supportStateLayout?.setNetworkState(NetworkState.Success)
            supportViewAdapter.networkState = NetworkState.Loading
        }

        resetWidgetStates()
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model list holding data
     */
    open fun onPostModelChange(model: Collection<IRecyclerItem>?) {
        with (supportViewAdapter as SupportListAdapter) {
            model as List<IRecyclerItem>?
            submitList(model)
        }

        afterPostModelChange(model)
    }
}