package com.example.capstone.api

import com.example.capstone.request.LoginRequest
import com.example.capstone.request.RegisterRequest
import com.example.capstone.response.ResponseLogin
import com.example.capstone.response.ResponseRegister
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    @Headers("Content-Type: application/json")
    fun login(@Body request: LoginRequest): Call<ResponseLogin>

    @POST("register")
    @Headers("Content-Type: application/json")
    fun register(
        @Part request: RegisterRequest
    ): Call<ResponseRegister>



}
