package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.SupportFragmentList
import co.anitrend.arch.ui.recycler.adapter.SupportViewAdapter
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.MovieAdapter
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.domain.usecases.movie.TraktMovieUseCase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMovieList: SupportFragmentList<MovieEntity, CorePresenter, PagedList<MovieEntity>>() {

    private val pagingMediaPayload
            by argument<TraktMovieUseCase.Payload>(PARAM_MOVIE_TYPE)

    override val supportStateConfiguration = SupportStateLayoutConfiguration(
        R.drawable.ic_support_empty_state,
        R.drawable.ic_support_empty_state,
        R.string.supportTextLoading, R.string.action_retry
    )

    override val supportPresenter by inject<CorePresenter>()
    override val supportViewModel by viewModel<MovieViewModel>()

    override val supportViewAdapter: SupportViewAdapter<MovieEntity> =
        MovieAdapter(
            supportPresenter,
            object : ItemClickListener<MovieEntity> {
                /**
                 * When the target view from [View.OnClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been clicked
                 * @param data the liveData that at the click index
                 */
                /**
                 * When the target view from [View.OnClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been clicked
                 * @param data the liveData that at the click index
                 */
                override fun onItemClick(target: View, data: Pair<Int, MovieEntity?>) {

                }

                /**
                 * When the target view from [View.OnLongClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been long clicked
                 * @param data the liveData that at the long click index
                 */
                /**
                 * When the target view from [View.OnLongClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been long clicked
                 * @param data the liveData that at the long click index
                 */
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
                payload = it
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