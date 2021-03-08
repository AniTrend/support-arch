package co.anitrend.arch.data.paging.builder

import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Builder for `Flow<PagedList>` given a [DataSource.Factory] and a [PagedList.Config].
 */
abstract class AbstractFlowPagedListBuilder<K, V> {

    /**
     * First loading key passed to the first PagedList/DataSource.
     *
     * When a new PagedList/DataSource pair is created after the first, it acquires a load key from
     * the previous generation so that data is loaded around the position already being observed.
     *
     * @param key Initial load key passed to the first PagedList/DataSource.
     */
    abstract var initialLoadKey: K?

    /**
     * Typically used to load additional data from network when paging from local storage.
     *
     * Pass a BoundaryCallback to listen to when the PagedList runs out of data to load. If this
     * method is not called, or `null` is passed, you will not be notified when each
     * DataSource runs out of data to provide to its PagedList.
     *
     * If you are paging from a DataSource.Factory backed by local storage, you can set a
     * BoundaryCallback to know when there is no more information to page from local storage.
     * This is useful to page from the network when local storage is a cache of network data.
     *
     * Note that when using a BoundaryCallback with a `Flow<PagedList>`, method calls
     * on the callback may be dispatched multiple times - one for each PagedList/DataSource
     * pair. If loading network data from a BoundaryCallback, you should prevent multiple
     * dispatches of the same method from triggering multiple simultaneous network loads.
     */
    abstract var boundaryCallback: PagedList.BoundaryCallback<*>?

    protected abstract val dataSourceFactory: DataSource.Factory<K, V>
    protected abstract val config: PagedList.Config

    protected abstract var notifyDispatcher: CoroutineDispatcher
    protected abstract var fetchDispatcher: CoroutineDispatcher

    /**
     * Constructs a `Flow<PagedList>`.
     *
     * @return The Flow of PagedLists
     */
    abstract fun buildFlow(): Flow<PagedList<V>>


}