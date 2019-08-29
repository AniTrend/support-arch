package io.wax911.support.extension

import org.junit.Assert.assertEquals
import org.junit.Test

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