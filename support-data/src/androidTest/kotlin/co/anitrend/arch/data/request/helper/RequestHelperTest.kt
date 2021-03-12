package co.anitrend.arch.data.request.helper

import android.util.Log
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4ClassRunner::class)
class RequestHelperTest : ISupportCoroutine by Main() {

    private val moduleTag: String = javaClass.simpleName

    private val firstAttempt = AtomicBoolean(true)

    private val dispatcher: ISupportDispatcher = SupportDispatcher()

    private val requestHelper: AbstractRequestHelper = RequestHelper(dispatcher)

    init {
        launch {
            requestHelper.createStatusFlow()
                .collect {
                    Log.v(moduleTag, it.toString())
                }
        }
    }

    @Test
    fun testRunsIfNotRunning() {
        launch {
            val ran = requestHelper.runIfNotRunning(
                Request.Default("test_request_1", Request.Type.INITIAL)
            ) {
                if (firstAttempt.get()) {
                    val error =
                        Throwable("Request id: `test_request_1` simulated failure due")
                    it.recordFailure(RequestError(error))
                } else it.recordSuccess()
            }
            Assert.assertTrue(ran)
        }

        launch {
            val ran = requestHelper.runIfNotRunning(
                Request.Default("test_request_2", Request.Type.INITIAL)
            ) {
                it.recordSuccess()
            }
            Assert.assertTrue(ran)
        }

        runBlocking {
            delay(250)
            requestHelper.retryWithStatus(Request.Status.FAILED) {
                Log.v(moduleTag, "Retrying with status `Request.Status.FAILED`")
                firstAttempt.set(false)
            }
            delay(5_000)
        }
    }
}