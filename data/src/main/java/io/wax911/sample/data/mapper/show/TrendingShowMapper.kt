package io.wax911.sample.data.mapper.show

import androidx.paging.PagingRequestHelper
import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.dao.query.ShowDao
import io.wax911.sample.data.model.container.Trending
import io.wax911.sample.data.model.show.Show
import io.wax911.support.data.mapper.SupportPagingDataMapper
import retrofit2.Response
import timber.log.Timber

class TrendingShowMapper(
    private val showDao: ShowDao,
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : TraktTrendMapper<List<Trending<Show>>, List<Show>>(
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
    override suspend fun onResponseMapFrom(source: List<Trending<Show>>): List<Show> {
        return source.map {
            val trendRank = it.watchers
            val traktId = it.result.ids.trakt
            it.result.copy(
                id = traktId,
                trendingRank = trendRank
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
    override suspend fun onResponseDatabaseInsert(mappedData: List<Show>) {
        if (mappedData.isNotEmpty())
            showDao.upsert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}