package com.aleehatech.digitaldetoxchallange.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.content.SharedPreferences
import com.aleehatech.digitaldetoxchallange.R

class FocusModeSettingsActivity : Activity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("AppFocusModePrefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_mode_settings)

        val startTimePicker = findViewById<TimePicker>(R.id.start_time_picker)
        val endTimePicker = findViewById<TimePicker>(R.id.end_time_picker)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val startTimeMillis = startTimePicker.hour * 60 * 60 * 1000 + startTimePicker.minute * 60 * 1000
            val endTimeMillis = endTimePicker.hour * 60 * 60 * 1000 + endTimePicker.minute * 60 * 1000

            // Save the start and end times in SharedPreferences
            with(sharedPreferences.edit()) {
                putLong("startTime", startTimeMillis.toLong())
                putLong("endTime", endTimeMillis.toLong())
                apply()
            }

            finish() // Close the settings activity
        }
    }
}
