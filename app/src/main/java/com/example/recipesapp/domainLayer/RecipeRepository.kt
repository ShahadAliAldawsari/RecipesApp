package com.example.recipesapp.domainLayer

interface RecipeRepository{
    suspend fun searchByRecipeName(recipeName:String, page:Int):List<RecipeModel>
    suspend fun getRecipeByID(id:Int):RecipeModel
}