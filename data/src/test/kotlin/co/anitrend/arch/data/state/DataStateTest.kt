package co.anitrend.arch.data.state

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DataStateTest : ISupportCoroutine by Main() {

    private val dataSource = mockk<IDataSource>()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify event data source creation reflects initial load as loading and with refresh as idle`() = runTest {
        every { dataSource.loadState } returns flow {
            emit(LoadState.Loading())
        }

        val testFlow = flow {
            emit("Test event")
        }

        val state = dataSource.create(testFlow)

        val loadState = state.loadState.first()
        assertEquals(LoadState.Loading(), loadState)

        val retryState = state.refreshState.first()
        assertEquals(LoadState.Idle(), retryState)
    }

    @Test
    fun `verify event data source creation reflects initial load as idle and with refresh as idle`() = runTest {
        every { dataSource.loadState } returns flow {
            emit(LoadState.Idle())
        }

        val testFlow = flow {
            emit("Test event")
        }

        val state = dataSource.create(testFlow)

        val loadState = state.loadState.first()
        assertEquals(LoadState.Idle(), loadState)

        val retryState = state.refreshState.first()
        assertEquals(LoadState.Idle(), retryState)
    }
}