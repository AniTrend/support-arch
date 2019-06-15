package io.wax911.sample.data.usecase.meta

import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.dao.query.LanguageDao
import io.wax911.sample.data.source.meta.LanguageCoroutineDataSource
import io.wax911.sample.data.usecase.meta.contract.IMetaUseCase
import io.wax911.support.data.model.NetworkState

class LanguageFetchUseCase(
    private val metaEndpoints: MetaEndpoints,
    private val languageDao: LanguageDao
) : IMetaUseCase {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override suspend fun invoke(param: IMetaUseCase.Payload): NetworkState {
        val dataSource = LanguageCoroutineDataSource(
            mediaCategory = param.mediaCategory,
            metaEndpoints = metaEndpoints,
            languageDao = languageDao
        )

        return dataSource()
    }
}