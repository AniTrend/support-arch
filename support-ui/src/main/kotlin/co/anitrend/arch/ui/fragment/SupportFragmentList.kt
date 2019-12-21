package co.anitrend.arch.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.extension.configureWidgetBehaviorWith
import co.anitrend.arch.ui.extension.onResponseResetStates
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
abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM>  :
    SupportFragment<M, P, VM>(), ISupportFragmentList<M> {

    override val inflateLayout: Int = R.layout.support_list

    override var supportStateLayout: SupportStateLayout? = null
    override var supportRefreshLayout: SwipeRefreshLayout? = null
    override var supportRecyclerView: SupportRecyclerView? = null

    abstract override val supportViewAdapter: ISupportViewAdapter<M>

    override val stateLayoutOnClick = View.OnClickListener {
        if (supportStateLayout?.isLoading != true) {
            supportViewModel?.retry()
        } else
            Timber.tag(moduleTag).i("stateLayoutOnClick -> supportStateLayout is currently loading")
    }

    override val adapterFooterRetryAction = View.OnClickListener {
        if (supportStateLayout?.isLoading != true)
            supportViewModel?.retry()
        else
            Timber.tag(moduleTag).i("adapterFooterRetryAction -> supportStateLayout is currently loading")
    }

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
     * Called to do initial creation of a fragment. This is called after
     * [onAttach] and before [onCreateView].
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point. If you want to do work once the activity itself is
     * created, see [onActivityCreated].
     *
     * Any restored child fragments will be created before the base [onCreate] method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents(savedInstanceState)
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation). This will be called between
     * [onCreate] and [onActivityCreated].
     *
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

        supportStateLayout?.apply {
            onWidgetInteraction = stateLayoutOnClick
            stateConfiguration = supportStateConfiguration
            // setNetworkState(NetworkState.Loading)
        }
        supportViewAdapter.retryFooterAction = adapterFooterRetryAction

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
        supportViewModel?.networkState?.observe(this, onNetworkObserver)
        supportViewModel?.refreshState?.observe(this, onRefreshObserver)
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
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to [.onCreate],
     * [.onCreateView], and
     * [.onActivityCreated].
     *
     *
     * This corresponds to [ Activity.onSaveInstanceState(Bundle)][SupportFragment.onSaveInstanceState] and most of the discussion there
     * applies here as well.  Note however: *this method may be called
     * at any time before [.onDestroy]*.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SupportExtKeyStore.key_pagination, supportPresenter.pagingHelper)
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after [.onActivityCreated] and before
     * [.onStart].
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        supportPresenter.pagingHelper.from(savedInstanceState)
    }

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [NetworkState]
     *
     * @param networkState New state from the application
     */
    override fun changeLayoutState(networkState: NetworkState?) {
        if (supportViewAdapter.hasExtraRow()) {
            supportStateLayout?.setNetworkState(NetworkState.Success)
            supportViewAdapter.networkState = networkState
        }
        else {
            supportStateLayout?.setNetworkState(
                networkState ?: NetworkState.Error(
                    heading = "Unknown State",
                    message = "The application is in an unknown state ¯\\_(ツ)_/¯"
                )
            )
        }
        if (networkState is NetworkState.Success)
            supportPresenter.pagingHelper.onPageNext()

        resetWidgetStates()
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        supportPresenter.pagingHelper.onPageRefresh()
        supportViewModel?.refresh()
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
        else {
            if (supportViewAdapter.hasExtraRow()) {
                supportStateLayout?.setNetworkState(NetworkState.Success)
                supportViewAdapter.networkState = NetworkState.Loading
            }
            /*else
                supportStateLayout?.setNetworkState(NetworkState.Loading)*/
        }

        onUpdateUserInterface()
        resetWidgetStates()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (isPreferenceKeyValid(key))
            onRefresh()
    }
}