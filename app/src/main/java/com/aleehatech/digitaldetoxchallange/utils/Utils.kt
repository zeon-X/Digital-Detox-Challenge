package com.aleehatech.digitaldetoxchallange.utils

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import android.os.Build
import android.accessibilityservice.AccessibilityService

object Utils {

    /**
     * Checks if the Accessibility Service is enabled for a specific service class.
     *
     * @param context The application context.
     * @param service The AccessibilityService class to check.
     * @return Boolean indicating whether the service is enabled.
     */
    private fun isAccessibilityServiceEnabled(context: Context, service: Class<out AccessibilityService>): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)

        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals("${context.packageName}/${service.name}", ignoreCase = true)) {
                return true
            }
        }
        return false
    }


    /**
     * Opens the Accessibility Settings screen to prompt the user to enable services.
     *
     * @param context The application context.
     */
    private fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    private fun openUsageSettings(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }




    // Utility function to check if the app has Usage Access permission
    private fun hasUsageAccess(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Prompts the user to enable the specified Accessibility Service.
     *
     * @param context The application context.
     * @param service The AccessibilityService class to enable.
     */
    fun promptUserToEnableAccessibilityService(context: Context, service: Class<out AccessibilityService>) {
        if (!isAccessibilityServiceEnabled(context, service)) {
            Toast.makeText(context, "Please enable the Focus Mode Accessibility Service", Toast.LENGTH_LONG).show()
            openAccessibilitySettings(context)
        }
    }

    fun requestUsageAccess(context: Context) {
        if (!hasUsageAccess(context)) {
            Toast.makeText(context, "Please enable Usage Access for this app", Toast.LENGTH_LONG).show()

            openUsageSettings(context)
        }
    }




}
