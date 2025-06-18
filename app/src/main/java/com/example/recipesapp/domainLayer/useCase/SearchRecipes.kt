package com.example.recipesapp.domainLayer.useCase

import com.example.recipesapp.domainLayer.RecipeModel
import com.example.recipesapp.domainLayer.RecipeRepository

/**
 * Searches for recipes by name with optional pagination.
 * @param query Recipe name or keyword "the name of the recipe".
 * @param page Page number for pagination. Defaults to 1.
 */

class SearchRecipes(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1): List<RecipeModel> {
        return repository.searchByRecipeName(query, page)
    }
}