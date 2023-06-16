package com.example.capstone.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.capstone.R
import com.example.capstone.api.ApiConfig
import com.example.capstone.dataStore.UserPreferences
import com.example.capstone.request.LoginRequest
import com.example.capstone.databinding.LoginActivityBinding
import com.example.capstone.response.ResponseLogin
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        // Initialize views
        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)

        userPreferences = UserPreferences(this)

        val registerLinkTextView: TextView = findViewById(R.id.registerLink)
        registerLinkTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        setDrawableClickListenerForPasswordVisibility(passwordEditText)


        // Set click listener for the login button
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform login authentication
            if (username == "" || password == ""
            ) {
                Toast.makeText(applicationContext, "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(username, password)
            }
        }
    }

    private fun performLogin(username: String, password: String) {
        val apiService = ApiConfig.getApiService()
        val request = LoginRequest(username, password)
        apiService.login(request).enqueue(object : Callback<ResponseLogin> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<ResponseLogin>,
                response: Response<ResponseLogin>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val token = responseBody.token
                        userPreferences.saveAuthToken(token)
                        userPreferences.saveResponseLogin(responseBody)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity,"${responseBody.message}",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: JSONException){
                        "An error occurred"
                    }
                    Toast.makeText(this@LoginActivity, "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
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

}