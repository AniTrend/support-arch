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
import io.wax911.support.core.util.SupportKeyStore
import io.wax911.support.core.view.model.NetworkState
import io.wax911.support.core.view.model.contract.IUiModel
import io.wax911.support.core.view.model.contract.SupportStateType
import io.wax911.support.extension.isStateAtLeast
import io.wax911.support.extension.snackBar
import io.wax911.support.ui.R
import io.wax911.support.ui.extension.configureWidgetBehaviorWith
import io.wax911.support.ui.extension.onResponseResetStates
import io.wax911.support.ui.fragment.contract.ISupportFragmentList
import io.wax911.support.ui.recycler.SupportRecyclerView
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.recycler.holder.event.ItemClickListener
import io.wax911.support.ui.view.widget.SupportStateLayout
import kotlinx.android.synthetic.main.support_list.*

abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM> : SupportFragment<M, P, VM>(),
    ISupportFragmentList<M>, SwipeRefreshLayout.OnRefreshListener, ItemClickListener<M> {

    protected abstract val supportViewAdapter: SupportViewAdapter<M>

    protected var supportStateLayout: SupportStateLayout? = null
    protected var supportRefreshLayout: SwipeRefreshLayout? = null
    protected var supportRecyclerView: SupportRecyclerView? = null

    private val stateLayoutOnClick = View.OnClickListener {
        supportStateLayout?.showLoading(loadingMessage = loadingMessage)
        onRefresh()
        resetWidgetStates()
    }


    private val snackBarOnClickListener = View.OnClickListener {
        supportViewAdapter.networkState = NetworkState.LOADING
        makeRequest()
        resetWidgetStates()
    }


    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    private fun resetWidgetStates() {
        supportRefreshLayout?.onResponseResetStates()
        snackBar?.also {
            if (it.isShown)
                it.dismiss()
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(inflateLayout, container, false)?.apply {
                supportStateLayout = findViewById(R.id.progressLayout)
                supportRefreshLayout = findViewById(R.id.supportRefreshLayout)
                supportRecyclerView = findViewById(R.id.supportRecyclerView)
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
        supportRecyclerView?.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(columnSize),
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = supportViewAdapter.also {
                it.supportAction = supportAction
            }
        }

        setUpViewModelObserver()

        supportViewModel?.networkState?.observe(this, Observer {
            when (!supportViewAdapter.isEmpty()) {
                true -> supportViewAdapter.networkState = it
                false -> when (it.status) {
                    SupportStateType.ERROR ->
                        supportStateLayout?.showError(
                            errorMessage = it.message,
                            onClickListener = stateLayoutOnClick
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
            }
        })

        supportViewModel?.refreshState?.observe(this, Observer { networkState ->
            supportRefreshLayout?.isRefreshing = networkState.status == SupportStateType.LOADING
        })

        supportRefreshLayout?.apply {
            configureWidgetBehaviorWith(activity)
            setOnRefreshListener(this@SupportFragmentList)
        }
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [SupportFragment.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        supportStateLayout?.showLoading(loadingMessage = loadingMessage)
        when (supportViewAdapter.isEmpty()) {
            true -> makeRequest()
            else -> updateUI()
        }
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
        outState.putParcelable(SupportKeyStore.key_pagination, supportPresenter.pagingHelper)
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
            supportPresenter.pagingHelper.fromBundle(getParcelable(SupportKeyStore.key_pagination))
        }
    }

    private fun changeLayoutState(message: String?) {
        if (isStateAtLeast(Lifecycle.State.RESUMED)) {
            supportRefreshLayout?.onResponseResetStates()
            if (supportPresenter.pagingHelper.isFirstPage()) {
                supportStateLayout?.showLoading(loadingMessage = loadingMessage)
                snackBar = supportStateLayout?.snackBar(message!!, Snackbar.LENGTH_INDEFINITE)
                        ?.setAction(retryButtonText, snackBarOnClickListener)
                snackBar?.show()
            } else {
                supportStateLayout?.showError(
                    errorMessage = message,
                    onClickListener = stateLayoutOnClick
                )
            }
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        when (supportRefreshLayout?.isRefreshing) {
            false -> {
                if (supportStateLayout?.isLoading != true)
                    supportRefreshLayout?.isRefreshing = true
            }
        }
        supportPresenter.pagingHelper.onPageRefresh()
        supportViewModel?.refresh()
    }

    /**
     * Disables pagination if we're not on the first page & the last
     * network/cache request was not successful
     */
    private fun setPaginationLimitReached() {
        if (!supportPresenter.pagingHelper.isFirstPage()) {
            supportPresenter.pagingHelper.isPagingLimit = true
            supportViewAdapter.networkState = NetworkState(
                status = SupportStateType.CONTENT
            )
        }
    }

    /**
     * Sets up the [supportRecyclerView] with [SupportViewAdapter] and additional properties if needed,
     * after it will change the state layout to empty or content.
     *
     * @param uiModel exposed user interface method
     */
    @Deprecated("Will be removed in the next version")
    override fun injectAdapter(uiModel: IUiModel) {
        supportViewAdapter.also {
            if (it.itemCount > 0) {
                when {
                    supportRecyclerView?.adapter == null -> {
                        it.supportAction = supportAction
                        supportRecyclerView?.adapter = it
                    }
                    else -> {
                        supportRefreshLayout?.onResponseResetStates()
                        it.applyFilterIfRequired()
                    }
                }
                supportStateLayout?.showContent()
            }
            else
                changeLayoutState(uiModel.networkState.value?.message)
        }
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param pagedList paged list holding data
     */
    override fun onPostModelChange(pagedList: PagedList<M>?) {
        when (pagedList?.isEmpty()) {
            false -> {
                supportViewAdapter.submitList(pagedList)
                supportRefreshLayout?.onResponseResetStates()
                supportStateLayout?.showContent()
                updateUI()
            }
            true -> {
                if (!supportPresenter.pagingHelper.isFirstPage())
                    setPaginationLimitReached()
                if (!supportViewAdapter.isEmpty())
                    supportStateLayout?.showError(
                        errorMessage =  supportViewModel?.networkState?.value?.message,
                        onClickListener = stateLayoutOnClick
                    )
                supportRefreshLayout?.onResponseResetStates()
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (isPreferenceKeyValid(key)) {
            supportRefreshLayout?.isRefreshing = true
            onRefresh()
        }
    }
}