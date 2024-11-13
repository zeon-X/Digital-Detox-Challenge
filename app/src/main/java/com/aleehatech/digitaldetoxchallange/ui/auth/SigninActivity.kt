package com.aleehatech.digitaldetoxchallange.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.R
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpLink: TextView // SignUp Link TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                    // Navigate to MainActivity after successful sign-in
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Sign in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
