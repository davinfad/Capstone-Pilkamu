package com.example.capstone.dataStore

import android.content.Context
import android.content.SharedPreferences
import com.example.capstone.response.ResponseLogin
import com.google.gson.Gson

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val AUTH_TOKEN = "AuthToken"
    }

    fun saveAuthToken(token: String?) {
        sharedPreferences.edit().putString(AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove(AUTH_TOKEN).apply()
    }

    fun isLoggedIn(): Boolean {
        return getAuthToken() != null
    }

    fun saveResponseLogin(responseLogin: ResponseLogin) {
        val gson = Gson()
        val responseJson = gson.toJson(responseLogin)
        sharedPreferences.edit().putString("ResponseLogin", responseJson).apply()
    }

    fun getResponseLogin(): ResponseLogin? {
        val responseJson = sharedPreferences.getString("ResponseLogin", null)
        return if (responseJson != null) {
            val gson = Gson()
            gson.fromJson(responseJson, ResponseLogin::class.java)
        } else {
            null
        }
    }
}

