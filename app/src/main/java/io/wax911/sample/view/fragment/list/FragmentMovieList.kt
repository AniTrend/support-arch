package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.MovieAdapter
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.usecase.media.MediaRequestType
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.extension.argument
import io.wax911.support.ui.fragment.SupportFragmentList
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.recycler.holder.event.ItemClickListener
import io.wax911.support.ui.util.SupportStateLayoutConfiguration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMovieList: SupportFragmentList<Movie, CorePresenter, PagedList<Movie>>() {

    override val supportStateConfiguration = SupportStateLayoutConfiguration(
        R.drawable.ic_support_empty_state,
        R.drawable.ic_support_empty_state,
        R.string.supportTextLoading, R.string.action_retry
    )
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

    override val columnSize: Int = R.integer.single_list_size

    private val pagingMediaPayload by argument<IPagedMediaUseCase.Payload>(
        MediaRequestType.selectedMediaType
    )

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        supportViewModel.model.observe(this, Observer {
            onPostModelChange(it)
        })
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
     * Update views or bind a liveData to them
     */
    override fun onUpdateUserInterface() {

    }

    override fun onFetchDataInitialize() {
        pagingMediaPayload?.also {
            supportViewModel(
                parameter = it
            )
        }
    }

    companion object : InstanceCreator<FragmentMovieList, IPagedMediaUseCase.Payload>({
        FragmentMovieList().apply {
            arguments = Bundle().apply {
                putParcelable(MediaRequestType.selectedMediaType, it)
            }
        }
    })
}