package com.aleehatech.digitaldetoxchallange.ui

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.aleehatech.digitaldetoxchallange.R

class BlockScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Prevent user interaction with the UI
        setContentView(R.layout.activity_block_screen)

        val textView = findViewById<TextView>(R.id.block_message)
        textView.text = "Focus Mode is ON. You cannot use this app now."
    }
}
