package io.wax911.support.custom.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.annimon.stream.IntPair
import com.google.android.material.snackbar.Snackbar
import io.wax911.support.R
import io.wax911.support.base.event.RecyclerLoadListener
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.recycler.SupportViewAdapter
import io.wax911.support.custom.widget.SupportRefreshLayout
import io.wax911.support.util.SupportNotifyUtil
import io.wax911.support.util.SupportStateUtil
import kotlinx.android.synthetic.main.support_list.*
import retrofit2.Call

abstract class SupportFragmentList<M, P : SupportPresenter<*>, VM> : SupportFragment<M, P, VM>(),
        RecyclerLoadListener, SupportRefreshLayout.OnRefreshAndLoadListener {

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

    private val snackbarOnClickListener = android.view.View.OnClickListener {
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
        when (!supportViewAdapter.isEmpty()) {
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
        outState.putBoolean(SupportStateUtil.key_pagination, isPager)
        outState.putInt(SupportStateUtil.key_columns, mColumnSize)
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
            isPager = savedInstanceState.getBoolean(SupportStateUtil.key_pagination)
            mColumnSize = savedInstanceState.getInt(SupportStateUtil.key_columns)
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

    override fun onResponseError(call: Call<VM>, throwable: Throwable) {
        supportRefreshLayout.onResponseHandle()
        if (presenter.isNotFirstPage() && isPager) {
            if (progressLayout.isLoading)
                progressLayout.showContent()
            snackbar = SupportNotifyUtil.make(progressLayout,
                    R.string.text_unable_to_load_next_page, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.text_try_again, snackbarOnClickListener)
            snackbar!!.show()
        } else {
            showLoading()

        }
    }

    override fun onResponseSuccess(call: Call<VM>, message: String) {
        supportRefreshLayout.onResponseHandle()
        if (presenter.isNotFirstPage() && isPager) {
            if (progressLayout.isLoading)
                progressLayout.showContent()
            snackbar = SupportNotifyUtil.make(progressLayout,
                    R.string.text_unable_to_load_next_page, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.text_try_again, snackbarOnClickListener)
            snackbar!!.show()
        } else {
            showLoading()

        }
    }

    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data   the mutableLiveData that at the click index
     */
    override fun onItemClick(target: View, data: IntPair<M>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data   the mutableLiveData that at the long click index
     */
    override fun onItemLongClick(target: View, data: IntPair<M>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Update views or bind a mutableLiveData to them
     */
    override fun updateUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: VM) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoad() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    protected fun attachScrollListener() {
        when {
            isPager -> if (!supportRecyclerView.hasOnScrollListener()) {
                presenter.initListener(staggeredGridLayoutManager, this)
                supportRecyclerView.addOnScrollListener(presenter)
            }
        }
    }

    protected fun detachScrollListener() = when {
        isPager -> supportRecyclerView.clearOnScrollListeners()
        else -> { }
    }

    fun showLoading() =
            progressLayout.showLoading()

    fun showConent() =
            progressLayout.showContent()

}