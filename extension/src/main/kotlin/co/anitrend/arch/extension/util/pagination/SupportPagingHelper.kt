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

package co.anitrend.arch.extension.util.pagination

import co.anitrend.arch.extension.util.pagination.contract.ISupportPagingHelper

/**
 * Paging helper, makes the manipulating of paging related variables easier
 *
 * @since v1.1.0
 */
data class SupportPagingHelper(
    var page: Int = 1,
    var pageOffset: Int = 0,
    val pageSize: Int,
    var isPagingLimit: Boolean,
) : ISupportPagingHelper {
    /**
     * Resets the paging parameters to their default
     */
    override fun onPageRefresh() {
        page = 1
        pageOffset = 0
        isPagingLimit = false
    }

    /**
     * Calculates the previous page offset and index
     */
    override fun onPagePrevious() {
        page -= 1
        pageOffset -= pageSize
    }

    /**
     * Calculates the next page offset and index
     */
    override fun onPageNext() {
        page += 1
        pageOffset += pageSize
    }

    /**
     * Checks if the current page and offset is the first
     */
    override fun isFirstPage() = page == 1 && pageOffset == 0
}
