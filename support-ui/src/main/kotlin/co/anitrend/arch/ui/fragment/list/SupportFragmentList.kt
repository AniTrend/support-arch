package co.anitrend.arch.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.extension.configureWidgetBehaviorWith
import co.anitrend.arch.ui.extension.onResponseResetStates
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.adapter.SupportListAdapter
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import timber.log.Timber

/**
 * Core implementation for fragments that rely on paginated/non-paginated data sets
 * using standard [List] collections
 *
 * @since v1.2.0
 * @see SupportFragment
 * @see ISupportFragmentList
 */
abstract class SupportFragmentList<M>(
    override val inflateLayout: Int = R.layout.support_list
) : SupportFragment<M>(), ISupportFragmentList<M> {

    protected var supportStateLayout: SupportStateLayout? = null
    protected var supportRefreshLayout: SwipeRefreshLayout? = null
    protected var supportRecyclerView: SupportRecyclerView? = null

    abstract override val supportViewAdapter: ISupportViewAdapter<M>

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

    override val onStateLayoutObserver = Observer<View> {
        if (supportStateLayout?.isLoading != true)
            viewModelState()?.retry()
        else
            Timber.tag(moduleTag).d(
                "onStateLayoutObserver -> supportStateLayout is currently loading"
            )
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
                resources.getInteger(columnSize),
                StaggeredGridLayoutManager.VERTICAL
            )
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = supportViewAdapter.let {
                if (it.supportAction == null)
                    it.supportAction = supportAction

                it as SupportListAdapter<M>
            }
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

        supportViewAdapter.retryFooterAction = View.OnClickListener {
            if (supportStateLayout?.isLoading != true)
                viewModelState()?.retry()
            else
                Timber.tag(moduleTag).d(
                    "retryFooterAction -> supportStateLayout is currently loading"
                )
        }

        supportRefreshLayout?.apply {
            configureWidgetBehaviorWith(activity)
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
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelObserver()
        viewModelState()?.networkState?.observe(viewLifecycleOwner, onNetworkObserver)
        viewModelState()?.refreshState?.observe(viewLifecycleOwner, onRefreshObserver)
        supportStateLayout?.interactionLiveData?.observe(viewLifecycleOwner, onStateLayoutObserver)
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [SupportFragment.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        when (supportViewAdapter.isEmpty()) {
            true -> onFetchDataInitialize()
            else -> onUpdateUserInterface()
        }
    }

    override fun onPause() {
        supportStateLayout?.onViewRecycled()
        super.onPause()
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

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model paged list holding data
     */
    fun onPostModelChange(model: List<M>?) {
        with (supportViewAdapter as SupportListAdapter) {
            submitList(model)
        }

        if (!model.isNullOrEmpty())
            supportStateLayout?.setNetworkState(NetworkState.Success)
        else if (supportViewAdapter.hasExtraRow()) {
            supportStateLayout?.setNetworkState(NetworkState.Success)
            supportViewAdapter.networkState = NetworkState.Loading
        }

        onUpdateUserInterface()
        resetWidgetStates()
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        super.onDestroy()
        supportViewAdapter.supportAction = null
        supportViewAdapter.retryFooterAction = null
    }
}