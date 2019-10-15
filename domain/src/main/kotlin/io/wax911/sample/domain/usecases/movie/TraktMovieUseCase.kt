package io.wax911.sample.domain.usecases.movie

import android.os.Parcelable
import co.anitrend.arch.domain.common.IUserInterfaceState
import io.wax911.sample.domain.common.IPagedMediaUseCase
import io.wax911.sample.domain.repositories.movie.ITraktMovieRepository
import kotlinx.android.parcel.Parcelize

abstract class TraktMovieUseCase<R: IUserInterfaceState<*>>(
    protected val repository: ITraktMovieRepository<R>
) : IPagedMediaUseCase<TraktMovieUseCase.Payload, R> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: Payload): R {
        return when (param.requestType) {
            MovieRequestType.MoviePopular -> repository.getPopularMovies()
            MovieRequestType.MovieTrending -> repository.getTrendingMovies()
        }
    }

    @Parcelize
    data class Payload(
        val requestType: MovieRequestType
    ) : Parcelable
}