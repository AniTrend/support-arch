package co.anitrend.arch.ui.activity.contract

import android.os.Bundle

/**
 * Contract for implementing [androidx.appcompat.app.AppCompatActivity] components
 *
 * @since v1.3.0
 */
interface ISupportActivity {

    val moduleTag: String

    /**
     * A simple value that can be used when making permission requests,
     * the value can be overridden in the implementing class
     *
     * [compatViewPermissionValue] has a default value of 110
     */
    val compatViewPermissionValue: Int
        get() = 110

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    fun initializeComponents(savedInstanceState: Bundle?)

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     *
     * @see [androidx.fragment.app.FragmentActivity.requestPermissions]
     */
    fun requestPermissionIfMissing(manifestPermission: String): Boolean = false
}