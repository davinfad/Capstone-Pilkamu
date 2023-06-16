package com.example.capstone.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check the login state
        val isLoggedIn = isLoggedIn()

        // Determine which activity to start
        val targetActivity = if (isLoggedIn) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        // Start the appropriate activity
        val intent = Intent(this, targetActivity)
        startActivity(intent)
        finish() // Optional: close the SplashActivity to prevent going back to it
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AuthToken", null)
        Log.d("MainActivity", "Token: $token")
        return token != null
    }
}
