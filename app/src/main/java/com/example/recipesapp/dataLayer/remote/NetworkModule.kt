
package com.example.recipesapp.dataLayer.remote

import com.example.recipesapp.utils.NetworkConstants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


fun provideRecipeApi(): RecipeApi {
    val contentType = "application/json".toMediaType()

    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor(NetworkConstants.API_KEY))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .client(client)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType)
        )
        .build()

    return retrofit.create(RecipeApi::class.java)
}