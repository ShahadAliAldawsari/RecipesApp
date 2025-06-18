package com.example.recipesapp.dataLayer

import com.example.recipesapp.dataLayer.remote.RecipeApi
import com.example.recipesapp.dataLayer.remote.dto.RecipeDto
import com.example.recipesapp.domainLayer.RecipeModel
import com.example.recipesapp.domainLayer.RecipeRepository

class RecipeRepositoryImpl (private val api: RecipeApi): RecipeRepository{
    override suspend fun searchByRecipeName(recipeName: String, page: Int): List<RecipeModel> {
        return api.searchRecipeName(recipeName,page).result.map { it.toDomainModel() }
    }
    override suspend fun getRecipeByID(id: Int): RecipeModel {
        return api.getRecipeByID(id).toDomainModel()
    }
}

fun RecipeDto.toDomainModel(): RecipeModel = RecipeModel(
    id = pk,
    title= title,
    imageUrl= imageUrl,
    publisher= publisher,
    ingredients=ingredients,
    instructions = instructions,
    rating = rating,
    description= description,
    sourceUrl= sourceUrl
)