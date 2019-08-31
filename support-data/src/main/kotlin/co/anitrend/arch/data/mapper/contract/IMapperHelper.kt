package co.anitrend.arch.data.mapper.contract

import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.Deferred

/**
 * Contract mapper for wrapping future requests, commonly in the form of a
 * [Deferred] or [retrofit2.Call]
 *
 * @since v1.1.0
 */
interface IMapperHelper<in S> {

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @return [NetworkState] for the deferred result
     */
    suspend operator fun invoke(resource: S): NetworkState
}