package com.example.tvlauncherapp.ui.screen.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    var isFavorite: Boolean = false
)

fun getInstalledApps(context: Context): List<AppInfo> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }
    return packageManager.queryIntentActivities(intent, 0).mapNotNull { resolveInfo ->
        val appName = resolveInfo.loadLabel(packageManager).toString()
        val packageName = resolveInfo.activityInfo.packageName
        val icon = resolveInfo.loadIcon(packageManager)
        AppInfo(appName, packageName, icon)
    }.sortedBy { it.name }
}