/*
 *    Copyright 2020 AniTrend
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package co.anitrend.arch.data.converter.contract


/**
 * Contract for type converter
 *
 * @param M generic type of interchangeable type which can be mapped to and from
 * @param E generic type of interchangeable type which can be mapped to and from
 *
 * @since 1.3.0
 */
interface ISupportConverter<M, E> {
    /**
     * Convert from a single type [M] to [E]
     */
    fun convertFrom(item: M): E

    /**
     * Convert from [Collection] types of [M] to [E]
     */
    fun convertFrom(items: Collection<M>) =
        items.map { convertFrom(it) }

    /**
     * Convert from a single type [E] to [M]
     */
    fun convertTo(item: E): M

    /**
     * Convert to [Collection] types of [E] to [M]
     */
    fun convertTo(items: Collection<E>) =
        items.map { convertTo(it) }
}