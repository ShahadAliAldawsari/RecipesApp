package com.example.recipesapp.presentationLayer

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.recipesapp.R
import com.example.recipesapp.Screens
import com.example.recipesapp.domainLayer.RecipeModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import com.example.recipesapp.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, factory: RecipeViewModelFactory) {

    val recipeVM: RecipeViewModel = viewModel(factory=factory)
    val homeScreenData = Screens.Home
    val recipeListState by recipeVM.recipeListState
    val query by recipeVM.query
    var isActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (query.isBlank()) {
            recipeVM.search("") // to show a default set of recipes on launch
        }
    }

    Scaffold(
        modifier = Modifier.padding(integerResource(R.integer.mediumSpace).dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(homeScreenData.titleID),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Icon(
                        painterResource(id = homeScreenData.actionIconID),
                        contentDescription = "filter icon",
                        tint = Color.Gray, // usually I would use Material Color Schema It is better and supports dark/light mode
                        modifier = Modifier.size(integerResource(R.integer.filterIconSize).dp)

                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { recipeVM.onQueryChange(it) },
                onSearch = {recipeVM.search(query)},
                active = isActive,
                onActiveChange = {isActive=it},
                placeholder = {
                    Text(
                        stringResource(R.string.search_meal_name),
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        recipeVM.search(query)
                        isActive=false
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "search icon")
                    }
                },
                shape = RoundedCornerShape(integerResource(R.integer.searchBarCorners).dp),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Spacer(Modifier.height(integerResource(R.integer.mediumSpace).dp))
}
                when (recipeListState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    is UiState.Error -> {
                        Text(
                            text = (recipeListState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    is UiState.Success -> {
                        val recipes = (recipeListState as UiState.Success<List<RecipeModel>>).data
                        if (recipes.isEmpty()) {
                            Text(
                                text = stringResource(R.string.noRecipes),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            RecipeList(
                                recipesList= recipeVM.recipeListState,
                                navController = navController,
                                isLoadingMore = recipeVM.isLoadingMore.value,
                                onLoadMore = {recipeVM.loadNextPage()})
                        }
                    }
                }

        }
    }
}

// I've separated the lazy list of recipe in another composable,
// so it dose not recomposes the whole screen while it's not needed
@Composable
fun RecipeList(
    navController: NavHostController,
    recipesList: State<UiState<List<RecipeModel>>>,
    isLoadingMore: Boolean,
    onLoadMore:()-> Unit
    ) {
    val list = (recipesList.value as? UiState.Success)?.data ?: emptyList()

    LazyColumn(
        modifier = Modifier.padding(integerResource(R.integer.smallSpace).dp),

        ) {
        itemsIndexed(list.chunked(2)) {index, pareOfItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = integerResource(R.integer.smallerSpace).dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                pareOfItems.forEach{recipe->
                    RecipeItem(recipe = recipe, modifier = Modifier.weight(1f), navController = navController)
                }
            }

            if(list.size >= PAGE_SIZE && index>=list.size/2-1){ // PAGE_SIZE = 30 by defult
                // if true it means the user is near to the end of the list and we need to load more
                LaunchedEffect(index) {
                    onLoadMore()
                }
            }
        }
        item {
            if (isLoadingMore) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = integerResource(R.integer.mediumSpace).dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe:RecipeModel, modifier: Modifier, navController: NavHostController) {
    OutlinedCard(
        modifier = modifier
            .height(integerResource(R.integer.itemHeight).dp)
            .clickable {
                navController.navigate(Screens.Details.createRoute(recipe.id.toString()))
            },
    ) {
        Column(modifier = Modifier.padding(integerResource(R.integer.itemPadding).dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(integerResource(R.integer.recipeImageBoxSize).dp)
                    .clip(RoundedCornerShape(integerResource(R.integer.recipeImageCorners).dp))
                    .background(Color.LightGray), // placeholder
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "Recipe image URL",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(Modifier.height(integerResource(R.integer.smallerSpace).dp))
            Text(
                text=recipe.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(integerResource(R.integer.smallerSpace).dp))
            Text(
                text = recipe.description?: stringResource(R.string.noDescription),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    Spacer(Modifier.width(integerResource(R.integer.smallSpace).dp))
}