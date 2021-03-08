package co.anitrend.arch.data.source.contract

interface ISource {

    /**
     * Informs the data source to invalidate itself and should invoke network refresh or reload
     */
    suspend fun invalidate()
}