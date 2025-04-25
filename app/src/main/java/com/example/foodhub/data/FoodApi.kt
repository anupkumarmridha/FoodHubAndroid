package com.example.foodhub.data

import com.example.foodhub.data.models.AuthResponse
import com.example.foodhub.data.models.LoginRequest
import com.example.foodhub.data.models.OAuthRequest
import com.example.foodhub.data.models.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodApi {
    @GET("food")
    suspend fun getFood(): List<String>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): AuthResponse

}