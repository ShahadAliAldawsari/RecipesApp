package com.example.recipesapp.dataLayer.remote

import com.example.recipesapp.dataLayer.remote.dto.RecipeDto
import com.example.recipesapp.dataLayer.remote.dto.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipe/search")
    suspend fun searchRecipeName(
        @Query("query")query: String,
        @Query("page") page: Int =1
    ): RecipeSearchResponse

    @GET("recipe/get")
    suspend fun getRecipeByID(
        @Query("id")id:Int
    ): RecipeDto
}