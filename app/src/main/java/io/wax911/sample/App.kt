package io.wax911.sample

import android.app.Application
import androidx.room.Room
import io.wax911.sample.dao.DatabaseHelper
import org.greenrobot.eventbus.EventBus

class App : Application() {

    init {
        EventBus.builder().logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(BuildConfig.DEBUG)
                .sendSubscriberExceptionEvent(BuildConfig.DEBUG)
                .throwSubscriberException(BuildConfig.DEBUG)
                .installDefaultEventBus()
    }

    override fun onCreate() {
        super.onCreate()
    }
}
