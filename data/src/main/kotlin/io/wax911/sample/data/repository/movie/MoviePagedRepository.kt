package io.wax911.sample.data.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.domain.entities.NetworkState
import io.wax911.sample.data.datasource.remote.movie.contract.TraktMoviePagedSource
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.domain.repositories.movie.ITraktMovieRepository

class MoviePagedRepository(
    private val moviePagedDataSource: TraktMoviePagedSource
) : SupportRepository(moviePagedDataSource), ITraktMovieRepository<UserInterfaceState<PagedList<MovieEntity>>> {

    /**
     * @return popular movies
     */
    override fun getPopularMovies(): UserInterfaceState<PagedList<MovieEntity>> {
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UserInterfaceState(
            model = moviePagedDataSource.popularMovieObservable(null),
            networkState = moviePagedDataSource.networkState,
            refresh = {
                moviePagedDataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                moviePagedDataSource.retryRequest()
            }
        )
    }

    /**
     * @return trending movies
     */
    override fun getTrendingMovies(): UserInterfaceState<PagedList<MovieEntity>> {
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UserInterfaceState(
            model = moviePagedDataSource.trendingMovieObservable(null),
            networkState = moviePagedDataSource.networkState,
            refresh = {
                moviePagedDataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                moviePagedDataSource.retryRequest()
            }
        )
    }
}