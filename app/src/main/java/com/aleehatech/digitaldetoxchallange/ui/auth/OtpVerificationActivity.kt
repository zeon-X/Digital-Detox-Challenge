package com.aleehatech.digitaldetoxchallange.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.aleehatech.digitaldetoxchallange.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var otpEditText: EditText
    private lateinit var verifyButton: Button
    private var phoneNumber: String? = null
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_verification_screen)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Get phone number from intent
        phoneNumber = intent.getStringExtra("phone")

        // Find UI elements
        otpEditText = findViewById(R.id.otp_input)
        verifyButton = findViewById(R.id.verify_button)

        // Start the phone number verification process
        phoneNumber?.let {
            sendOTP(it)
        } ?: run {
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set up button click listener
        verifyButton.setOnClickListener {
            val enteredOTP = otpEditText.text.toString()
            if (enteredOTP.isNotEmpty()) {
                verificationId?.let { id ->
                    verifyOTP(id, enteredOTP)
                } ?: run {
                    Toast.makeText(this, "Verification ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOTP(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@OtpVerificationActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PhoneAuth", "Verification failed", e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save the verification ID to verify later
                    this@OtpVerificationActivity.verificationId = verificationId
                    Toast.makeText(this@OtpVerificationActivity, "OTP sent to $phoneNumber", Toast.LENGTH_SHORT).show()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOTP(verificationId: String, otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "OTP verified successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to the main activity or next screen
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid OTP: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
