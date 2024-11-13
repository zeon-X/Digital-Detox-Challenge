package com.aleehatech.digitaldetoxchallange

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.utils.Utils.promptUserToEnableAccessibilityService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prompt user to enable the Focus Mode Accessibility Service
        promptUserToEnableAccessibilityService(this, FocusModeAccessibilityService::class.java)
    }
}
