package co.anitrend.arch.ui.pager

import android.content.Context
import android.os.Bundle
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import co.anitrend.arch.extension.ext.empty
import co.anitrend.arch.extension.ext.getStringList
import timber.log.Timber
import java.util.*

/**
 * If [FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT] is passed in, then only the current
 * Fragment is in the [androidx.lifecycle.Lifecycle.State.RESUMED] state, while all other fragments are
 * capped at [androidx.lifecycle.Lifecycle.State.STARTED].
 *
 * If [FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT] is used, then all fragments are in the
 * [androidx.lifecycle.Lifecycle.State.RESUMED] state and there will be callbacks to
 * [androidx.fragment.app.Fragment.setUserVisibleHint].
 *
 * @param fragmentManager Fragment manager that will interact with this adapter
 * @param defaultBehavior Defaulted to [FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT]
 *
 * @since v0.9.X
 */
abstract class SupportPageAdapter(
    fragmentManager: FragmentManager,
    defaultBehavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
): FragmentStatePagerAdapter(fragmentManager, defaultBehavior) {

    val titles = ArrayList<String>()

    /**
     * Sets the given array resources as standard strings titles
     *
     * @param titleRes array resource of which titles to use
     */
    open fun setPagerTitles(context: Context, @ArrayRes titleRes: Int) {
        if (titles.isNotEmpty())
            titles.clear()
        titles.addAll(context.getStringList(titleRes))
    }

    /**
     * Return the number of views available.
     */
    override fun getCount() = titles.size

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    abstract override fun getItem(position: Int): Fragment

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    override fun getPageTitle(position: Int): CharSequence {
        if (position <= titles.size)
            return titles[position].toUpperCase(Locale.getDefault())
        Timber.w("Page title at position: $position doesn't have a corresponding title, returning empty string")
        return String.empty()
    }
}