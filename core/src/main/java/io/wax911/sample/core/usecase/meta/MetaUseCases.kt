package io.wax911.sample.core.usecase.meta

import io.wax911.sample.core.usecase.contract.ICoreUseCase
import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.dao.query.CountryDao
import io.wax911.sample.data.dao.query.GenreDao
import io.wax911.sample.data.dao.query.LanguageDao
import io.wax911.sample.data.model.meta.MediaCategory
import io.wax911.sample.data.source.meta.CountryCoroutineDataSource
import io.wax911.sample.data.source.meta.GenreCoroutineDataSource
import io.wax911.sample.data.source.meta.LanguageCoroutineDataSource
import io.wax911.support.data.model.NetworkState

class CountryWorkerUseCase(
    private val metaEndpoints: MetaEndpoints,
    private val countryDao: CountryDao
) : ICoreUseCase {

    /**
     * Solves a given use case problem defined by the implementing class
     */
    suspend operator fun invoke(mediaCategory: MediaCategory): NetworkState {
        val dataSource = CountryCoroutineDataSource(
            metaEndpoints = metaEndpoints,
            countryDao = countryDao,
            mediaCategory = mediaCategory
        )

        return dataSource()
    }
}

class GenreWorkerUseCase(
    private val metaEndpoints: MetaEndpoints,
    private val genreDao: GenreDao
) : ICoreUseCase {

    /**
     * Solves a given use case problem defined by the implementing class
     */
    suspend operator fun invoke(mediaCategory: MediaCategory): NetworkState {
        val dataSource = GenreCoroutineDataSource(
            metaEndpoints = metaEndpoints,
            genreDao = genreDao,
            mediaCategory = mediaCategory
        )

        return dataSource()
    }
}

class LanguageWorkerUseCase(
    private val metaEndpoints: MetaEndpoints,
    private val languageDao: LanguageDao
) : ICoreUseCase {

    /**
     * Solves a given use case problem defined by the implementing class
     */
    suspend operator fun invoke(mediaCategory: MediaCategory): NetworkState {
        val dataSource = LanguageCoroutineDataSource(
            metaEndpoints = metaEndpoints,
            languageDao = languageDao,
            mediaCategory = mediaCategory
        )

        return dataSource()
    }
}