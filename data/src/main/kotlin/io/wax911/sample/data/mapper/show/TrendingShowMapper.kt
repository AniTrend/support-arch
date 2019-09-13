package io.wax911.sample.data.mapper.show

import com.uwetrottmann.trakt5.entities.TrendingShow
import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.datasource.local.query.ShowDao
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.entitiy.show.contract.ShowEntityIds
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class TrendingShowMapper(
    private val showDao: ShowDao
) : TraktTrendMapper<List<TrendingShow>, List<ShowEntity>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [invoke]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<TrendingShow>): List<ShowEntity> {
        return source.map { trendingShow ->
            ShowEntity(
                ids = ShowEntityIds(
                    tmdb = trendingShow.show?.ids?.tmdb,
                    imdb = trendingShow.show?.ids?.imdb,
                    trakt = trendingShow.show?.ids?.trakt,
                    slug = trendingShow.show?.ids?.slug,
                    tvdb = trendingShow.show?.ids?.tvdb,
                    tvrage = trendingShow.show?.ids?.tvrage
                ),
                airs = trendingShow.show?.airs,
                network = trendingShow.show?.network,
                status = trendingShow.show?.status,
                title = trendingShow.show?.title,
                year = trendingShow.show?.year,
                overview = trendingShow.show?.overview,
                runtime = trendingShow.show?.runtime,
                country = trendingShow.show?.country,
                trailer = trendingShow.show?.trailer,
                homepage = trendingShow.show?.homepage,
                rating = trendingShow.show?.rating,
                votes = trendingShow.show?.votes,
                updatedAt = trendingShow.show?.updated_at?.format(DateTimeFormatter.BASIC_ISO_DATE),
                language = trendingShow.show?.language,
                genres = trendingShow.show?.genres,
                certification = trendingShow.show?.certification,
                aired_episodes = 0,
                trendingRank = trendingShow.watchers,
                first_aired = null
            )
        }
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [invoke]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<ShowEntity>) {
        if (mappedData.isNotEmpty())
            showDao.upsert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<ShowEntity>) -> mappedData is empty")
    }
}