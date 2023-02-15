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

package co.anitrend.arch.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import timber.log.Timber

/**
 * Extends [RecyclerView] and adds position management on stateConfiguration changes, and
 * lifecycle aware callbacks
 *
 * @see co.anitrend.arch.extension.ext.attachComponent
 * @see co.anitrend.arch.extension.ext.detachComponent
 *
 * @since v0.9.X
 */
open class SupportRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : RecyclerView(context, attrs, defStyle), SupportLifecycle {

    /**
     * Tells this recycler to set it's adapters instance to false when [SupportLifecycle.onDestroy]
     * is triggered, assuming you've called [co.anitrend.arch.extension.ext.attachComponent] on this
     */
    open var autoClearAdapter: Boolean = true

    /**
     * Indicates whether or not a scroll listener has already been added to this recycler
     */
    protected open var hasScrollListener: Boolean = false

    /**
     * Add a listener that will be notified of any changes in scroll state or position.
     *
     * Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call [.clearOnScrollListeners]
     * to remove all attached listeners.
     *
     * @param listener listener to set or null to clear
     */
    override fun addOnScrollListener(listener: OnScrollListener) {
        if (!hasScrollListener) {
            super.addOnScrollListener(listener)
            hasScrollListener = true
        }
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    override fun clearOnScrollListeners() {
        super.clearOnScrollListeners()
        hasScrollListener = false
    }

    /**
     * Notifies that `ON_DESTROY` event occurred.
     *
     * This method will be called before the [LifecycleOwner]'s `onDestroy` method
     * is called.
     *
     * @param owner the component, whose state was changed
     */
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (autoClearAdapter) {
            Timber.v("Clearing adapter reference for this recycler to avoid potential leaks")
            adapter = null
        }
    }
}
