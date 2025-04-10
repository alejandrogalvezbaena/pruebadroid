package com.example.pruebadroid.data.network

import com.example.pruebadroid.data.model.CharacterListResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApiService {
    @GET("character/")
    suspend fun getAllCharacters(@Query("page") page: Int): Response<CharacterListResult>
}