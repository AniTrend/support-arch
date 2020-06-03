package co.anitrend.arch.core.presenter.contract

import co.anitrend.arch.extension.preference.contract.ISupportSettings

/**
 * Contract for presenters
 */
interface ISupportPresenter {

    /**
     * Contract for settings with base properties
     */
    val settings: ISupportSettings
}