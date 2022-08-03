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

package co.anitrend.arch.data.request.extension

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.listener.RequestHelperListener
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.request.report.contract.IRequestStatusReport
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

internal fun IRequestStatusReport.getRequestError(): RequestError {
    return Request.Type.values().mapNotNull {
        getErrorFor(it)
    }.first()
}

internal fun IRequestStatusReport.getTypeLoadPosition() = when (getType()) {
    Request.Type.INITIAL -> LoadState.Position.UNDEFINED
    Request.Type.BEFORE -> LoadState.Position.TOP
    Request.Type.AFTER -> LoadState.Position.BOTTOM
}

/**
 * Creates a live data observable on the paging request helper
 */
internal fun AbstractRequestHelper.createStatusFlow() = callbackFlow {
    val requestListener = RequestHelperListener(scope = this)
    addListener(requestListener)
    awaitClose { removeListener(requestListener) }
}
