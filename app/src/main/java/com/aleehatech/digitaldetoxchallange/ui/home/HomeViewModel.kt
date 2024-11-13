package com.aleehatech.digitaldetoxchallange.ui.home

import android.app.Application
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Data class to hold app details
//data class AppInfo(val appName: String, val appIcon: Drawable)

data class AppInfo(
    val appName: String,
    val appIcon: Drawable,
    val packageName: String
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _apps = MutableLiveData<List<AppInfo>>()
    val apps: LiveData<List<AppInfo>> = _apps

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        val packageManager = getApplication<Application>().packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        // Log.d("appInfo","from home view model: " + installedApps.toString())
        val appList = installedApps.filter {
            packageManager.getLaunchIntentForPackage(it.packageName) != null
        }.map {
            AppInfo(
                it.loadLabel(packageManager).toString(), it.loadIcon(packageManager),
                it.packageName
            )
        }
        _apps.value = appList
    }
}
