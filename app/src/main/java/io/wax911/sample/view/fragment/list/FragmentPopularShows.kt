package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.View
import androidx.paging.PagedList
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.ShowAdapter
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.data.repository.show.ShowRequestType
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.core.viewmodel.SupportViewModel
import io.wax911.support.extension.LAZY_MODE_UNSAFE
import io.wax911.support.extension.util.SupportExtKeyStore
import io.wax911.support.ui.fragment.SupportFragmentList
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.util.SupportUiKeyStore
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPopularShows : SupportFragmentList<Show, CorePresenter, PagedList<Show>>() {

    override val supportViewAdapter: SupportViewAdapter<Show> by lazy(LAZY_MODE_UNSAFE) {
        ShowAdapter(supportPresenter, this@FragmentPopularShows)
    }

    override val supportViewModel: ShowViewModel? by viewModel()
    override val supportPresenter: CorePresenter by inject()

    override val retryButtonText: Int = R.string.action_retry
    override val columnSize: Int = R.integer.single_list_size

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        supportViewModel?.model?.observe(this, this)
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragmentList]
     * then this method will be called in [SupportFragmentList.onCreate].
     * invokes this function
     *
     * @see [SupportFragmentList.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {

    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [CompatView] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun updateUI() {

    }

    /**
     * Handles the complex logic required to dispatch network request to [SupportViewModel]
     * which uses [SupportRepository] to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] inside of your [SupportRepository]
     *
     * @see [SupportRepository.publishResult]
     */
    override fun makeRequest() {
        supportViewModel?.queryFor(Bundle().apply {
            putParcelable(SupportExtKeyStore.key_pagination, supportPresenter.pagingHelper)
            putString(SupportExtKeyStore.arg_request_type, ShowRequestType.SHOW_TYPE_POPULAR)
        })
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: PagedList<Show>?) {
        onPostModelChange(t)
    }

    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data the liveData that at the click index
     */
    override fun onItemClick(target: View, data: Pair<Int, Show?>) {

    }

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data the liveData that at the long click index
     */
    override fun onItemLongClick(target: View, data: Pair<Int, Show?>) {

    }

    companion object : InstanceCreator<FragmentPopularShows, Bundle?>({
        FragmentPopularShows().apply {
            arguments = it
        }
    })
}