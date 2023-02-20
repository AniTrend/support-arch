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

package co.anitrend.arch.request

import co.anitrend.arch.request.contract.IRequestHelper
import co.anitrend.arch.request.model.Request
import co.anitrend.arch.request.queue.RequestQueue
import co.anitrend.arch.request.report.RequestStatusReport
import co.anitrend.arch.request.report.contract.IRequestStatusReport
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Contract for request helper
 *
 * @since v1.3.0
 */
abstract class AbstractRequestHelper : IRequestHelper {

    protected val requestQueue: MutableList<RequestQueue> = mutableListOf()

    protected val listeners = CopyOnWriteArrayList<IRequestHelper.Listener>()

    /**
     * Provides a snapshot of status reports for all [requestQueue]
     *
     * @return [RequestStatusReport]
     */
    protected fun prepareStatusReportLocked(request: Request): IRequestStatusReport {
        return RequestStatusReport(request)
    }

    /**
     * Adds a new listener that will be notified when any request changes [IRequestHelper.Status].
     *
     * @param listener The listener that will be notified each time a request's status changes.
     * @return True if it is added, false otherwise (e.g. it already exists in the list).
     */
    override fun addListener(
        listener: IRequestHelper.Listener,
    ) = listeners.add(listener)

    /**
     * Removes the given listener from the listeners list.
     *
     * @param listener The listener that will be removed.
     * @return True if the listener is removed, false otherwise (e.g. it never existed)
     */
    override fun removeListener(
        listener: IRequestHelper.Listener,
    ) = listeners.remove(listener)

    /*
    * Helper extension to dispatch changes to all listeners
    */
    protected fun IRequestStatusReport.dispatchReport() {
        listeners.forEach {
            it.onStatusChange(this)
        }
    }
}
