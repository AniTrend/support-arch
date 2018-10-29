package io.wax911.sample.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.wax911.sample.BuildConfig
import io.wax911.sample.R
import io.wax911.sample.model.BaseModel
import io.wax911.sample.model.WebToken
import io.wax911.support.util.SingletonUtil

@Database(entities = [WebToken::class, BaseModel::class], version = BuildConfig.VERSION_CODE)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun webTokenDao(): WebTokenDao
    abstract fun baseModelDao(): BaseModelDao

    companion object : SingletonUtil<DatabaseHelper, Context>({
        Room.databaseBuilder(it.applicationContext,
                DatabaseHelper::class.java, it.getString(R.string.app_name))
                .fallbackToDestructiveMigration()
                .build()
    })
}
