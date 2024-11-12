package com.aleehatech.digitaldetoxchallange.service

import android.accessibilityservice.AccessibilityService
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class FocusModeAccessibilityService : AccessibilityService() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        sharedPreferences = getSharedPreferences("AppSuspensionPrefs", MODE_PRIVATE)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null && isAppRestricted(packageName)) {
                // Retrieve startTime and endTime from SharedPreferences
                val startTime = sharedPreferences.getString("${packageName}-start", null)
                val endTime = sharedPreferences.getString("${packageName}-end", null)

                if (startTime != null && endTime != null && isWithinFocusTime(startTime, endTime)) {
                    // Navigate the user back to the home screen if the current time is within the focus time range
                    performGlobalAction(GLOBAL_ACTION_HOME)
                    Toast.makeText(this, "Focus mode is on. App access is restricted.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isWithinFocusTime(startTime: String, endTime: String): Boolean {
        val currentTime = Calendar.getInstance().time

        val startCalendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(startTime)!!
            if (time.after(currentTime)) add(Calendar.DATE, -1) // For cases where end time is after midnight
        }

        val endCalendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(endTime)!!
            if (time.before(currentTime)) add(Calendar.DATE, 1)
        }

        return currentTime.after(startCalendar.time) && currentTime.before(endCalendar.time)
    }

    private fun isAppRestricted(packageName: String): Boolean {
        // Define logic to check if the app is in the restricted list
        return true // Replace with actual restricted app logic
    }

    override fun onInterrupt() {}
}
