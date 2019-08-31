package io.wax911.sample.data.mapper.movie

import androidx.paging.PagingRequestHelper
import com.uwetrottmann.trakt5.entities.TrendingMovie
import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.datasource.local.query.MovieDao
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.entitiy.movie.contract.MovieEntityIds
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class TrendingMovieMapper(
    private val movieDao: MovieDao,
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : TraktTrendMapper<List<TrendingMovie>, List<MovieEntity>>(
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
    override suspend fun onResponseMapFrom(source: List<TrendingMovie>): List<MovieEntity> {
        return source.map {trendingMovie ->
            MovieEntity(
                ids = MovieEntityIds(
                    tmdb = trendingMovie.movie?.ids?.tmdb,
                    imdb = trendingMovie.movie?.ids?.imdb,
                    trakt = trendingMovie.movie?.ids?.trakt,
                    slug = trendingMovie.movie?.ids?.slug
                ),
                released = trendingMovie.movie?.released?.format(DateTimeFormatter.BASIC_ISO_DATE),
                tagline = trendingMovie.movie?.tagline,
                title = trendingMovie.movie?.title,
                year = trendingMovie.movie?.year,
                overview = trendingMovie.movie?.overview,
                runtime = trendingMovie.movie?.runtime,
                trailer = trendingMovie.movie?.trailer,
                homepage = trendingMovie.movie?.homepage,
                rating = trendingMovie.movie?.rating,
                votes = trendingMovie.movie?.votes,
                updatedAt = trendingMovie.movie?.updated_at?.format(DateTimeFormatter.BASIC_ISO_DATE),
                language = trendingMovie.movie?.language,
                genres = trendingMovie.movie?.genres,
                certification = trendingMovie.movie?.certification,
                country = null,
                trendingRank = trendingMovie.watchers
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