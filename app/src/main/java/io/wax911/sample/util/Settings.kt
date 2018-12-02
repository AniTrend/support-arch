package io.wax911.sample.util

import android.content.Context
import io.wax911.support.custom.preference.SupportPreference
import io.wax911.support.util.InstanceUtil

class Settings private constructor(context: Context) : SupportPreference(context) {

    companion object : InstanceUtil<Settings, Context>({ Settings(it) }) {
        /** Application Base Options  */
        private const val updateChannel = "updateChannel"
    }
}
