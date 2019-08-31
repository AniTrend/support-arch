package io.wax911.sample.data.mapper.movie

import androidx.paging.PagingRequestHelper
import com.uwetrottmann.trakt5.entities.Movie
import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.datasource.local.query.MovieDao
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.entitiy.movie.contract.MovieEntityIds
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class PopularMovieMapper(
    private val movieDao: MovieDao,
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : TraktTrendMapper<List<Movie>, List<MovieEntity>>(
    pagingRequestHelper = pagingRequestHelper
) {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [invoke]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<Movie>): List<MovieEntity> {
        return source.map { movie ->
            MovieEntity(
                ids = MovieEntityIds(
                    tmdb = movie.ids?.tmdb,
                    imdb = movie.ids?.imdb,
                    trakt = movie.ids?.trakt,
                    slug = movie.ids?.slug
                ),
                released = movie.released?.format(DateTimeFormatter.BASIC_ISO_DATE),
                tagline = movie.tagline,
                title = movie.title,
                year = movie.year,
                overview = movie.overview,
                runtime = movie.runtime,
                trailer = movie.trailer,
                homepage = movie.homepage,
                rating = movie.rating,
                votes = movie.votes,
                updatedAt = movie.updated_at?.format(DateTimeFormatter.BASIC_ISO_DATE),
                language = movie.language,
                genres = movie.genres,
                certification = movie.certification,
                country = null,
                trendingRank = 0
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
    override suspend fun onResponseDatabaseInsert(mappedData: List<MovieEntity>) {
        if (mappedData.isNotEmpty())
            movieDao.upsert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<MovieEntity>) -> mappedData is empty")
    }
}