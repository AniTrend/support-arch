package io.wax911.sample.movie.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.SupportFragmentPagedList
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import io.wax911.sample.R
import io.wax911.sample.movie.ui.adapter.recycler.MovieAdapter
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.domain.usecases.movie.TraktMovieUseCase
import io.wax911.sample.movie.viewmodel.MovieViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMovieList: SupportFragmentPagedList<MovieEntity, CorePresenter, PagedList<MovieEntity>>() {

    private val pagingMediaPayload
            by argument<TraktMovieUseCase.Payload>(PARAM_MOVIE_TYPE)

    override val supportStateConfiguration = SupportStateLayoutConfiguration(
        R.drawable.ic_support_empty_state,
        R.drawable.ic_support_empty_state,
        R.string.supportTextLoading, R.string.action_retry
    )

    override val supportPresenter by inject<CorePresenter>()
    override val supportViewModel by viewModel<MovieViewModel>()

    override val supportViewAdapter =
        MovieAdapter(supportPresenter, object : ItemClickListener<MovieEntity> {

            override fun onItemClick(target: View, data: Pair<Int, MovieEntity?>) {

            }

            override fun onItemLongClick(target: View, data: Pair<Int, MovieEntity?>) {

            }
        })

    override val columnSize: Int = R.integer.single_list_size

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        supportViewModel.model.observe(this, Observer {
            onPostModelChange(it)
        })
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragmentPagedList]
     * then this method will be called in [SupportFragmentPagedList.onCreate].
     * invokes this function
     *
     * @see [SupportFragmentPagedList.onCreate]
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

    companion object {

        const val PARAM_MOVIE_TYPE = "FragmentMovieList:PARAM_MOVIE_TYPE"

        fun newInstance(bundle: Bundle?): FragmentMovieList {
            val fragment = FragmentMovieList()
            fragment.arguments = bundle
            return fragment
        }
    }
}