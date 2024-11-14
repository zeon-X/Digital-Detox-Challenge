package com.aleehatech.digitaldetoxchallange.ui.stats

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aleehatech.digitaldetoxchallange.R

class StatsAdapter(private val context: Context, private val appList: List<AppUsage>) :
    RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    // Create ViewHolder to hold individual app data
    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val appName: TextView = itemView.findViewById(R.id.appName)
        val appUsageReport: TextView = itemView.findViewById(R.id.stats_report)
        val appUsageProgressBar: ProgressBar = itemView.findViewById(R.id.appUsageProgressBar)
    }

    // Create a new view and return it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stats_card, parent, false)
        return StatsViewHolder(view)
    }

    // Bind data to the views in each list item
    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val appUsage = appList[position]

        // Set app icon and name
        holder.appIcon.setImageDrawable(appUsage.appIcon)
        holder.appName.text = appUsage.appName

        // Set usage progress
        val usageTimeInHours = appUsage.usageTime / 3600000 // Convert to hours
        val maxUsageTime = 24 // Max usage in hours for progress bar
        val progress = ((usageTimeInHours.toFloat() / maxUsageTime.toFloat()) * 100).toInt()
        holder.appUsageReport.text = usageTimeInHours.toString()+"hr/7day"
        holder.appUsageProgressBar.progress = progress
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return appList.size
    }
}
