package io.wax911.sample.core.settings

import android.content.Context
import co.anitrend.arch.extension.preference.BooleanPreference
import co.anitrend.arch.extension.preference.IntPreference
import co.anitrend.arch.extension.preference.LongPreference
import co.anitrend.arch.extension.preference.SupportPreference
import io.wax911.sample.core.R
import io.wax911.sample.data.settings.IAuthSettings

class Settings(context: Context) : SupportPreference(context), IAuthSettings {

    override var authenticatedUserId by LongPreference(
        R.string.setting_authenticated_user_id,
        IAuthSettings.INVALID_USER_ID,
        context.resources
    )

    override var isNewInstallation by BooleanPreference(
        R.string.setting_is_new_installation,
        true,
        context.resources
    )

    override var versionCode by IntPreference(
        R.string.setting_version_code,
        1,
        context.resources
    )

    companion object {
        val BINDINGS = arrayOf(
            Settings::class, SupportPreference::class, IAuthSettings::class
        )
    }
}