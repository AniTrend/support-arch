package io.wax911.support.core.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer

@Deprecated(
    message = "This view model creator will be deprecated in v1.2 see [Koin](https://insert-koin.io/docs/2.0/getting-started/android-viewmodel/)",
    replaceWith = ReplaceWith(
        expression = "by viewmodel delegate, this is part of the Koin modules"
    ),
    level = DeprecationLevel.WARNING
)
interface CompanionViewModel<VM, T> {

    /**
     * Returns the view model that should be used by activities and fragments.
     *
     * @param context valid null checked reference to a fragment context
     * @param observer type to which should be used when observing changes
     */
    fun newInstance(context: FragmentActivity, observer: Observer<T?>) : VM
}