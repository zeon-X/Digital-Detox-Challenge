package com.aleehatech.digitaldetoxchallange

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.aleehatech.digitaldetoxchallange.ui.BlockScreenActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FocusModeAccessibilityService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        // Perform any necessary initialization here
    }

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        this.serviceInfo = info
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Check for relevant events indicating app launch or switch
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                // Retrieve package name of the app currently in the foreground
                val packageName = event.packageName?.toString()
                if (packageName != null) {
                    onAppOpened(packageName)
                }
            }
        }
    }

    private fun onAppOpened(packageName: String) {
        // Here you can check if the app is in focus mode and handle it

        // Toast.makeText(applicationContext, "App Opened: $packageName", Toast.LENGTH_SHORT).show()
        // Log.d("FocusModeService", "App Opened: $packageName")

        // Example: Block app if it's in focus mode or other conditions
        // (Fetch focus mode times and current state from SharedPreferences or database)
        // if (isAppInFocusMode(packageName)) {
        //     // Block access or show a restriction message
        // }



        val sharedPref = getSharedPreferences("AppFocusModePrefs", Context.MODE_PRIVATE)

        val startTimeString = sharedPref.getString("$packageName-start", "00:00") ?: "00:00"
        val endTimeString = sharedPref.getString("$packageName-end", "00:00") ?: "00:00"

        // Parse the time strings into Date objects (these will default to today’s date)
        val startTimeCalendar = Calendar.getInstance().apply {
            time = timeFormatter.parse(startTimeString) ?: Date(0)
            set(Calendar.YEAR, 1970)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endTimeCalendar = Calendar.getInstance().apply {
            time = timeFormatter.parse(endTimeString) ?: Date(0)
            set(Calendar.YEAR, 1970)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
        }


        // Get start and end times in milliseconds since midnight
        val startTime = startTimeCalendar.timeInMillis
        val endTime = endTimeCalendar.timeInMillis
        // Log.d("FocusModeService", "StartTime:$startTime EndTime:$endTime")

        // Get the current time in milliseconds from midnight
        val calendar = Calendar.getInstance()
        val currentTimeInMillisFromMidnight = calendar.run {
            set(Calendar.YEAR, 1970)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            timeInMillis
        }

        // Log.d("FocusModeService", "Current Time:$currentTimeInMillisFromMidnight")


        // Check if current time is within the focus mode start and end times
        if (currentTimeInMillisFromMidnight in startTime..endTime) {
            Toast.makeText(applicationContext, "Focus Mode Active! Blocking App.", Toast.LENGTH_SHORT).show()
            // Log.d("FocusModeService", "Focus Mode Active! Blocking App.")
            // Additional logic to block app access goes here

            // Launch the block screen overlay to prevent app usage
            val intent = Intent(applicationContext, BlockScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }




    }



    override fun onInterrupt() {
        // Handle service interruption, if necessary
    }
}
