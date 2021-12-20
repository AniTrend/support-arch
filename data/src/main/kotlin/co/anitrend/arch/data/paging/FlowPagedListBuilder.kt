/**
 * Copyright 2019 AniTrend
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

package co.anitrend.arch.data.paging

import android.annotation.SuppressLint
import androidx.paging.DataSource
import androidx.paging.PagedList
import co.anitrend.arch.data.paging.builder.AbstractFlowPagedListBuilder
import co.anitrend.arch.data.paging.contract.ClearInvalidatedCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Builder for `Flow<PagedList>` given a [DataSource.Factory] and a [PagedList.Config].
 *
 * The required parameters are in the constructor, so you can simply construct and build, or
 * optionally enable extra features (such as initial load key, or BoundaryCallback).
 *
 * The returned Flow will already be subscribed on the [fetchDispatcher], and will perform all
 * loading on that scheduler. It will already be observed on [notifyDispatcher],
 * and will dispatch new PagedLists, as well as their updates to that scheduler.
 *
 * @param K Type of input valued used to load data from the DataSource. Must be integer if
 * you're using PositionalDataSource.
 * @param V Item type being presented.
 */
class FlowPagedListBuilder<K, V>(
    override val dataSourceFactory: DataSource.Factory<K, V>,
    override val config: PagedList.Config,
    override var initialLoadKey: K? = null,
    override var boundaryCallback: PagedList.BoundaryCallback<*>? = null,
    override var notifyDispatcher: CoroutineDispatcher = Dispatchers.Main,
    override var fetchDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AbstractFlowPagedListBuilder<K, V>() {

    /**
     * Constructs a `Flow<PagedList>`.
     *
     * @return The Flow of PagedLists
     */
    @SuppressLint("RestrictedApi")
    override fun buildFlow(): Flow<PagedList<V>> = channelFlow {

        val invalidateCallback = object : ClearInvalidatedCallback {
            private var prevList: PagedList<V>? = null
            private var dataSource: DataSource<K, V>? = null

            override fun onInvalidated() = sendNewList()

            override fun clear() {
                dataSource?.removeInvalidatedCallback(this)
            }

            private fun sendNewList() {
                launch(fetchDispatcher) {
                    // Compute on the fetch dispatcher
                    val list = createPagedList()

                    withContext(notifyDispatcher) {
                        // Send on the notify dispatcher
                        send(list)
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            private fun createPagedList(): PagedList<V> {
                do {
                    dataSource?.removeInvalidatedCallback(this)

                    dataSource = dataSourceFactory.create().also {
                        it.addInvalidatedCallback(this)
                    }

                    val list = PagedList.Builder(dataSource!!, config)
                        .setNotifyExecutor(notifyDispatcher.asExecutor())
                        .setFetchExecutor(fetchDispatcher.asExecutor())
                        .setBoundaryCallback(boundaryCallback)
                        .setInitialKey(prevList?.lastKey as? K ?: initialLoadKey)
                        .build()
                        .also { prevList = it }
                } while (list.isDetached)

                return prevList!!
            }
        }

        // Do the initial load
        invalidateCallback.onInvalidated()

        awaitClose {
            invalidateCallback.clear()
        }
    }
}
