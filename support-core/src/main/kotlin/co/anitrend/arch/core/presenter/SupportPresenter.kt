package co.anitrend.arch.core.presenter

import android.content.Context
import co.anitrend.arch.core.presenter.contract.ISupportPresenter
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.extension.preference.SupportPreference

/**
 * An abstract declaration of what responsibilities a presenter should undertake
 *
 * @param context application based context
 * @param settings implementation of application preferences
 *
 * @see SupportPreference
 *
 * @since v0.9.X
 */
abstract class SupportPresenter<S : SupportPreference>(
    protected val context: Context,
    override val settings: S
): ISupportPresenter, SupportLifecycle {

    /**
     * Tag descriptor of the current module
     */
    override val moduleTag = javaClass.simpleName
}
