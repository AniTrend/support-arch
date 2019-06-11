package io.wax911.sample.data.repository.movie

import android.os.Bundle
import androidx.paging.PagedList
import io.wax911.sample.data.api.endpoint.MovieEndpoint
import io.wax911.sample.data.model.movie.Movie
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.model.UiModel
import io.wax911.support.data.repository.SupportRepository
import org.koin.core.inject

class MovieRepository(
    val movieEndpoint: MovieEndpoint
) : SupportRepository<PagedList<Movie>>() {

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     */
    override fun invokeRequest(bundle: Bundle): UiModel<PagedList<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}