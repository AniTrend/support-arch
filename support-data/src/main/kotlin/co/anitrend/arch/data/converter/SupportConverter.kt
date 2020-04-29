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

package co.anitrend.arch.data.converter

import co.anitrend.arch.data.converter.contract.ISupportConverter

/**
 * Abstract implementation for a type converter
 *
 * @param fromType lambda for a type of mapper
 * @param toType lambda for a type of mapper
 *
 * @since 1.3.0
 */
abstract class SupportConverter<M, E>(
    override val fromType: (M) -> E,
    override val toType: (E) -> M
) : ISupportConverter<M, E>