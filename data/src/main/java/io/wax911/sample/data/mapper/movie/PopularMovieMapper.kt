package io.wax911.sample.data.mapper.movie

import androidx.paging.PagingRequestHelper
import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.dao.query.MovieDao
import io.wax911.sample.data.model.movie.Movie
import timber.log.Timber

class PopularMovieMapper(
    private val movieDao: MovieDao,
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : TraktTrendMapper<List<Movie>, List<Movie>>(
    pagingRequestHelper = pagingRequestHelper
) {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<Movie>): List<Movie> {
        return source.map {
            val traktId = it.ids.trakt
            it.copy(
                id = traktId
            )
        }
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [handleResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<Movie>) {
        if (mappedData.isNotEmpty())
            movieDao.upsert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Movie>) -> mappedData is empty")
    }
}