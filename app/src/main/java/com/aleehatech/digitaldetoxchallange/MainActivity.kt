package com.aleehatech.digitaldetoxchallange

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aleehatech.digitaldetoxchallange.utils.Utils.promptUserToEnableAccessibilityService
import com.aleehatech.digitaldetoxchallange.utils.Utils.requestUsageAccess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prompt user to enable the Focus Mode Accessibility Service
        promptUserToEnableAccessibilityService(this, FocusModeAccessibilityService::class.java)
        requestUsageAccess(this)
    }

    override fun onResume() {
        super.onResume()
        promptUserToEnableAccessibilityService(this, FocusModeAccessibilityService::class.java)
        requestUsageAccess(this)
    }

}
