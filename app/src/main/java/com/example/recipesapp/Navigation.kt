package com.example.recipesapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipesapp.dataLayer.RecipeRepositoryImpl
import com.example.recipesapp.dataLayer.remote.provideRecipeApi
import com.example.recipesapp.domainLayer.useCase.GetRecipeById
import com.example.recipesapp.domainLayer.useCase.SearchRecipes
import com.example.recipesapp.presentationLayer.HomeScreen
import com.example.recipesapp.presentationLayer.RecipeDetailScreen
import com.example.recipesapp.presentationLayer.RecipeViewModelFactory

sealed class Screens(
    val titleID: Int,
    val route: String,
    val actionIconID: Int,
    val navigationIconID: ImageVector? = null,
) {
    data object Home : Screens(titleID = R.string.meals_recipes, route = "home", actionIconID = R.drawable.filter)
    data object Details : Screens(titleID = R.string.recipe_detail, route = "detail/{recipeID}", actionIconID = R.drawable.heart, navigationIconID = Icons.Default.ArrowBack){

        /*
        I'm using the following two lines to avoid hard-coding
         to avoid writing "detail/{recipeID}" and "recipeID"
         manually multiple times in different places.
        */
        const val ARG_RECIPE_ID = "recipeID" // when I need it just as a key not as a value
        fun createRoute(recipeID: String): String = "detail/$recipeID" // when I need and an actual ID value like 1234
    }
}

@Composable
fun AppNavGraph(){
    val navController= rememberNavController()
    val api = remember { provideRecipeApi() }

    val repository = remember { RecipeRepositoryImpl(api) }

    val searchRecipesUseCase = remember { SearchRecipes(repository) }
    val getRecipeByIdUseCase = remember { GetRecipeById(repository) }

    val factory = remember { RecipeViewModelFactory(searchRecipesUseCase, getRecipeByIdUseCase) }

    NavHost(
        navController=navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {HomeScreen(navController,factory)}
        composable(
            route = Screens.Details.route,
            arguments = listOf(navArgument(Screens.Details.ARG_RECIPE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeID = backStackEntry.arguments?.getString(Screens.Details.ARG_RECIPE_ID)
            RecipeDetailScreen(navController, recipeID, factory)
        }
    }
}