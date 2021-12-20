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

package co.anitrend.arch.extension.ext

/**
 * Capitalizes all the strings unless they are specified in the exceptions list
 *
 * @param exceptions words or characters to exclude during capitalization
 * @return list of capitalized strings
 */
fun Collection<String>.capitalizeWords(exceptions: List<String>? = null) =
    map { it.capitalizeWords(exceptions) }

/**
 * Gives the size of the collection by doing a null check first
 *
 * @return 0 if the collection is null or empty otherwise the size of the collection
 */
fun Collection<*>?.sizeOf() = when {
    this.isNullOrEmpty() -> 0
    else -> size
}

/**
 * Clears the current list and adds the new items into the collection replacing all items
 */
fun <C> MutableList<C>.replaceWith(collection: Collection<C>) {
    clear(); addAll(collection)
}
