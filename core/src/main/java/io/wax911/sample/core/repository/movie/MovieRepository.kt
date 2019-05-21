package io.wax911.sample.core.repository.movie

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.model.movie.Movie
import io.wax911.support.core.controller.contract.ISupportRequestClient
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.view.model.NetworkState
import io.wax911.support.core.view.model.UiModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieRepository(context: Context) : SupportRepository<PagedList<Movie>>(context), KoinComponent {

    override val networkClient: ISupportRequestClient by inject()
    private val databaseHelper: DatabaseHelper by inject()

    /**
     * When the application is connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the network using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun requestFromNetwork(bundle: Bundle): UiModel<PagedList<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun requestFromCache(bundle: Bundle): UiModel<PagedList<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     *
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    override fun refresh(bundle: Bundle): LiveData<NetworkState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    override fun insertResultIntoDb(bundle: Bundle, value: PagedList<Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}