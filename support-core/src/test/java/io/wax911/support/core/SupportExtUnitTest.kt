package io.wax911.support.core

import io.wax911.support.extension.extension.capitalizeWords
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class SupportExtUnitTest {

    @Test
    fun capitalise_exception_isValid() {
        val exceptions = ArrayList<String>().apply {
            add("just Sentence")
        }
        val output = "this is just a normal sentence".capitalizeWords(exceptions)
        assertEquals("This Is just A Normal Sentence", output)
    }

    @Test
    fun capitalise_isValid() {
        val output = "this is just a normal sentence".capitalizeWords()
        assertEquals("This Is Just A Normal Sentence", output)
    }
}