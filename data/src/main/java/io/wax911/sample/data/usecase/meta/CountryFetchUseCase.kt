package io.wax911.sample.data.usecase.meta

import io.wax911.sample.data.api.endpoint.MetaEndpoint
import io.wax911.sample.data.dao.query.CountryDao
import io.wax911.sample.data.source.meta.CountryCoroutineDataSource
import io.wax911.sample.data.usecase.meta.contract.IMetaUseCase
import io.wax911.support.data.model.NetworkState

class CountryFetchUseCase(
    private val metaEndpoint: MetaEndpoint,
    private val countryDao: CountryDao
) : IMetaUseCase {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override suspend fun invoke(param: IMetaUseCase.Payload): NetworkState {
        val dataSource = CountryCoroutineDataSource(
            metaEndpoint = metaEndpoint,
            countryDao = countryDao,
            mediaCategory = param.mediaCategory
        )

        return dataSource()
    }
}