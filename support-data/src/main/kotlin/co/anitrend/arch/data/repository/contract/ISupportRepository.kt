package co.anitrend.arch.data.repository.contract

import co.anitrend.arch.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *
 *
 * @since v1.1.0
 */
interface ISupportRepository : SupportCoroutineHelper {

    /**
     * Deals with cancellation of any pending or on going operations that the repository is busy with
     */
    fun onCleared() {
        cancelAllChildren()
    }

    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}