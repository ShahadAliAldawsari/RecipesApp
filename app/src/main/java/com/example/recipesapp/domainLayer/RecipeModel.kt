package com.example.recipesapp.domainLayer

data class RecipeModel(
    val id:Int,
    val title: String,
    val imageUrl: String,
    val publisher: String,
    val ingredients: List<String>,
    val instructions: String? = null,
    val rating:Int,
    val description: String? = null,
    val sourceUrl: String,
)