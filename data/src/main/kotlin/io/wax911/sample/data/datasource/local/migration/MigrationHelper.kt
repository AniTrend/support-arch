package io.wax911.sample.data.datasource.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    /**
     * Should run the necessary migrations.
     *
     * This class cannot access any generated Dao in this method.
     *
     * This method is already called inside a transaction and that transaction might actually be a
     * composite transaction of all necessary `Migration`s.
     *
     * @param database The database instance
     */
    override fun migrate(database: SupportSQLiteDatabase) {
        with (database) {
            execSQL("drop table ShowEntity")
            execSQL("CREATE TABLE IF NOT EXISTS `ShowEntity` (`id` INTEGER NOT NULL, `first_aired` TEXT, `airs` TEXT, `network` TEXT, `status` TEXT NOT NULL, `aired_episodes` INTEGER, `title` TEXT NOT NULL, `year` INTEGER NOT NULL, `overview` TEXT, `runtime` INTEGER, `country` TEXT, `trailer` TEXT, `homepage` TEXT, `rating` REAL, `votes` INTEGER, `updated_at` TEXT, `language` TEXT, `genres` TEXT, `certification` TEXT, `anticipationRank` INTEGER NOT NULL, `trendingRank` INTEGER NOT NULL, `tvdb` INTEGER NOT NULL, `tvrage` INTEGER NOT NULL, `trakt` INTEGER NOT NULL, `slug` TEXT NOT NULL, `imdb` TEXT NOT NULL, `tmdb` INTEGER NOT NULL, PRIMARY KEY(`id`))")

            execSQL("drop table MovieEntity")
            execSQL("CREATE TABLE IF NOT EXISTS `MovieEntity` (`id` INTEGER NOT NULL, `tagline` TEXT NOT NULL, `released` TEXT, `title` TEXT NOT NULL, `year` INTEGER NOT NULL, `overview` TEXT, `runtime` INTEGER, `country` TEXT, `trailer` TEXT, `homepage` TEXT, `rating` REAL, `votes` INTEGER, `updated_at` TEXT, `language` TEXT, `genres` TEXT, `certification` TEXT, `anticipationRank` INTEGER NOT NULL, `trendingRank` INTEGER NOT NULL, `trakt` INTEGER NOT NULL, `slug` TEXT NOT NULL, `imdb` TEXT NOT NULL, `tmdb` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {

    /**
     * Should run the necessary migrations.
     *
     * This class cannot access any generated Dao in this method.
     *
     * This method is already called inside a transaction and that transaction might actually be a
     * composite transaction of all necessary `Migration`s.
     *
     * @param database The database instance
     */
    override fun migrate(database: SupportSQLiteDatabase) {
        with (database) {
            execSQL("drop table ShowEntity")
            execSQL("CREATE TABLE IF NOT EXISTS `ShowEntity` (`id` INTEGER NOT NULL, `first_aired` TEXT, `airs` TEXT, `network` TEXT, `status` TEXT NOT NULL, `aired_episodes` INTEGER, `title` TEXT NOT NULL, `year` INTEGER NOT NULL, `overview` TEXT, `runtime` INTEGER, `country` TEXT, `trailer` TEXT, `homepage` TEXT, `rating` REAL, `votes` INTEGER, `updated_at` TEXT, `language` TEXT, `genres` TEXT, `certification` TEXT, `anticipationRank` INTEGER NOT NULL, `trendingRank` INTEGER NOT NULL, `tvdb` INTEGER NOT NULL, `tvrage` INTEGER NOT NULL, `trakt` INTEGER NOT NULL, `slug` TEXT NOT NULL, `imdb` TEXT, `tmdb` INTEGER, PRIMARY KEY(`id`))")

            execSQL("drop table MovieEntity")
            execSQL("CREATE TABLE IF NOT EXISTS `MovieEntity` (`id` INTEGER NOT NULL, `tagline` TEXT NOT NULL, `released` TEXT, `title` TEXT NOT NULL, `year` INTEGER NOT NULL, `overview` TEXT, `runtime` INTEGER, `country` TEXT, `trailer` TEXT, `homepage` TEXT, `rating` REAL, `votes` INTEGER, `updated_at` TEXT, `language` TEXT, `genres` TEXT, `certification` TEXT, `anticipationRank` INTEGER NOT NULL, `trendingRank` INTEGER NOT NULL, `trakt` INTEGER NOT NULL, `slug` TEXT NOT NULL, `imdb` TEXT, `tmdb` INTEGER, PRIMARY KEY(`id`))")
        }
    }
}
