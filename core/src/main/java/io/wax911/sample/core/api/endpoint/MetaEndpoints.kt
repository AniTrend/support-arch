package io.wax911.sample.core.api.endpoint

import io.wax911.sample.core.model.meta.Country
import io.wax911.sample.core.model.meta.Genre
import io.wax911.sample.core.model.meta.Language
import io.wax911.sample.core.model.meta.MetaCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MetaEndpoints {

    @GET("/genres/{categoryType}")
    fun getGenres(
        @MetaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Genre>>

    @GET("/languages/{categoryType}")
    fun getLanguages(
        @MetaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Language>>

    @GET("/countries/{categoryType}")
    fun getCountries(
        @MetaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Country>>
}