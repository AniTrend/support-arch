package io.wax911.sample.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import io.wax911.sample.BuildConfig
import io.wax911.sample.model.WebToken

@Database(entities = [WebToken::class], version = BuildConfig.VERSION_CODE)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun webTokenDao(): WebTokenDao
}
