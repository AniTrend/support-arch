package io.wax911.sample.data.api.endpoint

import io.wax911.sample.data.model.attribute.Country
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.sample.data.model.attribute.Language
import io.wax911.sample.data.model.meta.MediaCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MetaEndpoints {

    @GET("/genres/{categoryType}")
    suspend fun getGenres(
        @Path("categoryType") mediaCategory: MediaCategory
    ): Response<List<Genre>>

    @GET("/languages/{categoryType}")
    suspend fun getLanguages(
        @Path("categoryType") mediaCategory: MediaCategory
    ): Response<List<Language>>

    @GET("/countries/{categoryType}")
    suspend fun getCountries(
        @Path("categoryType") mediaCategory: MediaCategory
    ): Response<List<Country>>
}