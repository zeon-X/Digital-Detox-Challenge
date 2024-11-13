package com.aleehatech.digitaldetoxchallange

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import android.content.Context
import android.content.SharedPreferences

class FocusModeAccessibilityService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        // Perform any necessary initialization here
    }



    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Logic to restrict access to apps based on the timeline (focus mode)
        val currentTime = System.currentTimeMillis()

        val sharedPref = getSharedPreferences("AppFocusModePrefs", Context.MODE_PRIVATE)

        // Replace "AppName" with the specific app name key
        val startTimeString = sharedPref.getString("AppName-start", "00:00") ?: "00:00"
        val endTimeString = sharedPref.getString("AppName-end", "00:00") ?: "00:00"


        // Example: If within focus mode time, block access to certain apps
        val startTime = 0L // Start time (this should be fetched from SharedPreferences or database)
        val endTime = 0L // End time (this should be fetched from SharedPreferences or database)

        if (currentTime >= startTime && currentTime <= endTime) {
            // Logic to block the app from being accessed
            // For example, if the event corresponds to an app in focus mode, prevent it
            Toast.makeText(applicationContext, "Focus Mode Active! Blocking App.", Toast.LENGTH_SHORT).show()
            // You can block access to apps here by preventing the event from being passed further
        }
    }

    override fun onInterrupt() {
        // Handle service interruption, if necessary
    }
}
