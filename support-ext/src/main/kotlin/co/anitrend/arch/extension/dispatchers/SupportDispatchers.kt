package co.anitrend.arch.extension.dispatchers

import kotlinx.coroutines.*

data class SupportDispatchers constructor(
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val computation: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    @OptIn(ObsoleteCoroutinesApi::class)
    val confined: ExecutorCoroutineDispatcher = newSingleThreadContext("ConfinedContext")
)