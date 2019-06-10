package io.wax911.sample.data.mapper.meta

import io.wax911.sample.data.arch.mapper.TraktTrendMapper
import io.wax911.sample.data.dao.query.LanguageDao
import io.wax911.sample.data.model.attribute.Language
import io.wax911.sample.data.model.meta.MediaCategory
import timber.log.Timber

class LanguageMapper(
    private val languageDao: LanguageDao,
    private val mediaCategory: MediaCategory
) : TraktTrendMapper<List<Language>, List<Language>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: List<Language>): List<Language> {
        return source
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [handleResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<Language>) {
        if (mappedData.isNotEmpty())
            languageDao.insert(mappedData)
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert -> mappedData is empty")
    }
}