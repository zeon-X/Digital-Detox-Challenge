package com.aleehatech.digitaldetoxchallange.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.aleehatech.digitaldetoxchallange.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpLink: TextView // SignUp Link TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_screen)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if the user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
            return
        }

        // Find UI elements
        emailEditText = findViewById(R.id.editTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextPassword)
        signInButton = findViewById(R.id.signin_btn)
        signUpLink = findViewById(R.id.link_go_to_signup)  // Initialize SignUp link

        // Set up sign-in button click listener
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                signInUser(email, password)
            }
        }

        // Set up the Sign Up link click listener
        signUpLink.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInUser(email: String, password: String) {
        // Query Firestore to check if the user exists and validate credentials
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Retrieve user data
                    val storedPassword = document.getString("password")
                    val phoneNumber = document.getString("phone")

                    // Check if the provided password matches the stored password
                    if (storedPassword == password) {
                        if (!phoneNumber.isNullOrEmpty()) {
                            // Proceed with phone authentication
                            sendOTP(phoneNumber)
                        } else {
                            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No user found with this email", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendOTP(phoneNumber: String) {
        // Use Firebase Phone Auth to authenticate with the phone number
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra("phone", phoneNumber)
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
