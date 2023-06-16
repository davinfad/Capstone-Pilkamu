package com.example.capstone.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.dataStore.UserPreferences
import com.example.capstone.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        


        // Initialize UserPreferences
        userPreferences = UserPreferences(requireContext())

        // Retrieve the saved response
        val savedResponse = userPreferences.getResponseLogin()

        // Populate the profile fragment views with the saved response
        savedResponse?.let {
            binding.tvUsername.text = it.username
            binding.tvNama.text = it.nama
            binding.tvEmail.text = it.email
            binding.tvKelamin.text = it.kelamin
            binding.tvUsia.text = it.usia.toString()
            binding.tvBerat.text = it.berat.toString() + " Kg"
            binding.tvTinggi.text = it.tinggi.toString() + " Cm"
            binding.tvAktivitas.text = it.aktivitas
            binding.tvPenyakit.text = it.penyakit
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ...
}