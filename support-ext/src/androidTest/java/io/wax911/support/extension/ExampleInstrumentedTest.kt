package io.wax911.support.extension

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {

    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().context }

    @Test
    fun useAppContext() {
        assertEquals("io.wax911.support.extension.test", appContext.packageName)
    }
}
