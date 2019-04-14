package io.wax911.support.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nguyenhoanglam.progresslayout.ProgressLayout
import io.wax911.support.ui.R
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.core.recycler.adapter.SupportViewAdapter
import io.wax911.support.core.recycler.event.RecyclerLoadListener
import io.wax911.support.core.recycler.holder.event.ItemClickListener
import io.wax911.support.core.util.SupportStateKeyStore
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.isStateAtLeast
import io.wax911.support.extension.snackBar
import io.wax911.support.ui.extension.*
import io.wax911.support.ui.recycler.SupportRecyclerView
import io.wax911.support.ui.view.widget.SupportRefreshLayout
import kotlinx.android.synthetic.main.support_list.*

abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM> : SupportFragment<M, P, VM>(),
        RecyclerLoadListener, SupportRefreshLayout.OnRefreshAndLoadListener, ItemClickListener<M> {

    @IntegerRes
    protected var mColumnSize: Int = 0

    @LayoutRes
    protected var inflateLayout: Int = R.layout.support_list

    protected lateinit var supportViewAdapter: SupportViewAdapter<M>

    protected lateinit var progressLayout: ProgressLayout
    protected lateinit var supportRefreshLayout: SupportRefreshLayout
    protected lateinit var supportRecyclerView: SupportRecyclerView

    private val stateLayoutOnClick by lazy {
        android.view.View.OnClickListener {
            resetWidgetStates()
            progressLayout.showContentLoading()
            onRefresh()
        }
    }

    private val snackBarOnClickListener by lazy {
        android.view.View.OnClickListener {
            resetWidgetStates()
            supportRefreshLayout.isLoading = true
            makeRequest()
        }
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    private fun resetWidgetStates() {
        if (supportRefreshLayout.isRefreshing)
            supportRefreshLayout.isRefreshing = false
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
            inflater.inflate(inflateLayout, container, false).apply {
                progressLayout = findViewById(R.id.progressLayout)
                supportRefreshLayout = findViewById(R.id.supportRefreshLayout)
                supportRecyclerView = findViewById(R.id.supportRefreshLayout)
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
        supportRecyclerView.setHasFixedSize(true)
        supportRecyclerView.isNestedScrollingEnabled = true
        supportRecyclerView.layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(mColumnSize), StaggeredGridLayoutManager.VERTICAL)

        supportRefreshLayout.also {
            it.configureWidgetBehaviorWith(activity, presenter)
            it.setOnRefreshAndLoadListener(this)
        }
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [SupportFragment.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        progressLayout.showContentLoading()
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
        outState.putInt(SupportStateKeyStore.key_columns, mColumnSize)
        outState.putBoolean(SupportStateKeyStore.key_pagination, presenter.isPager)
        outState.putBoolean(SupportStateKeyStore.key_pagination_limit, presenter.isPagingLimit)

        outState.putInt(SupportStateKeyStore.arg_page, presenter.currentPage)
        outState.putInt(SupportStateKeyStore.arg_page_offset, presenter.currentOffset)
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
        savedInstanceState?.also {
            mColumnSize = it.getInt(SupportStateKeyStore.key_columns)
            presenter.isPager = it.getBoolean(SupportStateKeyStore.key_pagination)
            presenter.isPagingLimit = it.getBoolean(SupportStateKeyStore.key_pagination_limit)

            presenter.currentPage = it.getInt(SupportStateKeyStore.arg_page)
            presenter.currentOffset = it.getInt(SupportStateKeyStore.arg_page_offset)
        }
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to [SupportFragment.onPause] of the containing
     * Activity's lifecycle.
     */
    override fun onPause() {
        detachScrollListener()
        super.onPause()
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to [SupportFragment.onResume] of the containing
     * Activity's lifecycle.
     */
    override fun onResume() {
        super.onResume()
        attachScrollListener()
    }

    protected fun changeLayoutState(message: String?) {
        if (isStateAtLeast(Lifecycle.State.RESUMED)) {
            supportRefreshLayout.onResponseResetStates()
            if (presenter.isFirstPage()) {
                progressLayout.showLoadedContent()
                snackBar = progressLayout.snackBar(message!!, Snackbar.LENGTH_INDEFINITE)
                        .setAction(retryButtonText(), snackBarOnClickListener)
                snackBar?.show()
            } else {
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
        when (supportRefreshLayout.isRefreshing) {
            false -> {
                if (!progressLayout.isLoading)
                    supportRefreshLayout.isRefreshing = true
            }
        }
        presenter.isPagingLimit = false
        presenter.onRefreshPage()
        makeRequest()
    }

    override fun onLoad() {
        // no default implementation at this time
    }

    protected fun attachScrollListener() {
        when (presenter.isPager) {
            true -> when (!supportRecyclerView.hasOnScrollListener()) {
                true -> {
                    val layoutManager =
                            supportRecyclerView.layoutManager as StaggeredGridLayoutManager
                    presenter.initListener(layoutManager, this)
                    supportRecyclerView.addOnScrollListener(presenter)
                }
                else ->
                    Log.d(getViewName(), "attachScrollListener() already has " +
                            "OnScrollListener set, skipping this step")
            }
            else ->
                Log.d(getViewName(), "Skipping attachScrollListener() " +
                        "presenter.isPager -> ${presenter.isPager}")
        }
    }

    protected fun detachScrollListener() {
        when {
            presenter.isPager -> supportRecyclerView.clearOnScrollListeners()
            else ->
                Log.d(getViewName(), "Skipping detachScrollListener() " +
                        "presenter.isPager -> ${presenter.isPager}")
        }
    }

    /**
     * Disables pagination if we're not on the first page & the last
     * network/cache request was not successful
     */
    protected fun setPaginationLimitReached() {
        if (presenter.currentPage != 0) {
            supportRefreshLayout.isLoading = false
            presenter.isPagingLimit = true
        }
    }

    /**
     * Sets up the [supportRecyclerView] with [SupportViewAdapter] and additional properties if needed,
     * after it will change the state layout to empty or content.
     *
     * @param emptyText text that should be used when no data is available
     */
    protected fun injectAdapter(@StringRes emptyText: Int) = when {
        supportViewAdapter.hasData() -> {
            supportViewAdapter.presenter = presenter
            supportViewAdapter.itemClickListener = this
            when {
                supportRecyclerView.adapter == null -> {
                    supportViewAdapter.supportAction = supportAction
                    supportRecyclerView.adapter = supportViewAdapter
                }
                else -> {
                    supportRefreshLayout.onResponseResetStates()
                        supportViewAdapter.applyFilterIfRequired()
                }
            }
            progressLayout.showLoadedContent()
        }
        else -> changeLayoutState(context?.getString(emptyText))
    }


    /**
     * Handles post view model result after extraction or processing
     *
     * @param model main data model for the class
     * @param emptyText text that should be used when no data is available
     */
    protected fun onPostModelChange(model : List<M>?, @StringRes emptyText: Int) {
        when {
            !model.isNullOrEmpty() -> {
                when (presenter.isPager) {
                    true -> {
                        when (supportRefreshLayout.isRefreshing) {
                            true -> supportViewAdapter.onItemsInserted(model)
                            false -> supportViewAdapter.onItemRangeInserted(model)
                        }
                    }
                    false -> supportViewAdapter.onItemsInserted(model)
                }
                supportRefreshLayout.onResponseResetStates()
                updateUI()
            }
            else -> {
                if (presenter.isPager)
                    setPaginationLimitReached()
                if (!supportViewAdapter.hasData())
                    context.showContentError(progressLayout, emptyText, retryButtonText(), stateLayoutOnClick)
                supportRefreshLayout.onResponseResetStates()
            }
        }
    }

    /**
     * Returns a boolean that either allows or dis-allows this current fragment
     * from refreshing when preferences have been changed.
     *
     * @param key preference key that has been changed
     * */
    protected fun isPreferenceKeyValid(key: String) = true

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (isPreferenceKeyValid(key)) {
            supportRefreshLayout.isRefreshing = true
            onRefresh()
        }
    }

    /**
     * Must provide a string resource from the running application for a retry action button
     */
    @StringRes protected abstract fun retryButtonText() : Int

    /**
     * Called when the view model live data has been assigned a value.
     *
     * @param model The new data
     */
    abstract override fun onChanged(model: VM?)
}