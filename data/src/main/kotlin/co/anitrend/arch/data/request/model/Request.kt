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

package co.anitrend.arch.data.request.model

import co.anitrend.arch.domain.entities.RequestError

/**
 * Request type
 */
sealed class Request {

    abstract val id: String
    abstract val type: Type

    open var status: Status = Status.IDLE
    open var lastError: RequestError? = null

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Request -> {
                id == other.id && type == other.type
            }
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + (lastError?.hashCode() ?: 0)
        return result
    }

    /**
     * Represents a request type
     */
    enum class Type {
        INITIAL,
        BEFORE,
        AFTER,
    }

    /**
     * Represents the status of a Request for each [Request].
     */
    enum class Status {
        /** The request has not yet been started */
        IDLE,

        /** There is current a running request. */
        RUNNING,

        /** The last request has succeeded or no such requests have ever been run. */
        SUCCESS,

        /** The last request has failed. */
        FAILED,
    }

    class Default(
        override val id: String,
        override val type: Type,
    ) : Request()
}
