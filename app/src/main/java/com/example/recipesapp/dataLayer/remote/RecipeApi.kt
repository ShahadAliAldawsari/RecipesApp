package com.example.recipesapp.dataLayer.remote

import com.example.recipesapp.dataLayer.remote.dto.RecipeDto
import com.example.recipesapp.dataLayer.remote.dto.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipeApi {
    @Headers("Authorization: Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    @GET("api/recipe/search")
    suspend fun searchRecipeName(
        @Query("query")query: String,
        @Query("page") page: Int =1
    ): RecipeSearchResponse

    @Headers("Authorization: Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    @GET("api/recipe/get")
    suspend fun getRecipeByID(
        @Query("id")id:Int
    ): RecipeDto
}