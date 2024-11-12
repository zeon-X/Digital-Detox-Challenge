package com.aleehatech.digitaldetoxchallange.ui.home

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aleehatech.digitaldetoxchallange.R
import java.util.*

class AppAdapter(private val context: Context, private val appList: List<AppInfo>) :
    RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppSuspensionPrefs", Context.MODE_PRIVATE)

    inner class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val infoButton: ImageButton = view.findViewById(R.id.app_suspension_info_btn)
        val suspensionLayout: LinearLayout = view.findViewById(R.id.suspension_info_layout)
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

        // Check if the app is already suspended and set the background color of the button
        if (isAppSuspended(appInfo.appName)) {
            holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.red, null))
            // Set the suspension times in the EditText fields
            val startTime = sharedPreferences.getString("${appInfo.appName}-start", "Not Set")
            val endTime = sharedPreferences.getString("${appInfo.appName}-end", "Not Set")
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

        // Toggle suspension layout visibility
        holder.infoButton.setOnClickListener {
            toggleSuspensionLayout(holder)
        }
        holder.itemView.setOnClickListener {
            toggleSuspensionLayout(holder)
        }

        // Handle start suspension button click
        holder.startBtn.setOnClickListener {
            val startTime = holder.inputStartTime.text.toString()
            val endTime = holder.inputEndTime.text.toString()

            if (validateTime(startTime, endTime)) {
                suspendApp(appInfo.appName, startTime, endTime)
                Toast.makeText(context, "${appInfo.appName} is suspended from $startTime to $endTime", Toast.LENGTH_SHORT).show()
                // Update the button background color and the EditText fields
                holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.red, null))
                holder.inputStartTime.setText(startTime)
                holder.inputEndTime.setText(endTime)
            } else {
                Toast.makeText(context, "End time must be later than start time", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle update suspension button click
        holder.updateBtn.setOnClickListener {
            val startTime = holder.inputStartTime.text.toString()
            val endTime = holder.inputEndTime.text.toString()

            if (validateTime(startTime, endTime)) {
                updateSuspensionTime(appInfo.appName, startTime, endTime)
                Toast.makeText(context, "Updated suspension time for ${appInfo.appName}", Toast.LENGTH_SHORT).show()
                // Update the EditText fields with the new time
                holder.inputStartTime.setText(startTime)
                holder.inputEndTime.setText(endTime)
            } else {
                Toast.makeText(context, "End time must be later than start time", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle stop suspension button click
        holder.stopBtn.setOnClickListener {
            stopSuspension(appInfo.appName)
            Toast.makeText(context, "${appInfo.appName} suspension stopped", Toast.LENGTH_SHORT).show()
            // Reset the background color and the EditText fields
            holder.infoButton.setBackgroundColor(context.resources.getColor(R.color.transparent, null))
            holder.inputStartTime.setText("")
            holder.inputEndTime.setText("")
        }
    }

    private fun toggleSuspensionLayout(holder: AppViewHolder) {
        if (holder.suspensionLayout.visibility == View.GONE) {
            holder.suspensionLayout.visibility = View.VISIBLE
        } else {
            holder.suspensionLayout.visibility = View.GONE
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            editText.context,
            { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hourIn12HourFormat = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val formattedTime = String.format("%02d:%02d %s", hourIn12HourFormat, selectedMinute, amPm)
                editText.setText(formattedTime)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun validateTime(startTime: String, endTime: String): Boolean {
        val startHour = startTime.substring(0, 2).toInt()
        val endHour = endTime.substring(0, 2).toInt()

        return endHour > startHour || (endHour == startHour && endTime.substring(3, 5).toInt() > startTime.substring(3, 5).toInt())
    }

    private fun suspendApp(appName: String, startTime: String, endTime: String) {
        val editor = sharedPreferences.edit()
        editor.putString("$appName-start", startTime)
        editor.putString("$appName-end", endTime)
        editor.putBoolean("$appName-suspended", true)
        editor.apply()
    }

    private fun updateSuspensionTime(appName: String, startTime: String, endTime: String) {
        val editor = sharedPreferences.edit()
        editor.putString("$appName-start", startTime)
        editor.putString("$appName-end", endTime)
        editor.apply()
    }

    private fun stopSuspension(appName: String) {
        val editor = sharedPreferences.edit()
        editor.remove("$appName-start")
        editor.remove("$appName-end")
        editor.putBoolean("$appName-suspended", false)
        editor.apply()
    }

    // Check if the app is already suspended
    private fun isAppSuspended(appName: String): Boolean {
        return sharedPreferences.getBoolean("$appName-suspended", false)
    }

    override fun getItemCount() = appList.size
}
