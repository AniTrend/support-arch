package co.anitrend.arch.extension.preference.contract

import co.anitrend.arch.extension.settings.contract.AbstractSetting

/**
 * Default preference contract
 */
interface ISupportPreference {
    val isNewInstallation: AbstractSetting<Boolean>
    val versionCode: AbstractSetting<Int>
}