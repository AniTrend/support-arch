package io.wax911.sample.data.usecase.meta

import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.dao.query.GenreDao
import io.wax911.sample.data.source.meta.GenreCoroutineDataSource
import io.wax911.sample.data.usecase.meta.contract.IMetaUseCase
import io.wax911.support.data.model.NetworkState

class GenreFetchUseCase(
    private val metaEndpoints: MetaEndpoints,
    private val genreDao: GenreDao
) : IMetaUseCase {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override suspend fun invoke(param: IMetaUseCase.Payload): NetworkState {
        val dataSource = GenreCoroutineDataSource(
            metaEndpoints = metaEndpoints,
            genreDao = genreDao,
            mediaCategory = param.mediaCategory
        )

        return dataSource()
    }
}