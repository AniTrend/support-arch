package co.anitrend.arch.extension.preference.contract

/**
 * Default preference contract
 */
interface ISupportPreference {


    var isNewInstallation: Boolean
    var versionCode: Int

    companion object {
        internal const val VERSION_CODE = "_versionCode"
        internal const val IS_NEW_INSTALLATION = "_isNewInstallation"
    }
}