package co.anitrend.arch.data.source.core

import co.anitrend.arch.request.model.Request
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SupportCoreDataSourceTest : ISupportCoroutine by Main() {
    
    private val dispatcher = StandardTestDispatcher()

    private val supportDispatcher = mockk<ISupportDispatcher>()
    
    private val dataSource = object : SupportCoreDataSource() {

        /**
         * Contract for multiple types of [coroutineDispatcher]
         */
        override val dispatcher: ISupportDispatcher
            get() = supportDispatcher

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            // test case, do nothing
        }

    }

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { supportDispatcher.io } returns dispatcher
        every { supportDispatcher.main } returns dispatcher
        every { supportDispatcher.computation } returns dispatcher
        every { supportDispatcher.confined } returns dispatcher
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify data source behavior on retry changes when request fails`() = runTest(timeout = 5.seconds) {
        val expected = Throwable("Request id: `test_request_1` simulated failure due")

        val requestHelper = dataSource.requestHelper

        requestHelper.runIfNotRunning(
            Request.Default("test_request_1", Request.Type.INITIAL)
        ) {
            it.recordFailure(RequestError(expected))
        }

        val actual = requestHelper.hasAnyWithStatus(Request.Status.FAILED)

        assertTrue(actual)
    }

    @Test
    fun `verify data source behavior on failure reports existing status of failed`() = runTest(timeout = 5.seconds) {
        val error = Throwable("Request id: `test_request_1` simulated failure due")

        val requestHelper = dataSource.requestHelper

        requestHelper.runIfNotRunning(
            Request.Default("test_request_1", Request.Type.INITIAL)
        ) {
            it.recordFailure(RequestError(error))
        }

        assertTrue(requestHelper.hasAnyWithStatus(Request.Status.FAILED))
    }

    @Test
    fun `verify data source behavior on retry failed changes when request fails`() = runTest(timeout = 5.seconds) {
        val error = Throwable("Request id: `test_request_1` simulated failure due")

        val requestHelper = dataSource.requestHelper

        var runCount = 0

        requestHelper.runIfNotRunning(
            Request.Default("test_request_1", Request.Type.INITIAL)
        ) {
            if (runCount < 1)
                it.recordFailure(RequestError(error))
            else
                it.recordSuccess()
            runCount++
        }

        assertTrue(requestHelper.hasAnyWithStatus(Request.Status.FAILED))

        dataSource.retryFailed()

        assertFalse(requestHelper.hasAnyWithStatus(Request.Status.FAILED))
    }
}