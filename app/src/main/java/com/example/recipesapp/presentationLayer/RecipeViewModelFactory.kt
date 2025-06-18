package com.example.recipesapp.presentationLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.domainLayer.useCase.GetRecipeById
import com.example.recipesapp.domainLayer.useCase.SearchRecipes

class RecipeViewModelFactory(
    private val searchRecipesUseCase: SearchRecipes,
    private val getRecipeByIdUseCase: GetRecipeById
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(searchRecipesUseCase, getRecipeByIdUseCase) as T
    }
}