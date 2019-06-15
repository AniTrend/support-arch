package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.MovieAdapter
import io.wax911.sample.adapter.recycler.ShowAdapter
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.sample.data.model.contract.TraktEntity
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.repository.show.ShowRequestType
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.extension.util.SupportExtKeyStore
import io.wax911.support.ui.fragment.SupportFragment
import io.wax911.support.ui.fragment.SupportFragmentList
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.recycler.holder.event.ItemClickListener
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPopularMovies: SupportFragmentList<Movie, CorePresenter, PagedList<Movie>>() {

    override val supportPresenter by inject<CorePresenter>()
    override val supportViewModel by viewModel<MovieViewModel>()


    override val supportViewAdapter: SupportViewAdapter<Movie> =
        MovieAdapter(supportPresenter, object : ItemClickListener<Movie> {
            /**
             * When the target view from [View.OnClickListener]
             * is clicked from a view holder this method will be called
             *
             * @param target view that has been clicked
             * @param data the liveData that at the click index
             */
            override fun onItemClick(target: View, data: Pair<Int, Movie?>) {

            }

            /**
             * When the target view from [View.OnLongClickListener]
             * is clicked from a view holder this method will be called
             *
             * @param target view that has been long clicked
             * @param data the liveData that at the long click index
             */
            override fun onItemLongClick(target: View, data: Pair<Int, Movie?>) {

            }
        })

    override val retryButtonText: Int = R.string.action_retry
    override val columnSize: Int = R.integer.single_list_size


    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {

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
        inflater.inflate(R.layout.fragment_history, container, false)

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
        container.showLoading(
            drawableRes = R.drawable.ic_support_empty_state,
            loadingMessage = R.string.Loading
        )
    }

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        supportViewModel.model.observe(this, this)
    }

    /**
     * Update views or bind a liveData to them
     */
    override fun updateUI() {

    }

    override fun makeRequest() {
        /*supportViewModel(Bundle().apply {
            putParcelable(SupportExtKeyStore.key_pagination, supportPresenter.pagingHelper)
            putString(SupportExtKeyStore.arg_request_type, MovieRequestType.SHOW_TYPE_POPULAR)
        })*/
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: PagedList<Movie>?) {
        onPostModelChange(t)
    }

    companion object : InstanceCreator<FragmentPopularMovies, Bundle?>({
        val fragment = FragmentPopularMovies()
        fragment.arguments = it
        fragment
    })
}