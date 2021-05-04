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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * Contract for life cycle aware components, with all lifecycle events optional
 *
 * @see [co.anitrend.arch.extension.ext.attachComponent]
 * @see [co.anitrend.arch.extension.ext.detachComponent]
 *
 * @since v1.2.0
 */
interface SupportLifecycle : LifecycleObserver {

    /**
     * Triggered when the lifecycleOwner reaches it's onCreate state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Timber.v("onCreate")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onStart state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Timber.v("onStart")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onStop state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Timber.v("onStop")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onResume state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.v("onResume")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.v("onPause")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.v("onDestroy")
    }
}
