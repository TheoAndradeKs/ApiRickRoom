package com.theoandrade.propriapokedex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class RickApiResponse(
    val info: RickApiInfo,
    val results: List<RickApiCharacter>
)

data class RickApiInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class RickApiCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
)

interface OpenRickService {

    @GET("character")
    suspend fun getCharacters(): RickApiResponse
}

object RetrofitClient {

    private const val BASE_URL =
        "https://rickandmortyapi.com/api/"

    val api: OpenRickService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(OpenRickService::class.java)
    }
}