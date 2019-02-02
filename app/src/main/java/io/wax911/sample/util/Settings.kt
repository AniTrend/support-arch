package io.wax911.sample.util

import android.content.Context
import io.wax911.support.preference.SupportPreference
import io.wax911.support.factory.InstanceCreator

class Settings private constructor(context: Context) : SupportPreference(context) {

    companion object : InstanceCreator<Settings, Context>({ Settings(it) }) {
        /** Application Base Options  */
        private const val updateChannel = "updateChannel"
    }
}
