package com.example.capstone.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.capstone.R
import com.example.capstone.api.ApiConfig
import com.example.capstone.databinding.RegisterActivityBinding
import com.example.capstone.request.RegisterRequest
import com.example.capstone.response.ResponseRegister
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*



class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var weightEditText : EditText
    private lateinit var heightEditText : EditText
    private lateinit var  healthConditionChipGroup: ChipGroup
    private var selectedChip: Chip? = null
    private val calendar = Calendar.getInstance()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        usernameEditText = findViewById(R.id.usernameEditText)
        nameEditText = findViewById(R.id.editTextName)
        passwordEditText = findViewById(R.id.passwordEditText)
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText)
        emailEditText = findViewById(R.id.emailEditText)
        weightEditText = findViewById(R.id.weightEditText)
        heightEditText = findViewById(R.id.heightEditText)
        healthConditionChipGroup = findViewById(R.id.healthConditionsChipGroup)

        //password visibility
        setDrawableClickListenerForPasswordVisibility(passwordEditText)

        //choose date
        dateOfBirthEditText.setOnClickListener {
            showDatePickerDialog()
        }

        //spinner gender
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val genderSpinner: Spinner = findViewById(R.id.genderSpinner)
        genderSpinner.adapter = genderAdapter

        //spinner activity
        val activityLevels = resources.getStringArray(R.array.activity_levels)
        val activityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityLevels)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val activityLevelSpinner: Spinner = findViewById(R.id.activityLevelSpinner)
        activityLevelSpinner.adapter = activityAdapter

        //var selectedChipValue: String? = null
        //chip
        for (i in 0 until healthConditionChipGroup.childCount) {
            val chip = healthConditionChipGroup.getChildAt(i) as Chip
            chip.setOnClickListener {
                selectedChip = chip
            }
        }

        //to login
        val loginLinkTextView: TextView = findViewById(R.id.loginLink)
        loginLinkTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            // Perform registration logic here
            val date = dateOfBirthEditText.text.toString()

            val username = usernameEditText.text.toString()
            val nama = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val kelamin = genderSpinner.selectedItem.toString()
            val kelaminValue = when(kelamin){
                "Male" -> "Laki-Laki"
                "Female" -> "Perempuan"
                else -> ""
            }
            val usia = calculateAge(date).toString().toInt()
            val berat = weightEditText.text.toString().toInt()
            val tinggi = heightEditText.text.toString().toInt()
            val aktivitas = activityLevelSpinner.selectedItem.toString()
            val aktivitasValue = when (aktivitas) {
                "Low" -> "Rendah"
                "Moderate" -> "Sedang"
                "High" -> "Berat"
                else -> ""
            }

            // Retrieve the selected chip value
            val selectedChipValue = selectedChip?.text?.toString()


            if (username.isNotEmpty() && nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
                kelamin.isNotEmpty() && usia > 0 && berat > 0 && tinggi > 0 && aktivitas.isNotEmpty()) {
                if (selectedChipValue != null) {
                    registerUser(username,nama,email,password, kelaminValue,usia,berat,tinggi,aktivitasValue,selectedChipValue)
                } else{
                    Toast.makeText(this, "Please select your health condition", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            }

        }

    }


    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selected date in the EditText
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateOfBirthEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Set a maximum date (optional)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun getSelectedChipValue(): String? {
        val selectedChipId = healthConditionChipGroup.checkedChipId
        val selectedChip: Chip? = healthConditionChipGroup.findViewById(selectedChipId)

        return selectedChip?.text?.toString()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setDrawableClickListenerForPasswordVisibility(editText: EditText) {
        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText.compoundDrawables[2]

                if (drawableEnd != null && event.rawX >= (editText.right - drawableEnd.bounds.width())) {
                    // Toggle password visibility
                    val isVisible = editText.transformationMethod == PasswordTransformationMethod.getInstance()

                    if (isVisible) {
                        editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(editText.context,
                                R.drawable.ic_visibility_off
                            ),
                            null
                        )
                    } else {
                        editText.transformationMethod = PasswordTransformationMethod.getInstance()
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(editText.context, R.drawable.ic_visibility_on),
                            null
                        )
                    }

                    // Move the cursor to the end of the text
                    editText.setSelection(editText.text.length)

                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun calculateAge(birthDate: String): Int? {
        if (birthDate.isEmpty()) {
            return 123
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()

        return try {
            val birthDateObj = dateFormat.parse(birthDate)
            birth.time = birthDateObj

            var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
            null
        }
    }

    private fun registerUser(
        username: String,
        name: String,
        email: String,
        password: String,
        gender: String,
        age: Int,
        weight: Int,
        height: Int,
        activity:String,
        disease: String
    ) {
        val registerRequest = RegisterRequest(
            username = username,
            name = name,
            email = email,
            password = password,
            gender = gender,
            age = age,
            weight = weight,
            height = height,
            aktivitas = activity,
            disease = disease
        )
        val apiService = ApiConfig.getApiService()
        apiService.register(registerRequest).enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if(registerResponse != null){
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@RegisterActivity,"${registerResponse.message}",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.toString()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: JSONException){
                        "An error occurred"
                    }
                    Toast.makeText(this@RegisterActivity,"$errorMessage",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}