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
import io.wax911.sample.data.dao.migration.MIGRATION_2_3
import io.wax911.sample.data.dao.query.*
import io.wax911.sample.data.model.attribute.Country
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.sample.data.model.attribute.Language
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.model.show.Show

@Database(
    entities = [
        JsonWebToken::class, Language::class, Genre::class, Country::class,
        Show::class, Movie::class
    ],
    version = BuildConfig.DATABASE_SCHEMA_VERSION
)
@TypeConverters(
    MovieIdsConverter::class, OffsetDateTimeConverter::class, ShowIdsConverter::class,
    AirsConverter::class, StringListConverter::class
)
abstract class TraktTrendDatabase : RoomDatabase() {

    abstract fun jsonTokenDao(): JsonWebTokenDao

    abstract fun languageDao(): LanguageDao
    abstract fun genreDao(): GenreDao
    abstract fun countryDao(): CountryDao

    abstract fun showDao(): ShowDao
    abstract fun movieDao(): MovieDao

    companion object {

        fun newInstance(context: Context): TraktTrendDatabase {
            return Room.databaseBuilder(
                context,
                TraktTrendDatabase::class.java,
                "trakt-trend"
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}
