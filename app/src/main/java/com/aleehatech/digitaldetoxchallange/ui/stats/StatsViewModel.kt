package com.aleehatech.digitaldetoxchallange.ui.stats

import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

data class AppUsage(
    val appName: String,
    val appIcon: Drawable,
    val packageName: String,
    val usageTime: Long
)

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val _apps = MutableLiveData<List<AppUsage>>()
    val apps: LiveData<List<AppUsage>> = _apps

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        val packageManager = getApplication<Application>().packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val appList = mutableListOf<AppUsage>()

        // Get usage stats if available
        val usageStatsManager = getApplication<Application>().getSystemService(Application.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, -7) // Fetch data for the past 7 days
        val startTime = calendar.timeInMillis

        // Query usage stats for the last 7 days
        val usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, currentTime)

        if (usageStatsList != null && usageStatsList.isNotEmpty()) {
            val sortedStats = usageStatsList.sortedByDescending { it.totalTimeInForeground }

            sortedStats.forEach { usageStats ->
                // Only include apps that are installed on the device
                val app = installedApps.find { it.packageName == usageStats.packageName }

                // remove this && packageManager.getLaunchIntentForPackage(app.packageName) != null and it will show all the os app usage also
                if (app != null && packageManager.getLaunchIntentForPackage(app.packageName) != null ) {
                    appList.add(
                        AppUsage(
                            app.loadLabel(packageManager).toString(),
                            app.loadIcon(packageManager),
                            app.packageName,
                            usageStats.totalTimeInForeground // Time in milliseconds
                        )
                    )
                }
            }
        }

        _apps.value = appList
    }
}