package com.example.recipesapp.presentationLayer

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.domainLayer.RecipeModel
import com.example.recipesapp.domainLayer.useCase.GetRecipeById
import com.example.recipesapp.domainLayer.useCase.SearchRecipes
import com.example.recipesapp.utils.UiState
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val searchRecipesUseCase: SearchRecipes,
    private val getRecipeByIdUseCase: GetRecipeById
) : ViewModel() {

    private val _recipeListState = mutableStateOf<UiState<List<RecipeModel>>>(UiState.Loading)
    val recipeListState: State<UiState<List<RecipeModel>>> = _recipeListState

    private val _recipeDetailState = mutableStateOf<UiState<RecipeModel>>(UiState.Loading)
    val recipeDetailState: State<UiState<RecipeModel>> = _recipeDetailState

    private val _query = mutableStateOf("")
    val query: State<String> = _query

    private val _isLoadingMore = mutableStateOf(false)
    val isLoadingMore: State<Boolean> = _isLoadingMore

    private var currentQuery: String = ""
    private var currentPage = 1
    private var isLastPage = false

    private var allRecipes = mutableListOf<RecipeModel>()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun loadNextPage() {
        if (isLoadingMore.value || currentQuery.isEmpty() || isLastPage) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            currentPage++
            try {
                val moreRecipes = searchRecipesUseCase(currentQuery, currentPage)
                allRecipes.addAll(moreRecipes)
                _recipeListState.value = UiState.Success(allRecipes.toList())
                isLastPage = moreRecipes.isEmpty()
            } catch (e: Exception) {
                Log.d("loadNextPage() in RecipeViewModel","Something went wrong!")
                _recipeListState.value = UiState.Error(e.message ?: "Failed to load more recipes")

            }
            _isLoadingMore.value = false
        }
    }

    fun search(recipeName: String) {
        viewModelScope.launch {
            _recipeListState.value = UiState.Loading
            currentQuery = recipeName
            currentPage = 1
            isLastPage = false
            allRecipes.clear()
            try {
                val recipes = searchRecipesUseCase(recipeName, currentPage)
                allRecipes.addAll(recipes)
                _recipeListState.value = UiState.Success(recipes)
                isLastPage = recipes.isEmpty()
            } catch (e: Exception) {
                _recipeListState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getRecipeById(id: Int) {
        viewModelScope.launch {
            _recipeDetailState.value = UiState.Loading
            try {
                val recipe = getRecipeByIdUseCase(id)
                _recipeDetailState.value = UiState.Success(recipe)
            } catch (e: Exception) {
                _recipeDetailState.value = UiState.Error(e.message ?: "Error loading recipe")
            }
        }
    }
}