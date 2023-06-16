package com.example.capstone.response

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("message")
    val message: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("kelamin")
    val kelamin: String,
    @SerializedName("usia")
    val usia: Int,
    @SerializedName("berat")
    val berat: Int,
    @SerializedName("tinggi")
    val tinggi: Int,
    @SerializedName("aktivitas")
    val aktivitas: String,
    @SerializedName("penyakit")
    val penyakit: String,
    @SerializedName("token")
    val token: String
)



