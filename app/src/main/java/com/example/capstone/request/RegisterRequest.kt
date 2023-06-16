package com.example.capstone.request

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("nama")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("kelamin")
    val gender: String,
    @SerializedName("usia")
    val age: Int,
    @SerializedName("berat")
    val weight: Int,
    @SerializedName("tinggi")
    val height: Int,
    @SerializedName("aktivitas")
    val aktivitas: String,
    @SerializedName("penyakit")
    val disease: String
)

