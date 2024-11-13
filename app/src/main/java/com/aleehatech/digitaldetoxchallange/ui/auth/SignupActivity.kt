package com.aleehatech.digitaldetoxchallange.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.aleehatech.digitaldetoxchallange.R
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var linkGoToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find UI elements
        emailEditText = findViewById(R.id.editTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextPassword)
        repeatPasswordEditText = findViewById(R.id.editTextRepeatPassword)
        signUpButton = findViewById(R.id.signup_btn)
        linkGoToLogin = findViewById(R.id.link_go_to_login)  // Initialize the TextView


        // Set up sign-up button click listener
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(password)) {
                Toast.makeText(
                    this,
                    "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character",
                    Toast.LENGTH_LONG
                ).show()
            } else if (password != repeatPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                signUpUser(email, password)
            }
        }


        // Set up the "Already have an account? Login here" click listener
        linkGoToLogin.setOnClickListener {
            // Navigate to SignInActivity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

    }



    // Password validation
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.find(password) != null
    }

    // Email validation (basic pattern)
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                    // Navigate to next screen or main activity here

                    // Create an Intent to navigate to the MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    // Optionally, clear the activity stack if you don't want the user to come back to the sign-up screen
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    // Start the MainActivity
                    startActivity(intent)

                    // Finish the current SignUpActivity so the user cannot go back to it
                    finish()
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
