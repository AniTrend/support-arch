package io.wax911.support.pager

import android.os.Bundle
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import io.wax911.support.extension.empty
import io.wax911.support.extension.getStringList
import java.util.*
import kotlin.collections.ArrayList

abstract class SupportPageAdapter(protected val context: FragmentActivity): FragmentStatePagerAdapter(context.supportFragmentManager) {

    val bundle by lazy { Bundle() }
    val titles: ArrayList<String> by lazy { ArrayList<String>() }

    /**
     * Sets the given array resources as standard strings titles
     * <br/>
     *
     * @param titleRes array resource of which titles to use
     */
    fun setPagerTitles(@ArrayRes titleRes: Int) {
        if (titles.size > 0)
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
        return String.empty()
    }
}