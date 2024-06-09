package co.anitrend.arch.request.listener

import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.helper.RequestHelper
import co.anitrend.arch.request.model.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import kotlin.test.Test
import java.util.concurrent.atomic.AtomicBoolean

class RequestHelperListenerTest : ISupportCoroutine by Main() {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var request: AbstractRequestHelper

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        request = RequestHelper(main = dispatcher, io = dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `assure that a failed request results in request containing failed status`() = runTest {
        request.runIfNotRunning(Request.Default("failing_request_#", Request.Type.INITIAL)) {
            val error = RequestError(
                Throwable("Request id: `failing_request_#` simulated failure due to some issue")
            )
            it.recordFailure(error)
        }

        assertTrue(request.hasAnyWithStatus(Request.Status.FAILED))
    }

    @Test
    fun `assure that failing task can be re-ran and reflects new status`() = runTest {
        val firstAttempt = AtomicBoolean(true)

        request.runIfNotRunning(Request.Default("request", Request.Type.INITIAL)) {
            if (firstAttempt.get()) {
                val error =
                    Throwable("Request id: `request` simulated failure")
                it.recordFailure(RequestError(error))
            } else it.recordSuccess()
        }

        advanceUntilIdle()

        assertEquals(true, request.hasAnyWithStatus(Request.Status.FAILED))

        request.retryWithStatus(Request.Status.FAILED) {
            firstAttempt.set(false)
        }

        advanceUntilIdle()

        assertEquals(false, request.hasAnyWithStatus(Request.Status.FAILED))
        assertEquals(false, firstAttempt.get())
    }

    @Test
    fun `assure that failing task can be re-ran`() = runTest {
        request.runIfNotRunning(Request.Default("test_request_1", Request.Type.INITIAL)) {
            it.recordSuccess()
        }

        request.runIfNotRunning(Request.Default("test_request_2", Request.Type.INITIAL)) {
            it.recordSuccess()
        }

        advanceUntilIdle()

        assertEquals(false, request.hasAnyWithStatus(Request.Status.FAILED))
        assertEquals(false, request.hasAnyWithStatus(Request.Status.RUNNING))
        assertEquals(false, request.hasAnyWithStatus(Request.Status.IDLE))
    }
}
