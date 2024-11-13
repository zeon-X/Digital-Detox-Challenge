package com.aleehatech.digitaldetoxchallange.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import com.aleehatech.digitaldetoxchallange.MainActivity
import com.aleehatech.digitaldetoxchallange.R

class BlockScreenActivity : Activity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Prevent user interaction with the UI
        setContentView(R.layout.activity_block_screen)

        val textView = findViewById<TextView>(R.id.block_message)
        textView.text = "Focus Mode is on. You cannot use this app now."

        // Handle back button click
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            // Start the MainActivity (home page)
            val intent = Intent(this, MainActivity::class.java)  // Change MainActivity to your home activity
            startActivity(intent)

            // Optionally, finish the BlockScreenActivity to prevent going back to it
            finish()
        }
    }
}
