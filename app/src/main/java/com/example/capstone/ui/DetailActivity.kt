package com.example.capstone.ui

import com.example.capstone.adapter.Food
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.capstone.databinding.DetailActivityBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: DetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Retrieve the com.example.capstone.adapter.Food object from the intent
        val food = intent.getParcelableExtra<Food>("food")

        // Populate the views with the data
        food?.let {
            binding.foodNameTextView.text = it.foodName
            binding.caloriesTextView.text = "Calories: ${it.calories}"
            binding.proteinTextView.text = "Protein: ${it.protein}"
            binding.fatTextView.text = "Fat: ${it.fat}"
            binding.satFatTextView.text = "Saturated Fat: ${it.saturatedFat}"
            binding.fiberTextView.text = "Fiber: ${it.fiber}"
            binding.carbsTextView.text = "Carbs: ${it.carbs}"
            // Load the food image using Glide or any other image loading library
            Glide.with(this)
                .load(it.imageUrl)
                .into(binding.foodImageView)
        }
    }
}


