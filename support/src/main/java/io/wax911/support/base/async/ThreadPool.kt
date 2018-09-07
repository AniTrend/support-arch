package io.wax911.support.base.async

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadPool private constructor() {

    val executorService: ExecutorService by lazy {
        Executors.newCachedThreadPool()
    }

    fun execute(runnable: Runnable) {
        executorService.execute(runnable)
    }

    companion object {
        fun createInstance(): ThreadPool = ThreadPool()
    }
}
