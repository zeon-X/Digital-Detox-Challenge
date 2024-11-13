package com.aleehatech.digitaldetoxchallange.ui.home

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aleehatech.digitaldetoxchallange.R
import java.text.SimpleDateFormat
import java.util.*

class AppAdapter(private val context: Context, private val appList: List<AppInfo>) :
    RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppFocusModePrefs", Context.MODE_PRIVATE)

    // Date formatter to convert between 12-hour and 24-hour format
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    inner class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val infoButton: ImageButton = view.findViewById(R.id.app_focus_mode_info_btn)
        val focusModeLayout: LinearLayout = view.findViewById(R.id.focus_mode_info_layout)
        val inputStartTime: EditText = view.findViewById(R.id.input_field_start)
        val inputEndTime: EditText = view.findViewById(R.id.input_field_end)
        val startBtn: Button = view.findViewById(R.id.start_btn)
        val updateBtn: Button = view.findViewById(R.id.update_btn)
        val stopBtn: Button = view.findViewById(R.id.stop_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_card, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.appName.text = appInfo.appName
        holder.appIcon.setImageDrawable(appInfo.appIcon)

        // Log.d("appInfo", appInfo.toString())

        // Check if the app is already in focus mode
        if (isAppInFocusMode(appInfo.packageName,appInfo.appName)) {
            holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.red, null))

            val startTime = sharedPreferences.getString("${appInfo.packageName}-start", "Not Set")
            val endTime = sharedPreferences.getString("${appInfo.packageName}-end", "Not Set")
            holder.inputStartTime.setText(startTime)
            holder.inputEndTime.setText(endTime)
        } else {
            holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.transparent, null))
            holder.inputStartTime.setText("")
            holder.inputEndTime.setText("")
        }

        // Set up time picker for start and end time fields
        holder.inputStartTime.setOnClickListener {
            showTimePickerDialog(holder.inputStartTime)
        }
        holder.inputEndTime.setOnClickListener {
            showTimePickerDialog(holder.inputEndTime)
        }

        // Toggle focus mode layout visibility
        holder.infoButton.setOnClickListener {
            toggleFocusModeLayout(holder)
        }
        holder.itemView.setOnClickListener {
            toggleFocusModeLayout(holder)
        }

        // Handle start focus mode button click
        holder.startBtn.setOnClickListener {
            val startTime = holder.inputStartTime.text.toString()
            val endTime = holder.inputEndTime.text.toString()

            if (validateTime(startTime, endTime)) {
                enableFocusMode(appInfo.packageName, appInfo.appName, startTime, endTime)
                Toast.makeText(context, "${appInfo.appName} is in focus mode from $startTime to $endTime", Toast.LENGTH_SHORT).show()
                holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.red, null))
                holder.inputStartTime.setText(startTime)
                holder.inputEndTime.setText(endTime)
            } else {
                Toast.makeText(context, "End time must be later than start time", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle update focus mode button click
        holder.updateBtn.setOnClickListener {
            val startTime = holder.inputStartTime.text.toString()
            val endTime = holder.inputEndTime.text.toString()

            if (validateTime(startTime, endTime)) {
                updateFocusModeTime(appInfo.packageName,appInfo.appName, startTime, endTime)
                Toast.makeText(context, "Updated focus mode time for ${appInfo.appName}", Toast.LENGTH_SHORT).show()
                holder.inputStartTime.setText(startTime)
                holder.inputEndTime.setText(endTime)
            } else {
                Toast.makeText(context, "End time must be later than start time", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle stop focus mode button click
        holder.stopBtn.setOnClickListener {
            disableFocusMode(appInfo.packageName,appInfo.appName)
            Toast.makeText(context, "${appInfo.appName} focus mode stopped", Toast.LENGTH_SHORT).show()
            holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.transparent, null))
            holder.inputStartTime.setText("")
            holder.inputEndTime.setText("")
        }
    }

    private fun toggleFocusModeLayout(holder: AppViewHolder) {
        holder.focusModeLayout.visibility =
            if (holder.focusModeLayout.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            editText.context,
            { _, selectedHour, selectedMinute ->
                val formattedTime = timeFormatter.format(calendar.apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }.time)
                editText.setText(formattedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun validateTime(startTime: String, endTime: String): Boolean {
        return try {
            val start = timeFormatter.parse(startTime)
            val end = timeFormatter.parse(endTime)
            start != null && end != null && end.after(start)
        } catch (e: Exception) {
            false
        }
    }

    private fun enableFocusMode(packageName:String, appName: String, startTime: String, endTime: String) {
        val editor = sharedPreferences.edit()
        editor.putString("$packageName-start", startTime)
        editor.putString("$packageName-end", endTime)
        editor.putBoolean("$packageName-focusMode", true)
        editor.apply()
    }

    private fun updateFocusModeTime(packageName:String, appName: String, startTime: String, endTime: String) {
        enableFocusMode(packageName, appName, startTime, endTime) // Simply re-use enableFocusMode for updates
    }

    private fun disableFocusMode(packageName:String,appName: String) {
        val editor = sharedPreferences.edit()
        editor.remove("$packageName-start")
        editor.remove("$packageName-end")
        editor.putBoolean("$packageName-focusMode", false)
        editor.apply()
    }

    private fun isAppInFocusMode(packageName:String,appName: String): Boolean {
        return sharedPreferences.getBoolean("$packageName-focusMode", false)
    }

    override fun getItemCount() = appList.size
}
