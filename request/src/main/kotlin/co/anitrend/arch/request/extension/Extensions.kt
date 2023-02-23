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

package co.anitrend.arch.request.extension

import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.listener.RequestHelperListener
import co.anitrend.arch.request.report.contract.IRequestStatusReport
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

internal fun IRequestStatusReport.getRequestError(): RequestError {
    return co.anitrend.arch.request.model.Request.Type.values().mapNotNull {
        getErrorFor(it)
    }.first()
}

internal fun IRequestStatusReport.getTypeLoadPosition() = when (getType()) {
    co.anitrend.arch.request.model.Request.Type.INITIAL -> LoadState.Position.UNDEFINED
    co.anitrend.arch.request.model.Request.Type.BEFORE -> LoadState.Position.TOP
    co.anitrend.arch.request.model.Request.Type.AFTER -> LoadState.Position.BOTTOM
}

/**
 * Creates a live data observable on the paging request helper
 */
fun AbstractRequestHelper.createStatusFlow() = callbackFlow {
    val requestListener = RequestHelperListener(scope = this)
    addListener(requestListener)
    awaitClose { removeListener(requestListener) }
}
