package co.anitrend.arch.core.presenter.contract

import co.anitrend.arch.extension.preference.contract.ISupportSettings

/**
 * Contract for presenters
 */
interface ISupportPresenter {

    val settings: ISupportSettings

    /**
     * Enables or disables action mode, behaviour should be implemented in your adapter,
     * in ItemClickLister. Default value for this property is false
     */
    @Deprecated("May be removed in 1.3.0-stable when support-recycler module reaches stable")
    var isActionModeEnabled: Boolean


    /**
     * Indicates the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @see [androidx.viewpager.widget.ViewPager.setOffscreenPageLimit]
     */
    @Deprecated("May be removed in 1.3.0-stable")
    var offScreenPagerLimit: Int
}