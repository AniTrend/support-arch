package co.anitrend.arch.core.presenter

import android.content.Context
import android.content.SharedPreferences
import co.anitrend.arch.core.presenter.contract.ISupportPresenter
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.extension.preference.SupportSettings
import co.anitrend.arch.extension.preference.event.OnSharedPreferenceBinder

/**
 * An abstract declaration of what responsibilities a presenter should undertake
 *
 * @param context application based context
 * @param settings implementation of application preferences
 *
 * @see SupportSettings
 *
 * @since v0.9.X
 */
abstract class SupportPresenter<S : SupportSettings>(
    protected val context: Context,
    override val settings: S
): ISupportPresenter, OnSharedPreferenceBinder, SupportLifecycle {

    override val moduleTag = javaClass.simpleName

    override var isActionModeEnabled = false

    override var offScreenPagerLimit: Int = 3

    /**
     * Unregister any listeners from fragments or activities
     */
    override fun onPause(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        settings.unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    /**
     * Register any listeners from fragments or activities
     */
    override fun onResume(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        settings.registerOnSharedPreferenceChangeListener(changeListener)
    }
}
