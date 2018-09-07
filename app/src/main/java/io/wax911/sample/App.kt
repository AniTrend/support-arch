package io.wax911.sample

import android.app.Application
import androidx.room.Room
import io.wax911.sample.dao.DatabaseHelper
import org.greenrobot.eventbus.EventBus

class App : Application() {

    public lateinit var databaseHelper: DatabaseHelper

    init {
        EventBus.builder().logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(BuildConfig.DEBUG)
                .sendSubscriberExceptionEvent(BuildConfig.DEBUG)
                .throwSubscriberException(BuildConfig.DEBUG)
                .installDefaultEventBus()
    }

    private fun configureApplication() {
        databaseHelper = Room.databaseBuilder(applicationContext,
                DatabaseHelper::class.java, getString(R.string.app_name))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        configureApplication()
    }
}
