package com.example.recipesapp.dataLayer.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchResponse(
    val count:Int,
    val result: List<RecipeDto>
)