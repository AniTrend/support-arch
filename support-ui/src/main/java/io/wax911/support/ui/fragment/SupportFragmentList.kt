package io.wax911.support.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.IUiModel
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.extension.isStateAtLeast
import io.wax911.support.extension.snackBar
import io.wax911.support.extension.util.SupportExtKeyStore
import io.wax911.support.ui.R
import io.wax911.support.ui.extension.configureWidgetBehaviorWith
import io.wax911.support.ui.extension.onResponseResetStates
import io.wax911.support.ui.fragment.contract.ISupportFragmentList
import io.wax911.support.ui.recycler.SupportRecyclerView
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.recycler.holder.event.ItemClickListener
import io.wax911.support.ui.view.widget.SupportStateLayout
import kotlinx.android.synthetic.main.support_list.*
import timber.log.Timber

abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM> : SupportFragment<M, P, VM>(),
    ISupportFragmentList<M>, SwipeRefreshLayout.OnRefreshListener, ItemClickListener<M> {

    protected abstract val supportViewAdapter: SupportViewAdapter<M>

    protected var supportStateLayout: SupportStateLayout? = null
    protected var supportRefreshLayout: SwipeRefreshLayout? = null
    protected var supportRecyclerView: SupportRecyclerView? = null

    private val stateLayoutOnClick = View.OnClickListener {
        if (supportStateLayout?.isLoading != true) {
            supportStateLayout?.showLoading(loadingMessage = loadingMessage)
            onRefresh()
            resetWidgetStates()
        } else
            Timber.tag(moduleTag).w("stateLayoutOnClick -> supportStateLayout is currently loading")
    }


    private val snackBarOnClickListener = View.OnClickListener {
        if (supportStateLayout?.isLoading != true) {
            supportViewAdapter.networkState = NetworkState.LOADING
            makeRequest()
            resetWidgetStates()
        } else
            Timber.tag(moduleTag).w("snackBarOnClickListener -> supportStateLayout is currently loading")
    }


    private val onRefreshObserver = Observer<NetworkState> { networkState ->
        supportRefreshLayout?.isRefreshing = networkState.status == SupportStateType.LOADING
        if (networkState.status != SupportStateType.LOADING)
            changeLayoutState(networkState)
    }

    private val onNetworkObserver = Observer<NetworkState> {
        when (!supportViewAdapter.isEmpty()) {
            true -> supportViewAdapter.networkState = it
            false -> changeLayoutState(it)
        }
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    private fun resetWidgetStates() {
        supportRefreshLayout?.onResponseResetStates()
        snackBar?.apply {
            if (isShown)
                dismiss()
        }
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see [.onActivityCreated].
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
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
     * is the default implementation).  This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     * If you return a View from here, you will later be called in
     * [.onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(inflateLayout, container, false)?.apply {
            supportStateLayout = findViewById(R.id.progressLayout)
            supportRefreshLayout = findViewById(R.id.supportRefreshLayout)
            supportRecyclerView = findViewById(R.id.supportRecyclerView)
        }

        supportRefreshLayout?.apply {
            configureWidgetBehaviorWith(activity)
            setOnRefreshListener(this@SupportFragmentList)
        }

        supportRecyclerView?.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(columnSize),
                StaggeredGridLayoutManager.VERTICAL
            )
        }

        supportStateLayout?.showLoading(loadingMessage = loadingMessage)

        setUpViewModelObserver()
        supportViewModel?.networkState?.observe(this, onNetworkObserver)
        supportViewModel?.refreshState?.observe(this, onRefreshObserver)

        return view
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [Activity.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        when (supportViewAdapter.isEmpty()) {
            true -> makeRequest()
            else -> updateUI()
        }
    }

    override fun onResume() {
        super.onResume()
        supportStateLayout?.onWidgetInteraction = stateLayoutOnClick
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
        savedInstanceState?.apply {
            supportPresenter.pagingHelper.fromBundle(getParcelable(SupportExtKeyStore.key_pagination))
        }
    }

    private fun changeLayoutState(networkState: NetworkState?) {
        if (isStateAtLeast(Lifecycle.State.RESUMED)) {
            supportRefreshLayout?.onResponseResetStates()
            if (supportPresenter.pagingHelper.isFirstPage()) {
                supportStateLayout?.showLoading(loadingMessage = loadingMessage)
                networkState?.apply {
                    if (message.isNullOrEmpty()) {
                        when (networkState.status) {
                            SupportStateType.ERROR ->
                                supportStateLayout?.showError(
                                    errorMessage = message
                                )
                            SupportStateType.CONTENT -> {
                                supportStateLayout?.showContent()
                                supportPresenter.pagingHelper.onPageNext()
                            }
                            SupportStateType.LOADING ->
                                supportStateLayout?.showLoading(
                                    loadingMessage = loadingMessage
                                )
                        }
                    } else {
                        snackBar = supportStateLayout?.snackBar(message!!, Snackbar.LENGTH_INDEFINITE)
                            ?.setAction(retryButtonText, snackBarOnClickListener)
                        snackBar?.show()
                    }
                }
            } else {
                supportStateLayout?.showError(
                    errorMessage = networkState?.message
                )
            }
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        supportPresenter.pagingHelper.onPageRefresh()
        supportViewModel?.refresh()
    }

    /**
     * Sets up the [io.wax911.support.ui.recycler.SupportRecyclerView] with
     * [io.wax911.support.ui.recycler.adapter.SupportViewAdapter]
     * and additional properties if needed, after it will change the state layout to empty or content.
     */
    override fun injectAdapter() {
        supportViewAdapter.also {
            when {
                supportRecyclerView?.adapter == null -> {
                    it.supportAction = supportAction
                    supportRecyclerView?.adapter = it
                }
                else -> it.applyFilterIfRequired()
            }
        }
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param pagedList paged list holding data
     */
    override fun onPostModelChange(pagedList: PagedList<M>?) {
        supportViewAdapter.submitList(pagedList)
        supportRefreshLayout?.onResponseResetStates()
        supportStateLayout?.showContent()
        injectAdapter()
        updateUI()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (isPreferenceKeyValid(key))
            onRefresh()
    }
}