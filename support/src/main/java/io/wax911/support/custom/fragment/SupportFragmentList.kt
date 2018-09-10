package io.wax911.support.custom.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.annimon.stream.IntPair
import com.google.android.material.snackbar.Snackbar
import io.wax911.support.R
import io.wax911.support.base.event.RecyclerLoadListener
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.recycler.SupportViewAdapter
import io.wax911.support.custom.widget.SupportRefreshLayout
import io.wax911.support.getCompatDrawable
import io.wax911.support.isEmptyOrNull
import io.wax911.support.util.SupportNotifyUtil
import io.wax911.support.util.SupportStateUtil
import kotlinx.android.synthetic.main.support_list.*
import retrofit2.Call

abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM> : SupportFragment<M, P, VM>(),
        RecyclerLoadListener, SupportRefreshLayout.OnRefreshAndLoadListener {

    @IntegerRes
    protected var mColumnSize: Int = 0

    protected var searchQuery : String? = null
    protected var isLimitReached : Boolean = false

    protected lateinit var supportViewAdapter: SupportViewAdapter<M>

    protected val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(resources.getInteger(mColumnSize), StaggeredGridLayoutManager.VERTICAL)
    }

    private val stateLayoutOnClick = android.view.View.OnClickListener {
        resetWidgetStates()
        showLoading()
        onRefresh()
    }

    private val snackBarOnClickListener = android.view.View.OnClickListener {
        resetWidgetStates()
        supportRefreshLayout.isLoading = true
        makeRequest()
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    private fun resetWidgetStates() {
        if (supportRefreshLayout.isRefreshing)
            supportRefreshLayout.isRefreshing = false
        if (snackbar != null && snackbar!!.isShown)
            snackbar!!.dismiss()
    }

    /**
     * Called to do extra initialization on behalf of the onCreate method using saved instance
     * @see onCreate
     */
    protected abstract fun initializeListComponents(savedInstanceState: Bundle?)

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
        initializeListComponents(savedInstanceState)
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
            inflater.inflate(R.layout.support_list, container, false)

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
        supportRecyclerView.setHasFixedSize(true)
        supportRecyclerView.isNestedScrollingEnabled = true
        supportRecyclerView.layoutManager = staggeredGridLayoutManager

        supportRefreshLayout.setOnRefreshAndLoadListener(this)
        supportRefreshLayout.configureWidgetBehaviorWith(context)
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [Activity.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        showLoading()
        when (!supportViewAdapter.hasData()) {
            true -> onRefresh()
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
     * This corresponds to [ Activity.onSaveInstanceState(Bundle)][Activity.onSaveInstanceState] and most of the discussion there
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
        outState.putInt(SupportStateUtil.key_columns, mColumnSize)
        outState.putBoolean(SupportStateUtil.key_pagination, presenter.isPager)
        outState.putBoolean(SupportStateUtil.key_pagination_limit, presenter.isPagingLimit)

        outState.putInt(SupportStateUtil.arg_page, presenter.currentPage)
        outState.putInt(SupportStateUtil.arg_page_offset, presenter.currentOffset)
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
        if (savedInstanceState != null) {
            mColumnSize = savedInstanceState.getInt(SupportStateUtil.key_columns)
            presenter.isPager = savedInstanceState.getBoolean(SupportStateUtil.key_pagination)
            presenter.isPagingLimit = savedInstanceState.getBoolean(SupportStateUtil.key_pagination_limit)

            presenter.currentPage = savedInstanceState.getInt(SupportStateUtil.arg_page)
            presenter.currentOffset = savedInstanceState.getInt(SupportStateUtil.arg_page_offset)
        }
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to [Activity.onPause] of the containing
     * Activity's lifecycle.
     */
    override fun onPause() {
        detachScrollListener()
        super.onPause()
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to [Activity.onResume] of the containing
     * Activity's lifecycle.
     */
    override fun onResume() {
        super.onResume()
        attachScrollListener()
    }

    override fun onResponseError(call: Call<VM>, throwable: Throwable, @StringRes message: Int) {
        changeLayoutState(context?.getString(message))
    }


    override fun onResponseSuccess(call: Call<VM>, @StringRes message: Int) {
        changeLayoutState(context?.getString(message))
    }

    protected fun changeLayoutState(message: String?) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            supportRefreshLayout.onResponseResetStates()
            if (presenter.isFirstPage()) {
                progressLayout.showContent()
                snackbar = SupportNotifyUtil.make(progressLayout, message!!, Snackbar.LENGTH_INDEFINITE)
                        .setAction(retryButtonText(), snackBarOnClickListener)
                snackbar!!.show()
            } else {
                showLoading()
                progressLayout.showError(context?.getCompatDrawable(R.drawable.ic_support_empty_state),
                        message, context?.getString(retryButtonText()), stateLayoutOnClick)
            }
        }
    }

    override fun onLoadMore() {
        supportRefreshLayout.isLoading = true
        makeRequest()
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        presenter.isPagingLimit = false
        presenter.onRefreshPage()
        makeRequest()
    }

    override fun onLoad() {
        // no default implementation at this time
    }

    protected fun attachScrollListener() = when (presenter.isPager) {
        true -> when (!supportRecyclerView.hasOnScrollListener()) {
            true -> {
                presenter.initListener(staggeredGridLayoutManager, this)
                supportRecyclerView.addOnScrollListener(presenter)
            }
            else -> { }
        }
        else -> { }
    }

    protected fun detachScrollListener() = when {
        presenter.isPager -> supportRecyclerView.clearOnScrollListeners()
        else -> { }
    }

    fun showLoading() = when {
        progressLayout.isContent -> progressLayout.showLoading()
        progressLayout.isEmpty -> progressLayout.showLoading()
        progressLayout.isError -> progressLayout.showLoading()
        else -> { }
    }

    fun showConent() = when {
        progressLayout.isLoading -> progressLayout.showContent()
        progressLayout.isEmpty -> progressLayout.showContent()
        progressLayout.isError -> progressLayout.showContent()
        else -> { }
    }

    /**
     * While paginating if our request was a success and
     */
    fun setLimitReached() {
        if (presenter.currentPage != 0) {
            supportRefreshLayout.isLoading = false
            presenter.isPagingLimit = true
        }
    }

    /**
     * Set your adapter and call this method when you are done to the current
     * parents data, then call this method after
     *
     * @param emptyText text that should be used when no data is available
     */
    protected fun injectAdapter(@StringRes emptyText: Int) = when {
        supportViewAdapter.hasData() -> {
            supportViewAdapter.presenter = presenter
            supportViewAdapter.clickListener = this
            when {
                supportRecyclerView.adapter == null -> {
                    if (supportAction != null)
                        supportViewAdapter.supportAction = supportAction
                    supportRecyclerView.adapter = supportViewAdapter
                }
                else -> {
                    supportRefreshLayout.onResponseResetStates()
                    if (searchQuery.isNullOrEmpty())
                        supportViewAdapter.filter.filter(searchQuery)
                }
            }
            showConent()
        }
        else -> changeLayoutState(context?.getString(emptyText))
    }


    /**
     * Handles post view model result after extraction or processing
     *
     * @param model main data model for the class
     * @param emptyText text that should be used when no data is available
     */
    protected fun onPostModelChange(model : List<M>, @StringRes emptyText: Int) {
        when {
            !model.isEmptyOrNull() -> {
                when (presenter.isPager && supportRefreshLayout.isRefreshing) {
                    true -> {
                        when {
                            supportViewAdapter.hasData() -> supportViewAdapter.onItemsInserted(model)
                            else -> supportViewAdapter.onItemRangeInserted(model)
                        }
                    }
                    false -> supportViewAdapter.onItemsInserted(model)
                }
                supportRefreshLayout.setPermitRefresh(false)
                updateUI()
            }
            else -> {
                if (presenter.isPager)
                    setLimitReached()
                if (supportViewAdapter.hasData())
                    changeLayoutState(context?.getString(emptyText))
            }
        }
    }

    /**
     * Must provide a string resource from the running application for a retry action button
     */
    @StringRes protected abstract fun retryButtonText() : Int

    /**
     * Called when the data is changed.
     * @param model The new data
     */
    abstract override fun onChanged(model: VM)


    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data   the mutableLiveData that at the click index
     */
    abstract override fun onItemClick(target: View, data: IntPair<M>)

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data   the mutableLiveData that at the long click index
     */
    abstract override fun onItemLongClick(target: View, data: IntPair<M>)
}