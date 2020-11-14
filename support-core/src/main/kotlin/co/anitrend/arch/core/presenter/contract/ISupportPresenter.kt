package co.anitrend.arch.core.presenter.contract

import co.anitrend.arch.extension.preference.SupportPreference

/**
 * Contract for presenters
 */
interface ISupportPresenter {

    /**
     * Contract for settings with base properties
     */
    val settings: SupportPreference
}