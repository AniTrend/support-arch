package io.wax911.sample.data.repository.movie

import androidx.paging.PagedList
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.data.repository.SupportRepository

class MovieRepository(
    private val moviePagedListUseCase: IPagedMediaUseCase<Movie>
) : SupportRepository<PagedList<Movie>, IPagedMediaUseCase.Payload>() {

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param subject subject to apply business rules
     */
    override fun invoke(subject: IPagedMediaUseCase.Payload) =
        moviePagedListUseCase(subject)
}