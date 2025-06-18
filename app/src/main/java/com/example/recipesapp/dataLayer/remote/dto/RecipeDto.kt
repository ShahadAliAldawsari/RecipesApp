package com.example.recipesapp.dataLayer.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val pk:Int,
    val title: String,
    val publisher: String,
    val rating:Int,
    val description: String? = null,
    val ingredients: List<String>,
    // all the previous values are already following the naming convention
    // but the following are not so I need to rename them using @SerialName
    @SerialName("featured_image") val imageUrl: String,
    @SerialName("source_url") val sourceUrl: String,
    @SerialName("cooking_instructions") val instructions: String?=null,

    )