package io.wax911.support.data.source.contract

import android.os.Bundle
import androidx.lifecycle.LiveData

interface ISourceObservable<O> {

    /**
     * Returns the appropriate observable which we will monitor for updates,
     * common implementation may include but not limited to returning
     * data source live data for a database
     *
     * @param bundle request params, implementation is up to the developer
     */
    fun observerOnLiveDataWith(bundle: Bundle): LiveData<O>
}