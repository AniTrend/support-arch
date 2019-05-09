package io.wax911.sample.core.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.wax911.sample.core.BuildConfig
import io.wax911.sample.core.R
import io.wax911.sample.core.auth.model.JsonWebToken
import io.wax911.sample.core.dao.converter.*
import io.wax911.sample.core.dao.migration.MIGRATION_1_2
import io.wax911.sample.core.dao.query.*
import io.wax911.sample.core.data.entitiy.trending.TrendingMovie
import io.wax911.sample.core.data.entitiy.trending.TrendingShow
import io.wax911.sample.core.model.attribute.Country
import io.wax911.sample.core.model.attribute.Genre
import io.wax911.sample.core.model.attribute.Language
import io.wax911.sample.core.model.movie.Movie
import io.wax911.sample.core.model.movie.contract.MovieIds
import io.wax911.sample.core.model.show.Show
import io.wax911.sample.core.model.show.contract.ShowIds
import io.wax911.support.core.factory.InstanceCreator

@Database(
    entities = [
        JsonWebToken::class, Language::class, Genre::class, Country::class,
        Show::class, Movie::class, ShowIds::class, MovieIds::class,
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

    companion object : InstanceCreator<DatabaseHelper, Context>({ context ->
        Room.databaseBuilder(
            context,
            DatabaseHelper::class.java,
            context.getString(R.string.module_name)
        ).fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_2)
            .build()
    })
}
