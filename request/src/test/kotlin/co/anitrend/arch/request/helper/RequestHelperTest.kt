package co.anitrend.arch.request.helper

import android.util.Log
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.model.Request
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.request.helper.RequestHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class RequestHelperTest : ISupportCoroutine by Main() {

    private val moduleTag: String = javaClass.simpleName

    private val firstAttempt = AtomicBoolean(true)

    private val dispatcher = StandardTestDispatcher()

    private lateinit var requestHelper: AbstractRequestHelper

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        requestHelper = RequestHelper(dispatcher = SupportDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testRunsIfNotRunning() = runTest {
        val testRequest1 = async {
            requestHelper.runIfNotRunning(
                Request.Default("test_request_1", Request.Type.INITIAL)
            ) {
                if (firstAttempt.get()) {
                    val error =
                        Throwable("Request id: `test_request_1` simulated failure due")
                    it.recordFailure(RequestError(error))
                } else it.recordSuccess()
            }
        }

        val testRequest2 = async {
            requestHelper.runIfNotRunning(
                Request.Default("test_request_2", Request.Type.INITIAL)
            ) {
                it.recordSuccess()
            }
        }

        val actual = listOf(testRequest1, testRequest2)
            .map { it.await() }
        val expected = listOf(true, true)

        assertEquals(expected, actual)

        requestHelper.retryWithStatus(Request.Status.FAILED) {
            Log.v(moduleTag, "Retrying with status `Request.Status.FAILED`")
            firstAttempt.set(false)
        }

        assertEquals(false, firstAttempt.get())
    }
}
