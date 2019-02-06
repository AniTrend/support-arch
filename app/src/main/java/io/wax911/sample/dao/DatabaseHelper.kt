package io.wax911.sample.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.wax911.sample.BuildConfig
import io.wax911.sample.R
import io.wax911.sample.model.WebToken
import io.wax911.support.factory.SingletonCreator

@Database(entities = [WebToken::class], version = BuildConfig.DATABASE_SCHEMA)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun webTokenDao(): WebTokenDao

    companion object : SingletonCreator<DatabaseHelper, Context>({
        Room.databaseBuilder(it.applicationContext,
                DatabaseHelper::class.java, it.getString(R.string.app_name))
                .fallbackToDestructiveMigration()
                .build()
    })
}
