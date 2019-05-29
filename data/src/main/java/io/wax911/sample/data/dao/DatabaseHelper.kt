package io.wax911.sample.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.auth.model.JsonWebToken
import io.wax911.sample.data.dao.converter.*
import io.wax911.sample.data.dao.migration.MIGRATION_1_2
import io.wax911.sample.data.dao.query.*
import io.wax911.sample.data.entitiy.trending.TrendingMovie
import io.wax911.sample.data.entitiy.trending.TrendingShow
import io.wax911.sample.data.model.attribute.Country
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.sample.data.model.attribute.Language
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.model.movie.contract.MovieIds
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.model.show.contract.ShowIds

@Database(
    entities = [
        JsonWebToken::class, Language::class, Genre::class, Country::class,
        Show::class, Movie::class, ShowIds::class, MovieIds::class,
        TrendingShow::class, TrendingMovie::class
    ],
    version = BuildConfig.DATABASE_SCHEMA_VERSION
)
@TypeConverters(
    MovieIdsConverter::class, OffsetDateTimeConverter::class, ShowIdsConverter::class,
    AirsConverter::class, StringListConverter::class
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun jsonTokenDao(): JsonWebTokenDao

    abstract fun languageDao(): LanguageDao
    abstract fun genreDao(): GenreDao
    abstract fun countryDao(): CountryDao

    abstract fun showDao(): ShowDao
    abstract fun movieDao(): MovieDao

    abstract fun showIdsDao(): ShowIdsDao
    abstract fun movieIdsDao(): MovieIdsDao

    abstract fun trendingShowDao(): TrendingShowDao
    abstract fun trendingMovieDao(): TrendingMovieDao

    companion object {

        fun newInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(
                context,
                DatabaseHelper::class.java,
                "trakt-trend"
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
