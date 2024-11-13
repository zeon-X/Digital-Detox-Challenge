package com.aleehatech.digitaldetoxchallange

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class FocusModeAccessibilityService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        // Perform any necessary initialization here

        //Log.d("FocusModeService", "FocusModeAccessibilityService Opened")
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        this.serviceInfo = info
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.d("FocusModeService", "got an event.")
        if (event == null) return

        // Check for relevant events indicating app launch or switch
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {

                // Retrieve package name of the app currently in the foreground
                // Log.d("appInfo", event.toString())
                val packageName = event.packageName?.toString()
                if (packageName != null) {
                    onAppOpened(packageName)
                }
            }
        }
    }

    private fun onAppOpened(packageName: String) {
        // Here you can check if the app is in focus mode and handle it

        Toast.makeText(this, "App Opened: $packageName", Toast.LENGTH_SHORT).show()
        // Log.d("FocusModeService", "App Opened: $packageName")

        // Example: Block app if it's in focus mode or other conditions
        // (Fetch focus mode times and current state from SharedPreferences or database)
        // if (isAppInFocusMode(packageName)) {
        //     // Block access or show a restriction message
        // }

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
