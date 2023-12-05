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

package co.anitrend.arch.extension.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

/**
 * Contract for life cycle aware components, with all lifecycle events optional
 *
 * @see [co.anitrend.arch.extension.ext.attachComponent]
 * @see [co.anitrend.arch.extension.ext.detachComponent]
 *
 * @since v1.2.0
 */
interface SupportLifecycle : DefaultLifecycleObserver {
    /**
     * Notifies that `ON_CREATE` event occurred.
     *
     * This method will be called after the [LifecycleOwner]'s `onCreate`
     * method returns.
     *
     * @param owner the component, whose state was changed
     */
    override fun onCreate(owner: LifecycleOwner) {
        Timber.v("onCreate($owner)")
    }

    /**
     * Notifies that `ON_START` event occurred.
     *
     * This method will be called after the [LifecycleOwner]'s `onStart` method returns.
     *
     * @param owner the component, whose state was changed
     */
    override fun onStart(owner: LifecycleOwner) {
        Timber.v("onStart($owner)")
    }

    /**
     * Notifies that `ON_RESUME` event occurred.
     *
     * This method will be called after the [LifecycleOwner]'s `onResume`
     * method returns.
     *
     * @param owner the component, whose state was changed
     */
    override fun onResume(owner: LifecycleOwner) {
        Timber.v("onResume($owner)")
    }

    /**
     * Notifies that `ON_PAUSE` event occurred.
     *
     * This method will be called before the [LifecycleOwner]'s `onPause` method
     * is called.
     *
     * @param owner the component, whose state was changed
     */
    override fun onPause(owner: LifecycleOwner) {
        Timber.v("onPause($owner)")
    }

    /**
     * Notifies that `ON_STOP` event occurred.
     *
     * This method will be called before the [LifecycleOwner]'s `onStop` method
     * is called.
     *
     * @param owner the component, whose state was changed
     */
    override fun onStop(owner: LifecycleOwner) {
        Timber.v("onStop($owner)")
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
        Timber.v("onDestroy($owner)")
    }
}
