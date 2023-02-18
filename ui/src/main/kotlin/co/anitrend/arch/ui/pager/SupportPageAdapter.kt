/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.ui.pager

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import co.anitrend.arch.extension.ext.empty
import co.anitrend.arch.extension.ext.getStringList
import timber.log.Timber

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
    defaultBehavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
) : FragmentStatePagerAdapter(fragmentManager, defaultBehavior) {

    val titles = ArrayList<String>()

    /**
     * Sets the given array resources as standard strings titles
     *
     * @param titleRes array resource of which titles to use
     */
    open fun setPagerTitles(context: Context, @ArrayRes titleRes: Int) {
        if (titles.isNotEmpty()) {
            titles.clear()
        }
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
        if (position <= titles.size) {
            return titles[position].uppercase()
        }
        Timber.w("Position: $position doesn't have a corresponding title")
        return String.empty()
    }
}
