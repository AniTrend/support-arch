package io.wax911.support.core.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer

interface CompanionViewModel<VM, T> {

    /**
     * Returns the view model that should be used by activities and fragments.
     *
     * @param context valid null checked reference to a fragment context
     * @param observer type to which should be used when observing changes
     */
    fun newInstance(context: FragmentActivity, observer: Observer<T?>) : VM
}