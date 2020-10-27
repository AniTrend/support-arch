package co.anitrend.arch.ui.common

/**
 * Contract for components that should support [androidx.fragment.app.FragmentActivity.onBackPressed],
 * preferably this should be applied to fragments.
 *
 * @see [co.anitrend.arch.ui.activity.SupportActivity.onBackPressed]
 */
@Deprecated("Use OnBackPressedCallback instead")
interface ISupportActionUp {

    /**
     * Returns a boolean that can be used to block/unblock
     * [co.anitrend.arch.ui.activity.SupportActivity.onBackPressed] from calling it's super method
     * needed to finish an activity.
     *
     * @return true or false depending on the override implementation
     */
    fun hasBackPressableAction(): Boolean = false
}