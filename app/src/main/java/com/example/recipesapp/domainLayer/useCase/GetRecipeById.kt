package com.example.recipesapp.domainLayer.useCase

import com.example.recipesapp.domainLayer.RecipeModel
import com.example.recipesapp.domainLayer.RecipeRepository

class GetRecipeById(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: Int): RecipeModel {
        return repository.getRecipeByID(id)
    }
}