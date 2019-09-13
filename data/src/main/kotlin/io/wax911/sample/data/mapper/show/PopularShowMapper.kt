package io.wax911.sample.data.mapper.show

import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.datasource.local.query.ShowDao
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.entitiy.show.contract.ShowEntityIds
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class PopularShowMapper(
    private val showDao: ShowDao
) : TraktTrendMapper<List<com.uwetrottmann.trakt5.entities.Show>, List<ShowEntity>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [invoke]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<com.uwetrottmann.trakt5.entities.Show>): List<ShowEntity> {
        return source.map { show ->
            ShowEntity(
                ids = ShowEntityIds(
                    tmdb = show.ids?.tmdb,
                    imdb = show.ids?.imdb,
                    trakt = show.ids?.trakt,
                    slug = show.ids?.slug,
                    tvdb = show.ids?.tvdb,
                    tvrage = show.ids?.tvrage
                ),
                airs = show.airs,
                network = show.network,
                status = show.status,
                title = show.title,
                year = show.year,
                overview = show.overview,
                runtime = show.runtime,
                country = show.country,
                trailer = show.trailer,
                homepage = show.homepage,
                rating = show.rating,
                votes = show.votes,
                updatedAt = show.updated_at?.format(DateTimeFormatter.BASIC_ISO_DATE),
                language = show.language,
                genres = show.genres,
                certification = show.certification,
                aired_episodes = 0,
                trendingRank = null,
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