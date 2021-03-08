package co.anitrend.arch.data.paging.contract

import androidx.paging.DataSource

/**
 * Invalidation callback for DataSource.
 *
 * Used to signal when a DataSource a data source has become invalid,
 * and that a new data source is needed to continue loading data.
 */
internal interface ClearInvalidatedCallback : DataSource.InvalidatedCallback {
    fun clear()
}