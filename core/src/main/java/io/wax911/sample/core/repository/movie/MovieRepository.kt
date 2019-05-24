package io.wax911.sample.core.repository.movie

import android.content.Context
import android.os.Bundle
import androidx.paging.PagedList
import io.wax911.sample.core.model.movie.Movie
import io.wax911.support.core.factory.contract.IRetrofitFactory
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.view.model.UiModel
import org.koin.core.inject

class MovieRepository(context: Context) : SupportRepository<PagedList<Movie>>(context) {

    override val retroFactory: IRetrofitFactory by inject()

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     */
    override fun invokeRequest(bundle: Bundle): UiModel<PagedList<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}