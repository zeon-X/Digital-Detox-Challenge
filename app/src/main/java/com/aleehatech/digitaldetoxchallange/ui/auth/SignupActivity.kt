package com.aleehatech.digitaldetoxchallange.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.aleehatech.digitaldetoxchallange.R
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var fullNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var errorIcon: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var goToLoginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize Views
        fullNameEditText = findViewById(R.id.editTextFullName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        repeatPasswordEditText = findViewById(R.id.editTextRepeatPassword)
        signUpButton = findViewById(R.id.signup_btn)
        errorIcon = findViewById(R.id.signup_error_icon)
        errorMessage = findViewById(R.id.signup_error_message)
        goToLoginText = findViewById(R.id.link_go_to_login)

        // Set initial error visibility to GONE
        errorIcon.visibility = ImageView.GONE
        errorMessage.visibility = TextView.GONE

        // Sign Up button click listener
        signUpButton.setOnClickListener {
            validateAndSignUp()
        }

        // Navigate to Login
        goToLoginText.setOnClickListener {
            finish() // Close this activity and go back to the login screen
        }
    }

    private fun validateAndSignUp() {
        val fullName = fullNameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val repeatPassword = repeatPasswordEditText.text.toString().trim()

        // Validate inputs
        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showError("All fields are required.")
            return
        }
        if (password != repeatPassword) {
            showError("Passwords do not match.")
            return
        }
        if (!isValidPassword(password)) {
            showError("Password must contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character, and must be 8 characters long.")
            return
        }

        // Save user to Firestore
        saveUserToFirestore(fullName, phone, email, password)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=]).{8,}\$")
        return passwordPattern.matches(password)
    }

    private fun showError(message: String) {
        errorIcon.visibility = ImageView.VISIBLE
        errorMessage.visibility = TextView.VISIBLE
        errorMessage.text = message


        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveUserToFirestore(fullName: String, phone: String, email: String, password: String) {
        // Reference to Firestore collection
        val userRef = firestore.collection("users").document(email)  // Using email as document ID

        // Check if the email already exists in Firestore
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Email already exists
                showError("Email is already registered.")

            } else {
                // Email is unique, proceed to save the user
                val user = hashMapOf(
                    "fullname" to fullName,
                    "phone" to phone,
                    "email" to email,
                    "password" to password // Store password securely in real apps (e.g., hashed)
                )

                // Save the user data using the email as the document ID
                userRef.set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                        clearFields()
                    }
                    .addOnFailureListener { e ->
                        showError("Error saving user: ${e.message}")
                    }
            }
        }
            .addOnFailureListener { e ->
                showError("Error checking email uniqueness: ${e.message}")
            }
    }




    private fun clearFields() {
        fullNameEditText.text.clear()
        phoneEditText.text.clear()
        emailEditText.text.clear()
        passwordEditText.text.clear()
        repeatPasswordEditText.text.clear()
        errorIcon.visibility = ImageView.GONE
        errorMessage.visibility = TextView.GONE
    }
}
