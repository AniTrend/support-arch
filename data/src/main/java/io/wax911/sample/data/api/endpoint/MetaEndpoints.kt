package io.wax911.sample.data.api.endpoint

import io.wax911.sample.data.model.attribute.Country
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.sample.data.model.attribute.Language
import io.wax911.sample.data.model.meta.MediaCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MetaEndpoints {

    @GET("/genres/{categoryType}")
    fun getGenres(
        @MediaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Genre>>

    @GET("/languages/{categoryType}")
    fun getLanguages(
        @MediaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Language>>

    @GET("/countries/{categoryType}")
    fun getCountries(
        @MediaCategory
        @Path("categoryType") genreType: String
    ): Call<List<Country>>
}