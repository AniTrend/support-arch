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

package co.anitrend.arch.extension

import co.anitrend.arch.extension.ext.capitalizeWords
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class SupportExtUnitTest {

    @Test
    fun `capitalize words in sentence except is correct`() {
        val exceptions = ArrayList<String>().apply {
            add("just"); add("sentence")
        }
        val output = "this is just a normal sentence".capitalizeWords(exceptions)
        assertEquals("This Is just A Normal sentence", output)
    }

    @Test
    fun `capitalize words in sentence is correct`() {
        val output = "this is just a normal sentence".capitalizeWords()
        assertEquals("This Is Just A Normal Sentence", output)
    }
}
