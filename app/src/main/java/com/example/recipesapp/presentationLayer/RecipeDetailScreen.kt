package com.example.recipesapp.presentationLayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.recipesapp.R
import com.example.recipesapp.Screens
import com.example.recipesapp.domainLayer.RecipeModel
import com.example.recipesapp.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavHostController,
    recipeID: String?,
    factory: RecipeViewModelFactory,
) {
    val viewModel: RecipeViewModel = viewModel(factory = factory)
    val recipeState by viewModel.recipeDetailState
    val detailsScreenData = Screens.Details

    //currently it's UI only (I did not store liked recipes)
    val isLiked = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(recipeID) {
        recipeID?.toIntOrNull()?.let { viewModel.getRecipeById(it) }
    }

    Scaffold(
        modifier = Modifier.padding(integerResource(R.integer.mediumSpace).dp),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        if (detailsScreenData.navigationIconID != null) {
                            Icon(
                                imageVector = detailsScreenData.navigationIconID,
                                contentDescription = "back arw icon"
                            )
                        }
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(detailsScreenData.titleID),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isLiked.value = !isLiked.value }) {
                        Icon(
                            painterResource(id = detailsScreenData.actionIconID),
                            contentDescription = "heart icon",
                            tint = if (isLiked.value) Color.Red else Color.Gray, // usually I would use Material Color Schema It is better and supports dark/light mode
                            modifier = Modifier.size(integerResource(R.integer.heartIconSize).dp)
                        )
                    }
                }
            )
        }
    )
    { innerPadding ->
        when (recipeState) {
            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text((recipeState as UiState.Error).message)
                }
            }

            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val recipe = (recipeState as UiState.Success<RecipeModel>).data
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = "Recipe image URL",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(integerResource(R.integer.recipeImageCorners).dp))
                            .background(Color.LightGray), // I'm using it just as a placeholder
                    )
                    Spacer(Modifier.height(integerResource(R.integer.extraLargeSpace).dp))
                    // Recipe full title
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    // for Ingredients section
                    TitledList(
                        title = "Ingredients", recipe.ingredients
                    )

                    // for "More information" section (publisher and sourceUrl)
                    TitledList(
                        title = "More information",
                        listOf(
                            "Publisher: ${recipe.publisher}",
                            "Source: ${recipe.sourceUrl}"
                        )
                    )
                }
            }
        }
    }
}

//I'm using this function to reduce boiler code
@Composable
fun TitledList(title: String, list: List<String>) {
    Spacer(Modifier.height(integerResource(R.integer.extraLargeSpace).dp))
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium,

        )
    Spacer(Modifier.height(integerResource(R.integer.smallerSpace).dp))
    list.forEach { listItem ->
        Text(
            "- $listItem",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(integerResource(R.integer.extraSmallSpace).dp))
    }
}