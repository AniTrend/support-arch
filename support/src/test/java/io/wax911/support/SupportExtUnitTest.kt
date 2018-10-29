package io.wax911.support

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
        val list = ArrayList<String>()
        list.add("just")
        val output = "this is just a normal sentence".capitalizeWords(list)
        assertEquals("This Is just A Normal Sentence", output)
    }

    @Test
    fun capitalise_isValid() {
        val output = "this is just a normal sentence".capitalizeWords(ArrayList())
        assertEquals("This Is Just A Normal Sentence", output)
    }
}