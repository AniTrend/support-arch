package io.wax911.sample.data.mapper.meta

import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.dao.query.GenreDao
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.sample.data.model.meta.MediaCategory
import timber.log.Timber

class GenreMapper(
    private val genreDao: GenreDao,
    private val mediaCategory: MediaCategory
) : TraktTrendMapper<List<Genre>, List<Genre>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<Genre>): List<Genre> {
        return source
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [handleResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<Genre>) {
        if (mappedData.isNotEmpty())
            genreDao.upsert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert -> mappedData is empty")
    }
}