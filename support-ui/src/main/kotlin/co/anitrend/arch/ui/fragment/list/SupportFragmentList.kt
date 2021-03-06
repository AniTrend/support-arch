package co.anitrend.arch.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.domain.extensions.isLoading
import co.anitrend.arch.extension.ext.attachComponent
import co.anitrend.arch.extension.ext.detachComponent
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.extension.configureWidgetBehaviorWith
import co.anitrend.arch.ui.extension.onResponseResetStates
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
) : SupportFragment(), ISupportFragmentList<M> {

    protected open var supportStateLayout: SupportStateLayout? = null
    protected open var supportRefreshLayout: SwipeRefreshLayout? = null
    protected open var supportRecyclerView: SupportRecyclerView? = null

    /**
     * Default span size of [IntegerRes] the layout manager will use.
     *
     * @see setRecyclerLayoutManager
     */
    @get:IntegerRes
    protected abstract val defaultSpanSize: Int

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    abstract fun onFetchDataInitialize()

    override val onRefreshObserver = Observer<NetworkState> {
        supportRefreshLayout?.isRefreshing = it.isLoading()
    }

    override val onNetworkObserver = Observer<NetworkState> {
        changeLayoutState(it)
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    protected open fun resetWidgetStates() =
        supportRefreshLayout?.onResponseResetStates()

    /**
     * Provides a layout manager that should be used by [setRecyclerLayoutManager]
     */
    override fun provideLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(
            resources.getInteger(defaultSpanSize),
            StaggeredGridLayoutManager.VERTICAL
        )
    }

    /**
     * Sets a layout manager to the recycler view
     */
    override fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView) {
        if (recyclerView.layoutManager == null)
            recyclerView.layoutManager = provideLayoutManager()
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = supportViewAdapter.let { adapter ->
                adapter as RecyclerView.Adapter<*>
                adapter.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
                adapter
            }
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        attachComponent(supportViewAdapter)
        lifecycleScope.launchWhenResumed {
            supportViewAdapter.clickableStateFlow
                .debounce(16)
                .filterIsInstance<ClickableItem.State>()
                .collect {
                    if (it.state !is NetworkState.Loading)
                        viewModelState()?.retry()
                    else
                        Timber.tag(moduleTag).d(
                            "retryFooterAction -> state is loading? current state: ${it.state}"
                        )
                }
        }
        lifecycleScope.launchWhenResumed {
            supportStateLayout?.interactionStateFlow
                ?.filterNotNull()
                ?.debounce(16)
                ?.collect {
                    if (it.state !is NetworkState.Loading)
                        viewModelState()?.retry()
                    else
                        Timber.tag(moduleTag).d(
                            "onStateLayoutObserver -> state is loading? current state: ${it.state}"
                        )
                }
        }
        lifecycleScope.launchWhenResumed {
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
        supportStateLayout?.stateConfigFlow?.value = stateConfig

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
        supportRecyclerView?.also { attachComponent(it) }
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
            supportStateLayout?.networkMutableStateFlow?.value = NetworkState.Idle
            supportViewAdapter.networkState = networkState
        }
        else {
            supportStateLayout?.networkMutableStateFlow?.value = networkState
        }

        resetWidgetStates()
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        lifecycleScope.launch {
            viewModelState()?.refresh()
        }
    }

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        detachComponent(supportViewAdapter)
        supportRefreshLayout?.setOnRefreshListener(null)
        super.onDestroy()
    }

    /**
     * Called when the view previously created by [onCreateView] has
     * been detached from the fragment. The next time the fragment needs
     * to be displayed, a new view will be created.
     *
     * This is called after [onStop] and before [onDestroy]. It is called *regardless* of
     * whether [onCreateView] returned a non-null view. Internally it is called after the
     * view's state has been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        supportRecyclerView?.also {
            detachComponent(it)
        }
        super.onDestroyView()
        supportStateLayout = null
        supportRefreshLayout = null
        supportRecyclerView = null
    }

    protected open fun afterPostModelChange(data: Collection<*>?) {
        /*if (data.isNullOrEmpty())
            supportViewAdapter.networkState = NetworkState.Loading
        else
            supportStateLayout?.networkMutableStateFlow?.value = NetworkState.Idle*/

        resetWidgetStates()
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model list holding data
     */
    protected open fun onPostModelChange(model: Collection<M>?) {
        /** Since pagedList is a type of list we check it first */
        when (model) {
            is PagedList -> {
                with (supportViewAdapter as SupportPagedListAdapter) {
                    submitList(model)
                }
            }
            is List -> {
                with (supportViewAdapter as SupportListAdapter) {
                    submitList(model)
                }
            }
            else -> {}
        }

        afterPostModelChange(model)
    }
}