package com.example.capstone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportActionBar?.hide()
                    switchFragment(HomeFragment())
                    true
                }
                R.id.favourite -> {
                    supportActionBar?.hide()
                    switchFragment(FavoriteFragment())
                    true
                }
                R.id.profile -> {
                    supportActionBar?.show()
                    switchFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Set the default fragment when the activity starts
        switchFragment(HomeFragment())

    }

    private fun switchFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Perform log out operation
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        // Do nothing if the user is in the MainActivity
        // This prevents navigating back to the login screen or any previous screens
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
    }

    private fun logout() {
        // Clear the saved token from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("AuthToken")
        editor.apply()

        // Navigate the user back to the LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close MainActivity to prevent going back to it
    }


}