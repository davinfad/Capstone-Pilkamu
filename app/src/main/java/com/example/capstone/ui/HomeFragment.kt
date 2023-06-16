package com.example.capstone.ui

import com.example.capstone.adapter.Food
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.FoodAdapter
import com.example.capstone.databinding.FragmentHomeBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class HomeFragment : Fragment(), FoodAdapter.OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Access views using binding
        binding.searchView
        // ... other view operations

        // Set up RecyclerView
        binding.rvFood.layoutManager = LinearLayoutManager(requireContext())
        val foodAdapter = FoodAdapter(emptyList())
        binding.rvFood.adapter = foodAdapter

        foodAdapter.setOnItemClickListener(this)

        // Load food data and update the adapter
        val foodList = loadFoodData()
        foodAdapter.setData(foodList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(view: View, food: Food) {
        // Handle the click event and navigate to the detail activity
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("food", food)
        startActivity(intent)
    }

    private fun loadFoodData(): List<Food> {
        val foodList = mutableListOf<Food>()

        try {
            val inputStream = requireContext().assets.open("Clustered_makanan.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Skip the header row if it exists
            reader.readLine()

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line?.split(",") // Assuming the CSV data is comma-separated

                // Parse the data from the CSV line
                val foodName = tokens?.get(0)
                val imageUrl = tokens?.get(8)
                val calories = tokens?.get(1)?.toIntOrNull()
                val protein = tokens?.get(2)?.toDoubleOrNull()
                val fat = tokens?.get(3)?.toDoubleOrNull()
                val saturatedFat = tokens?.get(4)?.toDoubleOrNull()
                val fiber = tokens?.get(5)?.toDoubleOrNull()
                val carbs = tokens?.get(6)?.toDoubleOrNull()
                val kelompok = tokens?.get(7)

                // Create a com.example.capstone.adapter.Food object and add it to the list
                if (foodName != null && imageUrl != null && calories != null &&
                    protein != null && fat != null && saturatedFat != null &&
                    fiber != null && carbs != null && kelompok != null) {
                    val food = Food(foodName,calories, protein,fat,saturatedFat,fiber,carbs,kelompok,imageUrl)
                    foodList.add(food)
                }
            }

            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return foodList
    }



}

